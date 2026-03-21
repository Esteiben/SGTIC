package com.uteq.sgtic.repository.student.reports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uteq.sgtic.entities.User;
import java.util.List;

@Repository
public interface StudentReportRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT type, title, description, data_endpoint as dataEndpoint, pdf_endpoint as pdfEndpoint FROM fn_enr_get_catalog()", nativeQuery = true)
    List<ReportCatalogProjection> getReportCatalog();

    // Solo mantenemos el CAST a TEXT a la salida porque el JSON que devuelve Postgres necesita volverse String
    @Query(value = "SELECT CAST(fn_enr_get_report_data(:idUsuario, :idPeriodo, :reportType) AS TEXT)", nativeQuery = true)
    String getReportDataAsJson(
        @Param("idUsuario") Long idUsuario, 
        @Param("idPeriodo") Long idPeriodo, 
        @Param("reportType") String reportType
    );
    
    @Query(value = "SELECT fn_enr_get_report_pdf(:idUsuario, :idPeriodo, :reportType)", nativeQuery = true)
    String getReportPdfHtml(
        @Param("idUsuario") Long idUsuario, 
        @Param("idPeriodo") Long idPeriodo, 
        @Param("reportType") String reportType
    );

    public interface ReportCatalogProjection {
        String getType();
        String getTitle();
        String getDescription();
        String getDataEndpoint();
        String getPdfEndpoint();
    }
}