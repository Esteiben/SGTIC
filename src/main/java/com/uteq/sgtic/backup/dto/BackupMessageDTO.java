package com.uteq.sgtic.backup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO simple para respuestas de texto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackupMessageDTO {
    private String message;
}