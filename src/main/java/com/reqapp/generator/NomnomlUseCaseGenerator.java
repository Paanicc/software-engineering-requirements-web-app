package com.reqapp.generator;

import com.reqapp.domain.Actor;
import com.reqapp.domain.Project;
import com.reqapp.domain.UseCase;

public class NomnomlUseCaseGenerator extends UseCaseGenerator {

    @Override
    protected String generateHeader() {
        return "";
    }

    @Override
    protected String generateBody(Project project) {
        StringBuilder sb = new StringBuilder();
        for (Actor actor : project.getActors()) {
            sb.append("[<actor> ").append(actor.getName()).append("]\n");
        }
        
        for (UseCase uc : project.getUseCases()) {
            sb.append("[<usecase> ").append(uc.getTitle()).append("]\n");
        }
        
        for (UseCase uc : project.getUseCases()) {
            for (Actor actor : uc.getActors()) {
                sb.append("[").append(actor.getName()).append("] -> [").append(uc.getTitle()).append("]\n");
            }
        }
        
        return sb.toString();
    }

    @Override
    protected String generateFooter() {
        return "";
    }
}
