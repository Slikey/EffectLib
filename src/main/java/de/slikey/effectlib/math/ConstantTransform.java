package de.slikey.effectlib.math;

import org.bukkit.configuration.ConfigurationSection;

public class ConstantTransform implements Transform {

    private double value;

    public ConstantTransform() {

    }

    public ConstantTransform(double value) {
        this.value = value;
    }

    @Override
    public void load(ConfigurationSection parameters) {
        value = parameters.getDouble("value");
    }

    @Override
    public double get(double t) {
        return value;
    }
}
