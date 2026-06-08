package com.reqapp.repository;

import com.reqapp.domain.Project;
import com.reqapp.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    private User owner;
    private User teammate;
    private User unrelated;

    @BeforeEach
    public void setUp() {
        owner = new User("owner", "pass", "Owner", "User", "owner@example.com");
        teammate = new User("teammate", "pass", "Teammate", "User", "team@example.com");
        unrelated = new User("unrelated", "pass", "Unrelated", "User", "unrelated@example.com");

        owner = entityManager.persist(owner);
        teammate = entityManager.persist(teammate);
        unrelated = entityManager.persist(unrelated);

        Project project = new Project("Shared Project", "Desc", owner);
        project.getTeammates().add(teammate);
        entityManager.persist(project);

        entityManager.flush();
    }

    @Test
    public void testFindByUserOrTeammate() {
        List<Project> ownerProjects = projectRepository.findByUserOrTeammate(owner);
        assertEquals(1, ownerProjects.size(), "Owner should see 1 project");
        assertEquals("Shared Project", ownerProjects.get(0).getName());

        List<Project> teammateProjects = projectRepository.findByUserOrTeammate(teammate);
        assertEquals(1, teammateProjects.size(), "Teammate should see 1 project");
        assertEquals("Shared Project", teammateProjects.get(0).getName());

        List<Project> unrelatedProjects = projectRepository.findByUserOrTeammate(unrelated);
        assertEquals(0, unrelatedProjects.size(), "Unrelated user should see 0 projects");
    }
}
