package com.reqapp.controller;

import com.reqapp.domain.CrcCard;
import com.reqapp.domain.Project;
import com.reqapp.domain.UseCase;
import com.reqapp.service.ProjectService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects/{projectId}/crccards")
public class CrcCardsController {

    private final ProjectService projectService;

    private final com.reqapp.service.UserService userService;

    public CrcCardsController(ProjectService projectService, com.reqapp.service.UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public String listCrcCards(@PathVariable("projectId") Long projectId, Model model, Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        if (!projectService.hasProjectAccess(project, authentication.getName())) {
            return "redirect:/projects";
        }
        model.addAttribute("project", project);
        model.addAttribute("crcCards", projectService.findCrcCardsByProject(project));
        return "crccards";
    }

    @GetMapping("/new")
    public String showCreateForm(@PathVariable("projectId") Long projectId, Model model, Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        if (!projectService.hasProjectAccess(project, authentication.getName())) {
            return "redirect:/projects";
        }
        model.addAttribute("project", project);
        model.addAttribute("crcCard", new CrcCard());
        return "crccard_form";
    }

    @PostMapping("/new")
    public String createCrcCard(@PathVariable("projectId") Long projectId,
                                @ModelAttribute CrcCard crcCard,
                                Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        if (!projectService.hasProjectAccess(project, authentication.getName())) {
            return "redirect:/projects";
        }

        crcCard.setProject(project);
        projectService.saveCrcCard(crcCard);
        return "redirect:/projects/" + projectId + "/crccards";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("projectId") Long projectId, @PathVariable("id") Long id, Model model, Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        CrcCard crcCard = projectService.findCrcCardById(id);

        if (!projectService.hasProjectAccess(project, authentication.getName()) || crcCard == null || !crcCard.getProject().getId().equals(projectId)) {
            return "redirect:/projects";
        }

        model.addAttribute("project", project);
        model.addAttribute("crcCard", crcCard);
        return "crccard_form";
    }

    @PostMapping("/edit/{id}")
    public String updateCrcCard(@PathVariable("projectId") Long projectId,
                                @PathVariable("id") Long id,
                                @ModelAttribute CrcCard crcCardDetails,
                                Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        CrcCard existingCrcCard = projectService.findCrcCardById(id);

        if (!projectService.hasProjectAccess(project, authentication.getName()) || existingCrcCard == null || !existingCrcCard.getProject().getId().equals(projectId)) {
            return "redirect:/projects";
        }

        existingCrcCard.setClassName(crcCardDetails.getClassName());
        existingCrcCard.setResponsibilities(crcCardDetails.getResponsibilities());
        existingCrcCard.setCollaborations(crcCardDetails.getCollaborations());

        projectService.saveCrcCard(existingCrcCard);
        return "redirect:/projects/" + projectId + "/crccards";
    }

    @PostMapping("/delete/{id}")
    public String deleteCrcCard(@PathVariable("projectId") Long projectId, @PathVariable("id") Long id, Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        CrcCard crcCard = projectService.findCrcCardById(id);

        if (projectService.hasProjectAccess(project, authentication.getName()) && crcCard != null && crcCard.getProject().getId().equals(projectId)) {
            for (UseCase useCase : projectService.findUseCasesByProject(project)) {
                if (useCase.getCrcCards().contains(crcCard)) {
                    useCase.getCrcCards().remove(crcCard);
                    projectService.saveUseCase(useCase);
                }
            }
            projectService.deleteCrcCard(id);
        }
        return "redirect:/projects/" + projectId + "/crccards";
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable("projectId") Long projectId,
                             @PathVariable("id") Long id,
                             @RequestParam("text") String text,
                             Authentication authentication) {
        Project project = projectService.findProjectById(projectId);
        CrcCard crcCard = projectService.findCrcCardById(id);
        if (projectService.hasProjectAccess(project, authentication.getName()) && crcCard != null && crcCard.getProject().getId().equals(projectId)) {
            com.reqapp.domain.User author = userService.findByUsername(authentication.getName());
            com.reqapp.domain.CrcCardComment comment = new com.reqapp.domain.CrcCardComment(text, author, crcCard);
            projectService.saveCrcCardComment(comment);
        }
        return "redirect:/projects/" + projectId + "/crccards";
    }
}
