package edu.usc.nlcaceres.infectionprotection_backend.controllers.rest;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.controllers.HealthPracticeController;
import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.services.HealthPracticeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(HealthPracticeController.class)
public class HealthPracticeControllerTests {

    @Autowired
    private HealthPracticeController healthPracticeController;

    @MockBean
    private HealthPracticeService healthPracticeService;

    @Test
    public void findHealthPracticeList() throws Exception {
        List<HealthPractice> mockHealthPracticeList = ModelFactory.getHealthPracticeList();
        when(healthPracticeService.getAll()).thenReturn(mockHealthPracticeList);

        List<HealthPractice> actualList = healthPracticeController.getAll();
        assertThat(actualList).isEqualTo(mockHealthPracticeList);
    }
    @Test
    public void findSingleHealthPractice() throws Exception {
        HealthPractice mockHealthPractice = ModelFactory.getHealthPractice(null);
        when(healthPracticeService.getById("abc")).thenReturn(mockHealthPractice);

        HealthPractice actualHealthPractice = healthPracticeController.getById("abc").getBody();
        assertThat(actualHealthPractice).isEqualTo(mockHealthPractice);
    }
    @Test
    public void unableToFindHealthPractice() throws Exception {
        when(healthPracticeService.getById("123")).thenThrow(new RuntimeException());

        HealthPractice actualHealthPractice = healthPracticeController.getById("123").getBody();
        assertThat(actualHealthPractice).isNull();
    }
}
