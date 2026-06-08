package com.reqapp.controller;

import com.reqapp.domain.Actor;
import com.reqapp.domain.CrcCard;
import com.reqapp.domain.Project;
import com.reqapp.domain.UseCase;
import com.reqapp.service.ProjectService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projects/{projectId}/usecases")
public class UseCasesController {

    private final ProjectService projectService;

    private final com.reqapp.service.UserService userService;

    public UseCasesController(ProjectService projectService, com.reqapp.service.UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public String listUseCases(@PathVariable("projectId") Long projectId, Model model, Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        if (!projectService.hasProjectAccess(project, authentication.getName())) {
            return "redirect:/projects";
        }
        model.addAttribute("project", project);
        model.addAttribute("useCases", projectService.findUseCasesByProject(project));
        return "usecases";
    }

    @GetMapping("/new")
    public String showCreateForm(@PathVariable("projectId") Long projectId, Model model, Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        if (!projectService.hasProjectAccess(project, authentication.getName())) {
            return "redirect:/projects";
        }
        model.addAttribute("project", project);
        model.addAttribute("useCase", new UseCase());
        model.addAttribute("availableCrcCards", projectService.findCrcCardsByProject(project));
        return "usecase_form";
    }

    @PostMapping("/new")
    public String createUseCase(@PathVariable("projectId") Long projectId, 
                                @ModelAttribute UseCase useCase, 
                                @RequestParam(value = "actorNames", required = false) String actorNames,
                                @RequestParam(value = "crcCardIds", required = false) List<Long> crcCardIds,
                                Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        if (!projectService.hasProjectAccess(project, authentication.getName())) {
            return "redirect:/projects";
        }
        
        useCase.setProject(project);
        
        if (actorNames != null && !actorNames.trim().isEmpty()) {
            useCase.setActors(processActors(actorNames, project));
        }
        
        if (crcCardIds != null && !crcCardIds.isEmpty()) {
            useCase.setCrcCards(processCrcCards(crcCardIds, projectId));
        }

        projectService.saveUseCase(useCase);
        return "redirect:/projects/" + projectId + "/usecases";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("projectId") Long projectId, @PathVariable("id") Long id, Model model, Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        UseCase useCase = projectService.findUseCaseById(id);
        
        if (!projectService.hasProjectAccess(project, authentication.getName()) || useCase == null || !useCase.getProject().getId().equals(projectId)) {
            return "redirect:/projects";
        }
        List<String> actorNameList = new ArrayList<>();
        for (Actor a : useCase.getActors()) {
            actorNameList.add(a.getName());
        }
        String actorNames = String.join(", ", actorNameList);
        
        model.addAttribute("project", project);
        model.addAttribute("useCase", useCase);
        model.addAttribute("actorNames", actorNames);
        model.addAttribute("availableCrcCards", projectService.findCrcCardsByProject(project));
        return "usecase_form";
    }

    @PostMapping("/edit/{id}")
    public String updateUseCase(@PathVariable("projectId") Long projectId, 
                                @PathVariable("id") Long id, 
                                @ModelAttribute UseCase useCaseDetails, 
                                @RequestParam(value = "actorNames", required = false) String actorNames,
                                @RequestParam(value = "crcCardIds", required = false) List<Long> crcCardIds,
                                Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        UseCase existingUseCase = projectService.findUseCaseById(id);
        
        if (!projectService.hasProjectAccess(project, authentication.getName()) || existingUseCase == null || !existingUseCase.getProject().getId().equals(projectId)) {
            return "redirect:/projects";
        }
        
        existingUseCase.setTitle(useCaseDetails.getTitle());
        existingUseCase.setPreconditions(useCaseDetails.getPreconditions());
        existingUseCase.setMainFlow(useCaseDetails.getMainFlow());
        existingUseCase.setPostconditions(useCaseDetails.getPostconditions());
        
        if (actorNames != null && !actorNames.trim().isEmpty()) {
            existingUseCase.setActors(processActors(actorNames, project));
        } else {
            existingUseCase.getActors().clear();
        }
        
        if (crcCardIds != null && !crcCardIds.isEmpty()) {
            existingUseCase.setCrcCards(processCrcCards(crcCardIds, projectId));
        } else {
            existingUseCase.getCrcCards().clear();
        }
        
        projectService.saveUseCase(existingUseCase);
        return "redirect:/projects/" + projectId + "/usecases";
    }

    @PostMapping("/delete/{id}")
    public String deleteUseCase(@PathVariable("projectId") Long projectId, @PathVariable("id") Long id, Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        UseCase useCase = projectService.findUseCaseById(id);
        
        if (projectService.hasProjectAccess(project, authentication.getName()) && useCase != null && useCase.getProject().getId().equals(projectId)) {
            projectService.deleteUseCase(id);
        }
        return "redirect:/projects/" + projectId + "/usecases";
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable("projectId") Long projectId,
                             @PathVariable("id") Long id,
                             @RequestParam("text") String text,
                             Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        UseCase useCase = projectService.findUseCaseById(id);
        if (projectService.hasProjectAccess(project, authentication.getName()) && useCase != null && useCase.getProject().getId().equals(projectId)) {
            com.reqapp.domain.User author = userService.findByUsername(authentication.getName());
            com.reqapp.domain.UseCaseComment comment = new com.reqapp.domain.UseCaseComment(text, author, useCase);
            projectService.saveUseCaseComment(comment);
        }
        return "redirect:/projects/" + projectId + "/usecases";
    }

    private List<Actor> processActors(String actorNames, Project project) {
        List<Actor> useCaseActors = new ArrayList<>();
        String[] names = actorNames.split(",");
        List<Actor> existingActors = projectService.findActorsByProject(project);
        
        for (String name : names) {
            String cleanName = name.trim();
            Actor actor = existingActors.stream()
                    .filter(a -> a.getName().equalsIgnoreCase(cleanName))
                    .findFirst()
                    .orElse(null);
            
            if (actor == null) {
                actor = new Actor(cleanName, project);
                projectService.saveActor(actor);
                existingActors.add(actor);
            }
            useCaseActors.add(actor);
        }
        return useCaseActors;
    }

    private List<CrcCard> processCrcCards(List<Long> crcCardIds, Long projectId) {
        List<CrcCard> linkedCrcCards = new ArrayList<>();
        for (Long crcId : crcCardIds) {
            CrcCard card = projectService.findCrcCardById(crcId);
            if (card != null && card.getProject().getId().equals(projectId)) {
                linkedCrcCards.add(card);
            }
        }
        return linkedCrcCards;
    }
}
