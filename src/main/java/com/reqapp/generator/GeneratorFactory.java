package com.reqapp.generator;

public class GeneratorFactory {
    
    public static UseCaseGenerator createUseCaseGenerator(String tool) {
        if ("plantuml".equalsIgnoreCase(tool)) {
            return new PlantUmlUseCaseGenerator();
        } else if ("nomnoml".equalsIgnoreCase(tool)) {
            return new NomnomlUseCaseGenerator();
        }
        return null;
    }
    
    public static ClassDiagramGenerator createClassGenerator(String tool) {
        if ("plantuml".equalsIgnoreCase(tool)) {
            return new PlantUmlClassGenerator();
        } else if ("nomnoml".equalsIgnoreCase(tool)) {
            return new NomnomlClassGenerator();
        }
        return null;
    }
}
