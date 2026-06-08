package com.reqapp.controller;

import com.reqapp.domain.Project;
import com.reqapp.domain.User;
import com.reqapp.service.ProjectService;
import com.reqapp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects")
public class ProjectsController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectsController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    private boolean isProjectOwner(Project project, Authentication authentication) {
        return project != null && project.getUser().getUsername().equals(authentication.getName());
    }

    @GetMapping
    public String listProjects(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("projects", projectService.findProjectsByUserOrTeammate(user));
        return "projects";
    }

    @PostMapping("/create")
    public String createProject(@RequestParam("name") String name, @RequestParam("description") String description, Authentication authentication, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(authentication.getName());
        
        boolean exists = projectService.findProjectsByUserOrTeammate(user).stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(name) && p.getUser().getUsername().equals(user.getUsername()));
                
        if (exists) {
            redirectAttributes.addFlashAttribute("error", "You already have a project with this name.");
            return "redirect:/projects";
        }
        
        Project project = new Project(name, description, user);
        projectService.saveProject(project);
        return "redirect:/projects";
    }

    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable("id") Long id, Authentication authentication) {
        Project project = projectService.findProjectById(id);
        if (isProjectOwner(project, authentication)) {
            projectService.deleteProject(id);
        }
        return "redirect:/projects";
    }

    @PostMapping("/{id}/teammates")
    public String addTeammate(@PathVariable("id") Long id, @RequestParam("username") String username, Authentication authentication) {
        Project project = projectService.findProjectById(id);
        if (isProjectOwner(project, authentication)) {
            projectService.addTeammate(id, username);
        }
        return "redirect:/projects";
    }

    @GetMapping("/{id}/generate")
    public String showGeneratorPage(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Project project = projectService.findProjectById(id);
        if (!projectService.hasProjectAccess(project, authentication.getName())) {
            return "redirect:/projects";
        }
        model.addAttribute("project", project);
        return "generator";
    }

    @PostMapping("/{id}/generate")
    public String generateScript(@PathVariable("id") Long id,
                                 @RequestParam("type") String type,
                                 @RequestParam("tool") String tool,
                                 Model model,
                                 Authentication authentication) {
        Project project = projectService.findProjectById(id);
        if (!projectService.hasProjectAccess(project, authentication.getName())) {
            return "redirect:/projects";
        }

        String script = "";
        if ("usecase".equals(type)) {
            script = projectService.generateUseCaseScript(id, tool);
        } else if ("class".equals(type)) {
            script = projectService.generateClassDiagramScript(id, tool);
        }

        model.addAttribute("project", project);
        model.addAttribute("script", script);
        model.addAttribute("type", type);
        model.addAttribute("tool", tool);
        return "generator";
    }
}
