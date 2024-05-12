package edu.usc.nlcaceres.infectionprotection_backend.controllers.graphql;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.ProfessionGraphController;
import edu.usc.nlcaceres.infectionprotection_backend.graphql_scalars.DateScalarConfiguration;
import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import edu.usc.nlcaceres.infectionprotection_backend.services.ProfessionService;
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
@GraphQlTest(ProfessionGraphController.class)
@Import(DateScalarConfiguration.class)
public class ProfessionControllerTests {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private ProfessionService professionService;

    @Test
    public void getProfessionList() throws Exception {
        List<Profession> mockProfessionList = ModelFactory.getProfessionList();
        when(professionService.getAll()).thenReturn(mockProfessionList);

        this.tester.document("{ professions { id serviceDiscipline observedOccupation } }").execute()
                .path("professions").entityList(Profession.class).hasSize(2);
    }

    @Test
    public void getProfessionById() throws Exception {
        Profession mockProfession = ModelFactory.getProfession(null, null);
        when(professionService.getById("abc")).thenReturn(mockProfession);

        this.tester.document("{ professionById(id: \"abc\") { observedOccupation serviceDiscipline } }").execute().path("professionById")
                .matchesJson("""
                    {
                        "observedOccupation": "Clinic",
                        "serviceDiscipline": "Doctor"
                    }
                """);
    }
    @Test
    public void tryToGetUnknownProfession() throws Exception {
        when(professionService.getById("123")).thenThrow(new RuntimeException());
        this.tester.document("{ professionById(id: \"123\") { observedOccupation serviceDiscipline } }").execute().path("professionById")
                .valueIsNull();
    }
}
