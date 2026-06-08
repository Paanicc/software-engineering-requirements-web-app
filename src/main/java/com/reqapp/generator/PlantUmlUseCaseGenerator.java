package com.reqapp.generator;

import com.reqapp.domain.Actor;
import com.reqapp.domain.Project;
import com.reqapp.domain.UseCase;

public class PlantUmlUseCaseGenerator extends UseCaseGenerator {

    @Override
    protected String generateHeader() {
        return "@startuml\nleft to right direction\n";
    }

    @Override
    protected String generateBody(Project project) {
        StringBuilder sb = new StringBuilder();
        for (Actor actor : project.getActors()) {
            sb.append("actor \"").append(actor.getName()).append("\" as actor").append(actor.getId()).append("\n");
        }
        
        sb.append("package \"").append(project.getName()).append("\" {\n");
        for (UseCase uc : project.getUseCases()) {
            sb.append("  usecase \"").append(uc.getTitle()).append("\" as uc").append(uc.getId()).append("\n");
        }
        sb.append("}\n");
        
        for (UseCase uc : project.getUseCases()) {
            for (Actor actor : uc.getActors()) {
                sb.append("actor").append(actor.getId()).append(" --> ").append("uc").append(uc.getId()).append("\n");
            }
        }
        
        return sb.toString();
    }

    @Override
    protected String generateFooter() {
        return "@enduml\n";
    }
}
