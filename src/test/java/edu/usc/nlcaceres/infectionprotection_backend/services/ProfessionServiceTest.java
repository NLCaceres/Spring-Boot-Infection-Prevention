package edu.usc.nlcaceres.infectionprotection_backend.services;

import edu.usc.nlcaceres.infectionprotection_backend.ModelFactory;
import edu.usc.nlcaceres.infectionprotection_backend.models.Profession;
import edu.usc.nlcaceres.infectionprotection_backend.repositories.ProfessionRepository;
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
public class ProfessionServiceTest {

    @Autowired
    private ProfessionService professionService;

    @MockBean
    private ProfessionRepository professionRepository;

    @Test
    public void findProfessionList() throws Exception {
        List<Profession> mockProfessionList = ModelFactory.getProfessionList();
        when(professionRepository.findAll()).thenReturn(mockProfessionList);

        List<Profession> professionList = professionService.getAll();
        assertThat(professionService).isNotNull();

        assertThat(professionList).isNotEmpty();
        assertThat(professionList.size()).isEqualTo(2);
    }
    @Test
    public void findSingleProfession() throws Exception {
        Profession mockProfession = ModelFactory.getProfession(null, null);
        when(professionRepository.findById("abc")).thenReturn(Optional.of(mockProfession));

        Profession profession = professionService.getById("abc");
        assertThat(profession).isNotNull();
        assertThat(profession.getObservedOccupation()).isEqualTo("Clinic");
        assertThat(profession.getServiceDiscipline()).isEqualTo("Doctor");
    }
    @Test
    public void unableToFindProfession() throws Exception {
        assertThrows(NoSuchElementException.class, () -> {
            professionService.getById("abc");
        });
    }
}
