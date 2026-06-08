package com.reqapp.generator;

import com.reqapp.domain.Actor;
import com.reqapp.domain.CrcCard;
import com.reqapp.domain.Project;
import com.reqapp.domain.UseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DiagramGeneratorTest {

    private Project testProject;
    private UseCase testUseCase;
    private Actor testActor;
    private CrcCard testCrcCard;

    @BeforeEach
    public void setUp() {
        testProject = new Project("Test Project", "Description", null);
        testProject.setId(1L);

        testActor = new Actor("Admin", testProject);
        testActor.setId(10L);

        testUseCase = new UseCase();
        testUseCase.setTitle("Login");
        testUseCase.setProject(testProject);
        testUseCase.setId(20L);
        testUseCase.setActors(List.of(testActor));

        testProject.setActors(List.of(testActor));
        testProject.setUseCases(List.of(testUseCase));

        testCrcCard = new CrcCard();
        testCrcCard.setId(30L);
        testCrcCard.setClassName("User");
        testCrcCard.setResponsibilities("Handle login");
        testCrcCard.setProject(testProject);

        testProject.setCrcCards(List.of(testCrcCard));
    }

    @Test
    public void testGeneratorFactory() {
        assertTrue(GeneratorFactory.createUseCaseGenerator("plantuml") instanceof PlantUmlUseCaseGenerator);
        assertTrue(GeneratorFactory.createUseCaseGenerator("nomnoml") instanceof NomnomlUseCaseGenerator);
        assertTrue(GeneratorFactory.createClassGenerator("plantuml") instanceof PlantUmlClassGenerator);
        assertTrue(GeneratorFactory.createClassGenerator("nomnoml") instanceof NomnomlClassGenerator);
        assertNull(GeneratorFactory.createUseCaseGenerator("unknown"));
    }

    @Test
    public void testPlantUmlUseCaseGenerator() {
        UseCaseGenerator generator = new PlantUmlUseCaseGenerator();
        String script = generator.generate(testProject);
        
        assertTrue(script.contains("@startuml"), "Should contain startuml");
        assertTrue(script.contains("actor \"Admin\""), "Should contain the actor");
        assertTrue(script.contains("usecase \"Login\""), "Should contain the use case");
        assertTrue(script.contains("@enduml"), "Should contain enduml");
    }

    @Test
    public void testNomnomlUseCaseGenerator() {
        UseCaseGenerator generator = new NomnomlUseCaseGenerator();
        String script = generator.generate(testProject);
        
        assertTrue(script.contains("[<actor> Admin]"), "Should contain the actor syntax");
        assertTrue(script.contains("[<usecase> Login]"), "Should contain the use case syntax");
    }

    @Test
    public void testPlantUmlClassGenerator() {
        ClassDiagramGenerator generator = new PlantUmlClassGenerator();
        String script = generator.generate(testProject);
        
        assertTrue(script.contains("@startuml"), "Should contain startuml");
        assertTrue(script.contains("User"), "Should contain the class name");
        assertTrue(script.contains("Handle login"), "Should contain the responsibilities");
    }

    @Test
    public void testNomnomlClassGenerator() {
        ClassDiagramGenerator generator = new NomnomlClassGenerator();
        String script = generator.generate(testProject);
        
        assertTrue(script.contains("[User|"), "Should contain the class name syntax");
        assertTrue(script.contains("Handle login"), "Should contain the responsibilities");
    }
}
