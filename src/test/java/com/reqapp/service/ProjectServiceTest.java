package com.reqapp.service;

import com.reqapp.domain.Project;
import com.reqapp.domain.User;
import com.reqapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private User owner;
    private User teammate;
    private User unrelatedUser;
    private Project project;

    @BeforeEach
    public void setUp() {
        owner = new User();
        owner.setUsername("owner");

        teammate = new User();
        teammate.setUsername("teammate");

        unrelatedUser = new User();
        unrelatedUser.setUsername("unrelated");

        project = new Project("Test Project", "Description", owner);
        project.setId(1L);
        project.setTeammates(new ArrayList<>());
    }

    @Test
    public void testSaveProject() {
        projectService.saveProject(project);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    public void testDeleteProject() {
        projectService.deleteProject(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddTeammate() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userService.findByUsername("teammate")).thenReturn(teammate);

        projectService.addTeammate(1L, "teammate");

        assertTrue(project.getTeammates().contains(teammate), "Teammate should be added to the project");
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    public void testHasProjectAccessForOwner() {
        boolean hasAccess = projectService.hasProjectAccess(project, "owner");
        assertTrue(hasAccess, "Owner should have access to the project");
    }

    @Test
    public void testHasProjectAccessForTeammate() {
        project.getTeammates().add(teammate);
        boolean hasAccess = projectService.hasProjectAccess(project, "teammate");
        assertTrue(hasAccess, "Teammate should have access to the project");
    }

    @Test
    public void testHasProjectAccessForUnrelatedUser() {
        boolean hasAccess = projectService.hasProjectAccess(project, "unrelated");
        assertFalse(hasAccess, "Unrelated user should not have access to the project");
    }
}
