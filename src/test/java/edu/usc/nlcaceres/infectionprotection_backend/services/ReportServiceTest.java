package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.ReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisabledInAotMode
@SpringBootTest
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @MockBean
    private ReportRepository reportRepository;

    @Test
    public void findReportList() throws Exception {
        List<Report> mockReportList = ModelFactory.getReportList();
        when(reportRepository.findAll()).thenReturn(mockReportList);

        List<Report> reportList = reportService.getAll();
        assertThat(reportService).isNotNull();

        assertThat(reportList).isNotEmpty();
        assertThat(reportList.size()).isEqualTo(1);
    }
    @Test
    public void findSingleReport() throws Exception {
        Report mockReport = ModelFactory.getReport();
        when(reportRepository.findById("abc")).thenReturn(Optional.of(mockReport));

        Report report = reportService.getById("abc");
        assertThat(report).isNotNull();
        assertThat(report.getEmployee().getFirstName()).isEqualTo("Melody");
        assertThat(report.getHealthPractice().getName()).isEqualTo("Hand Hygiene");
        assertThat(report.getLocation().getFacilityName()).isEqualTo("USC");
        assertThat(report.getLocation().getUnitNum()).isEqualTo("2");
        assertThat(report.getLocation().getRoomNum()).isEqualTo("123");
    }
    @Test
    public void unableToFindReport() throws Exception {
        assertThrows(NoSuchElementException.class, () -> {
            Report report = reportService.getById("abc");
        });
    }
}
