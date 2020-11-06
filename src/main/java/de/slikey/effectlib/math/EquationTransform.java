package de.slikey.effectlib.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.configuration.ConfigurationSection;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

public class EquationTransform implements Transform {
    private Expression expression;
    private static Function randFunction;
    private static Function minFunction;
    private static Function maxFunction;
    private static Function selectFunction;
    private final Collection<String> inputVariables;
    private Exception exception;

    @Override
    public void load(ConfigurationSection parameters) {
        setEquation(parameters.getString("equation", ""));
    }

    public EquationTransform() {
        inputVariables = new ArrayList<String>();
    }
    
    public EquationTransform(String equation) {
        this(equation, "t");
    }

    public EquationTransform(String equation, String inputVariable) {
        inputVariables = new ArrayList<String>();
        inputVariables.add(inputVariable);
        setEquation(equation);
    }

    public EquationTransform(String equation, String... inputVariables) {
        this.inputVariables = new ArrayList<String>();
        for (String inputVariable : inputVariables) {
            this.inputVariables.add(inputVariable);
        }
        setEquation(equation);
    }

    public EquationTransform(String equation, Collection<String> inputVariables) {
        this.inputVariables = inputVariables;
        setEquation(equation);
    }

    private void checkCustomFunctions() {
            if (randFunction == null) {
                randFunction = new Function("rand", 2) {
                    private Random random = new Random();

                    @Override
                    public double apply(double... args) {
                        return random.nextDouble() * (args[1] - args[0]) + args[0];
                    }
                };
            }
            if (minFunction == null) {
                minFunction = new Function("min", 2) {
                    @Override
                    public double apply(double... args) {
                        return Math.min(args[0], args[1]);
                    }
                };
            }
            if (maxFunction == null) {
                maxFunction = new Function("max", 2) {
                    @Override
                    public double apply(double... args) {
                        return Math.max(args[0], args[1]);
                    }
                };
            }
            if (selectFunction == null) {
                selectFunction = new Function("select", 4) {
                    @Override
                    public double apply(double... args) {
                        if (args[0] < 0) return args[1];
                        else if (args[0] == 0) return args[2];
                        return args[3];
                    }
                };
            }
    }

    public boolean setEquation(String equation) {
        try {
            checkCustomFunctions();
            exception = null;
            expression = new ExpressionBuilder(equation)
                .function(randFunction)
                .function(minFunction)
                .function(maxFunction)
                .function(selectFunction)
                .variables(new HashSet<String>(inputVariables))
                .build();
        } catch (Exception ex) {
            expression = null;
            exception = ex;
        }
        
        return exception == null;
    }

    @Override
    public double get(double t) {
        if (expression == null) {
            return 0;
        }
        for (String inputVariable : inputVariables) {
            expression.setVariable(inputVariable, t);
        }
        return get();
    }
    
    public double get(double... t) {
        if (expression == null) {
            return 0;
        }
        int index = 0;
        for (String inputVariable : inputVariables) {
            expression.setVariable(inputVariable, t[index]);
            if (index < t.length - 1) index++;
        }
        return get();
    }
    
    public void addVariable(String key) {
        inputVariables.add(key);
    }

    public void setVariable(String key, double value) {
        if (expression != null) {
            expression.setVariable(key, value);
        }
    }

    public double get() {
        if (expression == null) {
            return Double.NaN;
        }
        double value = Double.NaN;
        try {
            exception = null;
            value = expression.evaluate();
        } catch (Exception ex) {
            exception = ex;
        }
        return value;
    }
    
    public Exception getException() {
        return exception;
    }

    public boolean isValid() {
        return exception == null;
    }
    
    public Collection<String> getParameters() {
        return inputVariables;
    }
}
