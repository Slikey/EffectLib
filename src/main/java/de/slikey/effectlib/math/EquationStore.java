package de.slikey.effectlib.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EquationStore {
    private static EquationStore instance;
    private Map<String, EquationTransform> transforms = new HashMap<String, EquationTransform>();

    public static final String DEFAULT_VARIABLE = "x";

    public EquationTransform getTransform(String equation) {
        return getTransform(equation, DEFAULT_VARIABLE);
    }

    public EquationTransform getTransform(String equation, String variable) {
        EquationTransform transform = transforms.get(equation);
        if (transform == null) {
            transform = new EquationTransform(equation, variable);
            transforms.put(equation, transform);
        }

        return transform;
    }

    public EquationTransform getTransform(String equation,String... variables) {
        EquationTransform transform = transforms.get(equation);
        if (transform == null) {
            transform = new EquationTransform(equation, variables);
            transforms.put(equation, transform);
        }

        return transform;
    }

    public EquationTransform getTransform(String equation, Set<String> variables) {
        EquationTransform transform = transforms.get(equation);
        if (transform == null) {
            transform = new EquationTransform(equation, variables);
            transforms.put(equation, transform);
        }
        
        return transform;
    }
    
    public static EquationStore getInstance() {
        if (instance == null) {
            instance = new EquationStore();
        }
        
        return instance;
    }
}
