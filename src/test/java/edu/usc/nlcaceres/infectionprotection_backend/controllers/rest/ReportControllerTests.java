package edu.usc.nlcaceres.infectionprotection_backend.controllers.rest;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.ReportController;
import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import edu.usc.nlcaceres.infectionprotection_backend.services.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisabledInAotMode
@WebMvcTest(ReportController.class)
public class ReportControllerTests {

    @Autowired
    private ReportController reportController;

    @MockBean
    private ReportService reportService;

    @Test
    public void findReportList() throws Exception {
        List<Report> mockReportList = ModelFactory.getReportList();
        when(reportService.getAll()).thenReturn(mockReportList);

        List<Report> actualList = reportController.getAll();
        assertThat(actualList).isEqualTo(mockReportList);
    }
    @Test
    public void findSingleReport() throws Exception {
        Report mockReport = ModelFactory.getReport();
        when(reportService.getById("abc")).thenReturn(mockReport);

        Report actualReport = reportController.getById("abc").getBody();
        assertThat(actualReport).isEqualTo(mockReport);
    }
    @Test
    public void unableToFindReport() throws Exception {
        when(reportService.getById("123")).thenThrow(new RuntimeException());

        Report actualReport = reportController.getById("123").getBody();
        assertThat(actualReport).isNull();
    }
}
