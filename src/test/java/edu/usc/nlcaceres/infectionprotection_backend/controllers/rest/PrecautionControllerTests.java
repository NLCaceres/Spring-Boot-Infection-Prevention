package edu.usc.nlcaceres.infectionprotection_backend.controllers.rest;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.PrecautionController;
import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.services.PrecautionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
    }
    @Test
    public void findSinglePrecaution() throws Exception {
        Precaution mockPrecaution = ModelFactory.getPrecaution(null);
        when(precautionService.getById("abc")).thenReturn(mockPrecaution);

        Precaution actualPrecaution = precautionController.getById("abc").getBody();
        assertThat(actualPrecaution).isEqualTo(mockPrecaution);
    }
    @Test
    public void unableToFindPrecaution() throws Exception {
        when(precautionService.getById("123")).thenThrow(new RuntimeException());

        Precaution actualPrecaution = precautionController.getById("123").getBody();
        assertThat(actualPrecaution).isNull();
    }
}
