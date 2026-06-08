package com.reqapp.generator;

import com.reqapp.domain.CrcCard;
import com.reqapp.domain.Project;

public class NomnomlClassGenerator extends ClassDiagramGenerator {

    @Override
    protected String generateHeader() {
        return "";
    }

    @Override
    protected String generateBody(Project project) {
        StringBuilder sb = new StringBuilder();
        for (CrcCard card : project.getCrcCards()) {
            sb.append("[").append(card.getClassName());
            if (card.getResponsibilities() != null && !card.getResponsibilities().trim().isEmpty()) {
                sb.append("|");
                String[] respLines = card.getResponsibilities().split("\\R");
                boolean first = true;
                for (String line : respLines) {
                    if (!line.trim().isEmpty()) {
                        if (!first) {
                            sb.append("; ");
                        }
                        sb.append(line.trim());
                        first = false;
                    }
                }
            }
            sb.append("]\n");
            
            if (card.getCollaborations() != null && !card.getCollaborations().trim().isEmpty()) {
                String[] collabs = card.getCollaborations().split(",");
                for (String collab : collabs) {
                    if (!collab.trim().isEmpty()) {
                        sb.append("[").append(card.getClassName()).append("] -> [").append(collab.trim()).append("]\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    @Override
    protected String generateFooter() {
        return "";
    }
}
