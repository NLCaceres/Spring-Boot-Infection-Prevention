package edu.usc.nlcaceres.infectionprotection_backend.controllers.graphql;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.HealthPracticeGraphController;
import edu.usc.nlcaceres.infectionprotection_backend.graphql_scalars.DateScalarConfiguration;
import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.services.HealthPracticeService;
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
@GraphQlTest(HealthPracticeGraphController.class)
@Import(DateScalarConfiguration.class)
public class HealthPracticeControllerTests {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private HealthPracticeService healthPracticeService;

    @Test
    public void getHealthPracticeList() throws Exception {
        List<HealthPractice> mockHealthPracticeList = ModelFactory.getHealthPracticeList();
        when(healthPracticeService.getAll()).thenReturn(mockHealthPracticeList);

        this.tester.document("{ healthPractices { id name } }").execute()
                .path("healthPractices").entityList(HealthPractice.class).hasSize(3);
    }

    @Test
    public void getHealthPracticeById() throws Exception {
        HealthPractice mockHealthPractice = ModelFactory.getHealthPractice(null);
        when(healthPracticeService.getById("abc")).thenReturn(mockHealthPractice);

        this.tester.document("{ healthPracticeById(id: \"abc\") { name } }").execute().path("healthPracticeById")
                .matchesJson("{ \"name\": \"Hand Hygiene\" } ");
    }
    @Test
    public void tryToGetUnknownHealthPractice() throws Exception {
        when(healthPracticeService.getById("123")).thenThrow(new RuntimeException());
        this.tester.document("{ healthPracticeById(id: \"123\") { name } }").execute().path("healthPracticeById")
                .valueIsNull();
    }
}
