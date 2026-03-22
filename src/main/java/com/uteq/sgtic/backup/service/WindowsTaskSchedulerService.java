package com.uteq.sgtic.backup.service;

import com.uteq.sgtic.backup.entity.BackupSchedule;
import com.uteq.sgtic.backup.enums.BackupFrequency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class WindowsTaskSchedulerService {

    @Value("${backup.runner.script-path}")
    private String runnerScriptPath;

    public void createOrUpdateTask(BackupSchedule schedule) {
        validateSchedule(schedule);

        List<String> command = new ArrayList<>();
        command.add("schtasks");
        command.add("/create");
        command.add("/tn");
        command.add(schedule.getWindowsTaskName());
        command.add("/tr");
        
        // PASO DEL ID DINÁMICO AL SCRIPT
        String commandToRun = "\"" + runnerScriptPath + "\" " + schedule.getCreatedBy();
        command.add(commandToRun);
        
        command.add("/sc");

        if (schedule.getFrequency() == BackupFrequency.DAILY) {
            command.add("daily");
        } else {
            command.add("weekly");
        }

        if (schedule.getFrequency() == BackupFrequency.WEEKLY) {
            command.add("/d");
            command.add(schedule.getDayOfWeek().getWindowsCode());
        }

        command.add("/st");
        command.add(schedule.getExecutionTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        command.add("/f");

        SchedulerCommandResult result = execute(command);

        if (result.exitCode != 0) {
            throw new IllegalStateException("No se pudo crear/actualizar la tarea de Windows: " + result.output);
        }
    }

    public void deleteTaskIfExists(String taskName) {
        if (!StringUtils.hasText(taskName)) {
            return;
        }

        if (!taskExists(taskName)) {
            return;
        }

        List<String> command = new ArrayList<>();
        command.add("schtasks");
        command.add("/delete");
        command.add("/tn");
        command.add(taskName);
        command.add("/f");

        SchedulerCommandResult result = execute(command);

        if (result.exitCode != 0) {
            throw new IllegalStateException("No se pudo eliminar la tarea de Windows: " + result.output);
        }
    }

    public void runTaskNow(String taskName) {
        List<String> command = new ArrayList<>();
        command.add("schtasks");
        command.add("/run");
        command.add("/tn");
        command.add(taskName);

        SchedulerCommandResult result = execute(command);

        if (result.exitCode != 0) {
            throw new IllegalStateException("No se pudo ejecutar la tarea: " + result.output);
        }
    }

    public boolean taskExists(String taskName) {
        List<String> command = new ArrayList<>();
        command.add("schtasks");
        command.add("/query");
        command.add("/tn");
        command.add(taskName);

        SchedulerCommandResult result = execute(command);
        return result.exitCode == 0;
    }

    private void validateSchedule(BackupSchedule schedule) {
        if (!StringUtils.hasText(schedule.getWindowsTaskName())) {
            throw new IllegalArgumentException("La tarea no tiene windowsTaskName.");
        }

        if (!StringUtils.hasText(runnerScriptPath)) {
            throw new IllegalArgumentException("La propiedad backup.runner.script-path no está configurada.");
        }

        if (schedule.getFrequency() == BackupFrequency.WEEKLY && schedule.getDayOfWeek() == null) {
            throw new IllegalArgumentException("Las tareas semanales requieren dayOfWeek.");
        }
    }

    private SchedulerCommandResult execute(List<String> command) {
        try {
            log.info("Ejecutando comando de Windows: {}", command);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            boolean finished = process.waitFor(60, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                return new SchedulerCommandResult(-1, "El comando excedió el tiempo máximo.");
            }

            return new SchedulerCommandResult(process.exitValue(), output.toString());

        } catch (Exception e) {
            throw new IllegalStateException("Error ejecutando schtasks: " + e.getMessage(), e);
        }
    }

    private record SchedulerCommandResult(int exitCode, String output) {
    }
}