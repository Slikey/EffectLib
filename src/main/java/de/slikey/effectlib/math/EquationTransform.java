package de.slikey.effectlib.math;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Random;
import java.util.logging.Level;

public class EquationTransform implements Transform {
    private Expression expression;
    private static Function randFunction;
    private final String inputVariable;

    @Override
    public void load(ConfigurationSection parameters) {
        setEquation(parameters.getString("equation", ""));
    }

    public EquationTransform() {
        inputVariable = "t";
    }

    public EquationTransform(String equation) {
        this();
        setEquation(equation);
    }

    public EquationTransform(String equation, String inputVariable) {
        this.inputVariable = inputVariable;
        setEquation(equation);
    }

    public void setEquation(String equation) {
        try {
            if (randFunction == null) {
                randFunction = new Function("rand", 2) {
                    private Random random = new Random();
                    
                    @Override
                    public double apply(double... args) {
                        return random.nextDouble() * (args[1] - args[0]) + args[0];
                    }
                };
            }
            expression = new ExpressionBuilder(equation)
                .function(randFunction)
                .variables(inputVariable)
                .build();
        } catch (Exception ex) {
            expression = null;
            org.bukkit.Bukkit.getLogger().log(Level.WARNING, "Error parsing equation " + equation, ex);
        }
    }

    @Override
    public double get(double t) {
        if (expression == null) {
            return 0;
        }
        expression.setVariable(inputVariable, t);
        return expression.evaluate();
    }
}
