package com.reqapp.service;

import com.reqapp.domain.Actor;
import com.reqapp.domain.CrcCard;
import com.reqapp.domain.Project;
import com.reqapp.domain.UseCase;
import com.reqapp.domain.User;

import java.util.List;

public interface ProjectService {
    // Project operations
    List<Project> findProjectsByUserOrTeammate(User user);
    Project findProjectById(Long id);
    void saveProject(Project project);
    void deleteProject(Long id);

    // Collaboration
    boolean hasProjectAccess(Project project, String username);
    void addTeammate(Long projectId, String username);
    void saveUseCaseComment(com.reqapp.domain.UseCaseComment comment);
    void saveCrcCardComment(com.reqapp.domain.CrcCardComment comment);

    // Actor operations
    List<Actor> findActorsByProject(Project project);
    void saveActor(Actor actor);

    // Use Case operations
    List<UseCase> findUseCasesByProject(Project project);
    UseCase findUseCaseById(Long id);
    void saveUseCase(UseCase useCase);
    void deleteUseCase(Long id);

    // CRC Card operations
    List<CrcCard> findCrcCardsByProject(Project project);
    CrcCard findCrcCardById(Long id);
    void saveCrcCard(CrcCard crcCard);
    void deleteCrcCard(Long id);

    // Diagram Generation
    String generateUseCaseScript(Long projectId, String tool);
    String generateClassDiagramScript(Long projectId, String tool);
}
