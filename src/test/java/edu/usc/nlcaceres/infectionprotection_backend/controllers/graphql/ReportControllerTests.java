package edu.usc.nlcaceres.infectionprotection_backend.controllers.graphql;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.ReportGraphController;
import edu.usc.nlcaceres.infectionprotection_backend.graphql_scalars.DateScalarConfiguration;
import edu.usc.nlcaceres.infectionprotection_backend.models.Report;
import edu.usc.nlcaceres.infectionprotection_backend.services.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.util.List;
import static org.mockito.Mockito.when;

@DisabledInAotMode
@GraphQlTest(ReportGraphController.class)
@Import(DateScalarConfiguration.class)
public class ReportControllerTests {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private ReportService reportService;

    @Test
    public void getReportList() throws Exception {
        List<Report> mockReportList = ModelFactory.getReportList();
        when(reportService.getAll()).thenReturn(mockReportList);

        this.tester.documentName("reportList").execute().path("reports").entityList(Report.class).hasSize(1);
    }

    @Test
    public void getReportById() throws Exception {
        Report mockReport = ModelFactory.getReport();
        when(reportService.getById("abc")).thenReturn(mockReport);

        // matchesJson differs from the "strictly" version by ONLY checking the properties you include
        // which means if I used .matchesJson("{ }") for the following query, the test would pass because it found a parent object
        // therefore it's important that all properties you want to check are present or else false positives can appear
        // Strictly CAN fix this issue BUT if you have highly complex JSON, any slight deviation will cause a fail
        this.tester.documentName("singleReport").variable("id", "abc").execute().path("reportById")
                .matchesJson("""
                  {
                    "employee": {
                      "firstName": "Melody",
                      "surname": "Rios"
                    },
                    "healthPractice": {
                      "name": "Hand Hygiene"
                    },
                    "location": {
                      "facilityName": "USC",
                      "unitNum": "2",
                      "roomNum": "123"
                    }
                  }
                """);
    }
    @Test
    public void tryToGetUnknownReport() throws Exception {
        when(reportService.getById("123")).thenThrow(new RuntimeException());
        this.tester.documentName("singleReport").variable("id", "123").execute().path("reportById").valueIsNull();
    }
}
