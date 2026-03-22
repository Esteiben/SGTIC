package com.uteq.sgtic.dtos.student.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReportCatalogItemDTO {
    private String type;
    private String title;
    private String description;
    private String dataEndpoint;
    private String pdfEndpoint;
}
