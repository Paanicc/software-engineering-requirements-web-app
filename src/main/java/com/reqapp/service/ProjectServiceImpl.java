package com.reqapp.service;

import com.reqapp.domain.Actor;
import com.reqapp.domain.CrcCard;
import com.reqapp.domain.Project;
import com.reqapp.domain.UseCase;
import com.reqapp.domain.User;
import com.reqapp.generator.*;
import com.reqapp.repository.ActorRepository;
import com.reqapp.repository.CrcCardRepository;
import com.reqapp.repository.ProjectRepository;
import com.reqapp.repository.UseCaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ActorRepository actorRepository;
    private final UseCaseRepository useCaseRepository;
    private final CrcCardRepository crcCardRepository;
    private final UserService userService;
    private final com.reqapp.repository.UseCaseCommentRepository useCaseCommentRepository;
    private final com.reqapp.repository.CrcCardCommentRepository crcCardCommentRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, ActorRepository actorRepository, UseCaseRepository useCaseRepository, CrcCardRepository crcCardRepository, UserService userService, com.reqapp.repository.UseCaseCommentRepository useCaseCommentRepository, com.reqapp.repository.CrcCardCommentRepository crcCardCommentRepository) {
        this.projectRepository = projectRepository;
        this.actorRepository = actorRepository;
        this.useCaseRepository = useCaseRepository;
        this.crcCardRepository = crcCardRepository;
        this.userService = userService;
        this.useCaseCommentRepository = useCaseCommentRepository;
        this.crcCardCommentRepository = crcCardCommentRepository;
    }


    @Override
    public Project findProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    @Override
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public List<Actor> findActorsByProject(Project project) {
        return actorRepository.findByProject(project);
    }

    @Override
    public void saveActor(Actor actor) {
        actorRepository.save(actor);
    }


    @Override
    public List<UseCase> findUseCasesByProject(Project project) {
        return useCaseRepository.findByProject(project);
    }

    @Override
    public UseCase findUseCaseById(Long id) {
        return useCaseRepository.findById(id).orElse(null);
    }

    @Override
    public void saveUseCase(UseCase useCase) {
        useCaseRepository.save(useCase);
    }

    @Override
    public void deleteUseCase(Long id) {
        useCaseRepository.deleteById(id);
    }

    @Override
    public List<CrcCard> findCrcCardsByProject(Project project) {
        return crcCardRepository.findByProject(project);
    }

    @Override
    public CrcCard findCrcCardById(Long id) {
        return crcCardRepository.findById(id).orElse(null);
    }

    @Override
    public void saveCrcCard(CrcCard crcCard) {
        crcCardRepository.save(crcCard);
    }

    @Override
    public void deleteCrcCard(Long id) {
        crcCardRepository.deleteById(id);
    }

    @Override
    public String generateUseCaseScript(Long projectId, String tool) {
        Project project = findProjectById(projectId);
        if (project == null) return "";
        UseCaseGenerator generator = GeneratorFactory.createUseCaseGenerator(tool);
        return (generator != null) ? generator.generate(project) : "";
    }

    @Override
    public String generateClassDiagramScript(Long projectId, String tool) {
        Project project = findProjectById(projectId);
        if (project == null) return "";
        ClassDiagramGenerator generator = GeneratorFactory.createClassGenerator(tool);
        return (generator != null) ? generator.generate(project) : "";
    }

    @Override
    public List<Project> findProjectsByUserOrTeammate(User user) {
        return projectRepository.findByUserOrTeammate(user);
    }

    @Override
    public boolean hasProjectAccess(Project project, String username) {
        if (project == null) return false;
        if (project.getUser().getUsername().equals(username)) return true;
        for (User teammate : project.getTeammates()) {
            if (teammate.getUsername().equals(username)) return true;
        }
        return false;
    }

    @Override
    public void addTeammate(Long projectId, String username) {
        Project project = findProjectById(projectId);
        if (project != null) {
            User newTeammate = userService.findByUsername(username);
            if (newTeammate != null && !hasProjectAccess(project, username)) {
                project.getTeammates().add(newTeammate);
                saveProject(project);
            }
        }
    }

    @Override
    public void saveUseCaseComment(com.reqapp.domain.UseCaseComment comment) {
        useCaseCommentRepository.save(comment);
    }

    @Override
    public void saveCrcCardComment(com.reqapp.domain.CrcCardComment comment) {
        crcCardCommentRepository.save(comment);
    }
}
