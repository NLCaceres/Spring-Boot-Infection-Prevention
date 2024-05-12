package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.Precaution;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.PrecautionRepository;
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
public class PrecautionServiceTest {

    @Autowired
    private PrecautionService precautionService;

    @MockBean
    private PrecautionRepository precautionRepository;

    @Test
    public void findPrecautionList() throws Exception {
        List<Precaution> mockPrecautionList = ModelFactory.getPrecautionList();
        when(precautionRepository.findAll()).thenReturn(mockPrecautionList);

        List<Precaution> precautionList = precautionService.getAll();
        assertThat(precautionService).isNotNull();

        assertThat(precautionList).isNotEmpty();
        assertThat(precautionList.size()).isEqualTo(2);
    }
    @Test
    public void findSinglePrecaution() throws Exception {
        Precaution mockPrecaution = ModelFactory.getPrecaution(null);
        when(precautionRepository.findById("abc")).thenReturn(Optional.of(mockPrecaution));

        Precaution precaution = precautionService.getById("abc");
        assertThat(precaution).isNotNull();
        assertThat(precaution.getName()).isEqualTo("Isolation");
    }
    @Test
    public void unableToFindPrecaution() throws Exception {
        assertThrows(NoSuchElementException.class, () -> {
            Precaution precaution = precautionService.getById("abc");
        });
    }
}
