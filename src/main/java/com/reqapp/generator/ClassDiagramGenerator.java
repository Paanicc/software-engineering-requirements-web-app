package com.reqapp.generator;

import com.reqapp.domain.Project;

public abstract class ClassDiagramGenerator {
    
    public String generate(Project project) {
        StringBuilder script = new StringBuilder();
        script.append(generateHeader());
        script.append(generateBody(project));
        script.append(generateFooter());
        return script.toString();
    }
    
    protected abstract String generateHeader();
    protected abstract String generateBody(Project project);
    protected abstract String generateFooter();
}
