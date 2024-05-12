package edu.usc.nlcaceres.infectionprotection_backend.controllers.graphql;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.PrecautionGraphController;
import edu.usc.nlcaceres.infectionprotection_backend.graphql_scalars.DateScalarConfiguration;
import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.services.PrecautionService;
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
@GraphQlTest(PrecautionGraphController.class)
@Import(DateScalarConfiguration.class)
public class PrecautionControllerTests {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private PrecautionService precautionService;

    @Test
    public void getPrecautionList() throws Exception {
        List<Precaution> mockPrecautionList = ModelFactory.getPrecautionList();
        when(precautionService.getAll()).thenReturn(mockPrecautionList);

        this.tester.document("{ precautions { id name healthPractices { id name } } }").execute()
                .path("precautions").entityList(Precaution.class).hasSize(2)
                .path("precautions[*].healthPractices[*]").entityList(HealthPractice.class).hasSize(4);
    } // If ".healthPractices" didn't have [*], then would get [[],[]] i.e. the array of 2 precautions, each with 2 healthPractices
    // Instead thanks to [*], we get a combined array of 4 healthPractices

    @Test
    public void getPrecautionById() throws Exception {
        Precaution mockPrecaution = ModelFactory.getPrecaution(null);
        when(precautionService.getById("abc")).thenReturn(mockPrecaution);

        this.tester.document("{ precautionById(id: \"abc\") { name } }").execute().path("precautionById")
                .matchesJson("{ \"name\": \"Isolation\" } ");
    }
    @Test
    public void tryToGetUnknownPrecaution() throws Exception {
        when(precautionService.getById("123")).thenThrow(new RuntimeException());
        this.tester.document("{ precautionById(id: \"123\") { name } }").execute().path("precautionById")
                .valueIsNull();
    }
}
