package com.reqapp.generator;

import com.reqapp.domain.CrcCard;
import com.reqapp.domain.Project;

public class PlantUmlClassGenerator extends ClassDiagramGenerator {

    @Override
    protected String generateHeader() {
        return "@startuml\n";
    }

    @Override
    protected String generateBody(Project project) {
        StringBuilder sb = new StringBuilder();
        for (CrcCard card : project.getCrcCards()) {
            sb.append("class \"").append(card.getClassName()).append("\" {\n");
            if (card.getResponsibilities() != null && !card.getResponsibilities().trim().isEmpty()) {
                String[] respLines = card.getResponsibilities().split("\\R");
                for (String line : respLines) {
                    if (!line.trim().isEmpty()) {
                        sb.append("  ").append(line.trim()).append("\n");
                    }
                }
            }
            sb.append("}\n");
            
            if (card.getCollaborations() != null && !card.getCollaborations().trim().isEmpty()) {
                String[] collabs = card.getCollaborations().split(",");
                for (String collab : collabs) {
                    if (!collab.trim().isEmpty()) {
                        sb.append("\"").append(card.getClassName()).append("\" --> \"").append(collab.trim()).append("\"\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    @Override
    protected String generateFooter() {
        return "@enduml\n";
    }
}
