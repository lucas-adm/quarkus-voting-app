package domain;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
class CandidateServiceTest {

    @Inject //Como um @Autowired
    CandidateService service;

    @InjectMock
    CandidateRepository repository;

    @Test
    void save() {
        //Dependência Instancio fazendo um Fake Candidate;
        Candidate candidate = Instancio.create(Candidate.class);

        service.save(candidate);

        verify(repository).save(candidate);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAll() {
        //Dependência Instancio criando uma Fake List;
        List<Candidate> candidates = Instancio.stream(Candidate.class).limit(10).toList();

        //Enganando a chamada do método
        when(repository.findAll()).thenReturn(candidates);

        List<Candidate> result = service.findAll();

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
        assertEquals(candidates, result);
    }

    @Test
    void findById_whenCandidateIsFound_returnsCandidate() {
        Candidate candidate = Instancio.create(Candidate.class);

        when(repository.findById(candidate.id())).thenReturn(Optional.of(candidate));

        Candidate result = service.findById(candidate.id());

        verify(repository).findById(candidate.id());
        verifyNoMoreInteractions(repository);
        assertEquals(candidate, result);
    }

    @Test
    void findById_whenCandidateIsNotFound_throwsException() {
        Candidate candidate = Instancio.create(Candidate.class);

        when(repository.findById(candidate.id())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.findById(candidate.id()));

        verify(repository).findById(candidate.id());
        verifyNoMoreInteractions(repository);
    }

}