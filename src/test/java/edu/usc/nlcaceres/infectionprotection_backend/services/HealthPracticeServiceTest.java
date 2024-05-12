package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.HealthPractice;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.HealthPracticeRepository;
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
public class HealthPracticeServiceTest {

    @Autowired
    private HealthPracticeService healthPracticeService;

    @MockBean
    private HealthPracticeRepository healthPracticeRepository;

    @Test
    public void findHealthPracticeList() throws Exception {
        List<HealthPractice> mockHealthPracticeList = ModelFactory.getHealthPracticeList();
        when(healthPracticeRepository.findAll()).thenReturn(mockHealthPracticeList);

        List<HealthPractice> healthPracticeList = healthPracticeService.getAll();
        assertThat(healthPracticeService).isNotNull();

        assertThat(healthPracticeList).isNotEmpty();
        assertThat(healthPracticeList.size()).isEqualTo(3);
    }
    @Test
    public void findSingleHealthPractice() throws Exception {
        HealthPractice mockHealthPractice = ModelFactory.getHealthPractice(null);
        when(healthPracticeRepository.findById("abc")).thenReturn(Optional.of(mockHealthPractice));

        HealthPractice healthPractice = healthPracticeService.getById("abc");
        assertThat(healthPractice).isNotNull();
        assertThat(healthPractice.getName()).isEqualTo("Hand Hygiene");
    }
    @Test
    public void unableToFindHealthPractice() throws Exception {
        assertThrows(NoSuchElementException.class, () -> {
            HealthPractice healthPractice = healthPracticeService.getById("abc");
        });
    }
}
