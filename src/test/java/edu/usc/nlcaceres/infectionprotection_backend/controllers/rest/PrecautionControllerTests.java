package edu.usc.nlcaceres.infectionprotection_backend.controllers.rest;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.PrecautionController;
import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.services.PrecautionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.aot.DisabledInAotMode;
import java.util.List;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisabledInAotMode
@WebMvcTest(PrecautionController.class)
public class PrecautionControllerTests {

    @Autowired
    private PrecautionController precautionController;

    @MockBean
    private PrecautionService precautionService;

    @Test
    public void findPrecautionList() throws Exception {
        List<Precaution> mockPrecautionList = ModelFactory.getPrecautionList();
        when(precautionService.getAll()).thenReturn(mockPrecautionList);

        List<Precaution> actualList = precautionController.getAll();
        assertThat(actualList).isEqualTo(mockPrecautionList);
        IntStream.range(0, actualList.size()).forEach(index -> {
            Precaution precaution = actualList.get(index);
            assertThat(precaution.getHealthPractices()).hasSize(2);

            precaution.getHealthPractices().forEach(healthPractice -> {
                assertThat(healthPractice.getPrecaution()).isNotNull();
                // All Precaution backrefs created by the mock will be "Standard" even if the actual Precaution isn't
                assertThat(healthPractice.getPrecaution().getName()).isEqualTo("Standard");
            });

            Precaution mockPrecaution = mockPrecautionList.get(index);
            assertThat(precaution.getHealthPractices()).isEqualTo(mockPrecaution.getHealthPractices());
        });
    }
    @Test
    public void findSinglePrecaution() throws Exception {
        Precaution mockPrecaution = ModelFactory.getPrecaution(null);
        when(precautionService.getById("abc")).thenReturn(mockPrecaution);

        Precaution actualPrecaution = precautionController.getById("abc").getBody();
        assertThat(actualPrecaution).isEqualTo(mockPrecaution);

        assertThat(actualPrecaution.getHealthPractices()).hasSize(2);

        actualPrecaution.getHealthPractices().forEach(healthPractice -> assertThat(healthPractice.getPrecaution()).isNotNull());

        assertThat(actualPrecaution.getHealthPractices()).isEqualTo(mockPrecaution.getHealthPractices());
    }
    @Test
    public void unableToFindPrecaution() throws Exception {
        when(precautionService.getById("123")).thenThrow(new RuntimeException());

        Precaution actualPrecaution = precautionController.getById("123").getBody();
        assertThat(actualPrecaution).isNull();
    }
}
