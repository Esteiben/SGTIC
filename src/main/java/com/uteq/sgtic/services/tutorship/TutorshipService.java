package com.uteq.sgtic.services.tutorship;

import com.uteq.sgtic.dtos.tutorship.AssignedWorkDTO;
import com.uteq.sgtic.dtos.tutorship.TutorshipReportDTO;
import com.uteq.sgtic.dtos.tutorship.TutorshipRequestDTO;
import com.uteq.sgtic.dtos.tutorship.TutorshipResponseDTO;
import com.uteq.sgtic.entities.DegreeWork;
import com.uteq.sgtic.entities.Teacher;
import com.uteq.sgtic.entities.WorkTutoring;
import com.uteq.sgtic.repository.TeacherRepository;
import com.uteq.sgtic.repository.tutorship.DegreeWorkRepository;
import com.uteq.sgtic.repository.tutorship.WorkTutoringRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutorshipService {
    private final DegreeWorkRepository degreeWorkRepository;
    private final WorkTutoringRepository workTutoringRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public void scheduleTutorship(TutorshipRequestDTO request, String userEmail) {
        Teacher director = teacherRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found for email: " + userEmail));

        DegreeWork degreeWork = degreeWorkRepository.findById(request.getIdWork())
                .orElseThrow(() -> new RuntimeException("Degree work not found with ID: " + request.getIdWork()));

        if (!degreeWork.getDirector().getIdTeacher().equals(director.getIdTeacher())) {
            throw new RuntimeException("Unauthorized: You are not the director of this degree work.");
        }

        WorkTutoring tutorship = new WorkTutoring();
        // Solo asignamos el DegreeWork, ya que este contiene al director y al estudiante
        tutorship.setDegreeWork(degreeWork);
        tutorship.setDate(request.getDate());
        tutorship.setType(request.getType());
        tutorship.setModality(request.getModality());
        tutorship.setLocationLink(request.getLocationLink());
        tutorship.setObservations(request.getObservations());
        tutorship.setRegistered(false);

        workTutoringRepository.save(tutorship);
    }

    public List<TutorshipResponseDTO> getMyTutorships(String userEmail) {
        Teacher director = teacherRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found."));

        List<WorkTutoring> tutorships = workTutoringRepository.findByDegreeWork_Director_IdTeacherOrderByDateDesc(director.getIdTeacher());

        return tutorships.stream().map(t -> {
            TutorshipResponseDTO dto = new TutorshipResponseDTO();
            dto.setIdTutoring(t.getIdTutoring());

            // Extraemos el estudiante navegando a través del DegreeWork
            String studentName = t.getDegreeWork().getStudent().getUser().getFirstName() + " " + t.getDegreeWork().getStudent().getUser().getLastName();
            dto.setStudentName(studentName);

            // Extraemos el título real de la propuesta
            String title = "";
            if (t.getDegreeWork().getWorkProposal().getTopic() != null) {
                title = t.getDegreeWork().getWorkProposal().getTopic().getTitle();
            } else if (t.getDegreeWork().getWorkProposal().getProposedTopic() != null) {
                title = t.getDegreeWork().getWorkProposal().getProposedTopic().getTitle();
            }
            dto.setThesisTitle(title);

            dto.setDate(t.getDate());
            dto.setType(t.getType());
            dto.setModality(t.getModality());
            dto.setLocationLink(t.getLocationLink());
            dto.setObservations(t.getObservations());
            dto.setStatus(t.getRegistered() ? "completed" : "pending");

            return dto;
        }).collect(Collectors.toList());
    }

    // Registrar el reporte de la tutoria
    @Transactional
    public void registerTutorshipReport(Integer idTutoring, TutorshipReportDTO reportData, String userEmail, String fileUrl) {

        Teacher director = teacherRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found."));

        WorkTutoring tutorship = workTutoringRepository.findById(idTutoring)
                .orElseThrow(() -> new RuntimeException("Tutorship not found."));

        // Validamos la propiedad navegando a través de DegreeWork
        if (!tutorship.getDegreeWork().getDirector().getIdTeacher().equals(director.getIdTeacher())) {
            throw new RuntimeException("Unauthorized: You cannot modify this tutorship.");
        }

        tutorship.setAttendance(reportData.getAttendance());
        tutorship.setObservations(reportData.getObservations());
        tutorship.setReportUrl(fileUrl);
        tutorship.setRegistered(true);

        workTutoringRepository.save(tutorship);
    }

    // Trabajos asignados al director
    public List<AssignedWorkDTO> getAssignedWorksForDirector(String userEmail) {
        Teacher director = teacherRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Teacher profile not found."));

        List<DegreeWork> works = degreeWorkRepository.findByDirector_IdTeacher(director.getIdTeacher());

        return works.stream().map(w -> {
            AssignedWorkDTO dto = new AssignedWorkDTO();
            String title = "";
            dto.setIdWork(w.getIdWork());

            String studentName = w.getStudent().getUser().getFirstName() + " " + w.getStudent().getUser().getLastName();
            dto.setStudentName(studentName);
            if (w.getWorkProposal().getTopic() != null) {
                title = w.getWorkProposal().getTopic().getTitle();
            } else if (w.getWorkProposal().getProposedTopic() != null) {
                title = w.getWorkProposal().getProposedTopic().getTitle();
            }
            dto.setTitle(title);

            return dto;
        }).collect(Collectors.toList());
    }
}