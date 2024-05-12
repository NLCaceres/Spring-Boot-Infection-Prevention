package edu.usc.nlcaceres.infectionprotection_backend.controllers.rest;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.ProfessionController;
import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import edu.usc.nlcaceres.infectionprotection_backend.services.ProfessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.aot.DisabledInAotMode;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisabledInAotMode
@WebMvcTest(ProfessionController.class)
public class ProfessionControllerTests {

    @Autowired
    private ProfessionController professionController;

    @MockBean
    private ProfessionService professionService;

    @Test
    public void findProfessionList() throws Exception {
        List<Profession> mockProfessionList = ModelFactory.getProfessionList();
        when(professionService.getAll()).thenReturn(mockProfessionList);

        List<Profession> actualList = professionController.getAll();
        assertThat(actualList).isEqualTo(mockProfessionList);
    }
    @Test
    public void findSingleProfession() throws Exception {
        Profession mockProfession = ModelFactory.getProfession(null, null);
        when(professionService.getById("abc")).thenReturn(mockProfession);

        Profession actualProfession = professionController.getById("abc").getBody();
        assertThat(actualProfession).isEqualTo(mockProfession);
    }
    @Test
    public void unableToFindProfession() throws Exception {
        when(professionService.getById("123")).thenThrow(new RuntimeException());

        Profession actualProfession = professionController.getById("123").getBody();
        assertThat(actualProfession).isNull();
    }
}
