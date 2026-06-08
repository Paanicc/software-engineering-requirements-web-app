package com.reqapp.service;

import com.reqapp.domain.CrcCard;
import com.reqapp.domain.CrcCardComment;
import com.reqapp.domain.UseCase;
import com.reqapp.domain.UseCaseComment;
import com.reqapp.domain.User;
import com.reqapp.repository.ActorRepository;
import com.reqapp.repository.CrcCardCommentRepository;
import com.reqapp.repository.CrcCardRepository;
import com.reqapp.repository.ProjectRepository;
import com.reqapp.repository.UseCaseCommentRepository;
import com.reqapp.repository.UseCaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceCommentTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ActorRepository actorRepository;
    @Mock
    private UseCaseRepository useCaseRepository;
    @Mock
    private CrcCardRepository crcCardRepository;
    @Mock
    private UserService userService;
    @Mock
    private UseCaseCommentRepository useCaseCommentRepository;
    @Mock
    private CrcCardCommentRepository crcCardCommentRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
    }

    @Test
    public void testSaveUseCaseComment() {
        UseCase useCase = new UseCase();
        useCase.setTitle("Test Use Case");

        UseCaseComment comment = new UseCaseComment("This is a use case comment", testUser, useCase);

        projectService.saveUseCaseComment(comment);

        ArgumentCaptor<UseCaseComment> captor = ArgumentCaptor.forClass(UseCaseComment.class);
        verify(useCaseCommentRepository).save(captor.capture());

        UseCaseComment savedComment = captor.getValue();
        assertEquals("This is a use case comment", savedComment.getText());
        assertEquals("testuser", savedComment.getAuthor().getUsername());
        assertEquals("Test Use Case", savedComment.getUseCase().getTitle());
    }

    @Test
    public void testSaveCrcCardComment() {
        CrcCard crcCard = new CrcCard();
        crcCard.setClassName("TestClass");

        CrcCardComment comment = new CrcCardComment("This is a CRC card comment", testUser, crcCard);

        projectService.saveCrcCardComment(comment);

        ArgumentCaptor<CrcCardComment> captor = ArgumentCaptor.forClass(CrcCardComment.class);
        verify(crcCardCommentRepository).save(captor.capture());

        CrcCardComment savedComment = captor.getValue();
        assertEquals("This is a CRC card comment", savedComment.getText());
        assertEquals("testuser", savedComment.getAuthor().getUsername());
        assertEquals("TestClass", savedComment.getCrcCard().getClassName());
    }
}
