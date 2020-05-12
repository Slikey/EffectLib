package de.slikey.effectlib.math;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.configuration.ConfigurationSection;

import de.slikey.effectlib.util.VectorUtils;

public class VectorTransform {

    private Transform xTransform;
    private Transform yTransform;
    private Transform zTransform;

    private boolean orient;

    public VectorTransform(ConfigurationSection configuration) {
        xTransform = Transforms.loadTransform(configuration, "x");
        yTransform = Transforms.loadTransform(configuration, "y");
        zTransform = Transforms.loadTransform(configuration, "z");
        orient = configuration.getBoolean("orient", true);
    }

    public Vector get(Location source, double t) {
        // This returns a unit vector with the new direction calculated via the equations
        double xValue = xTransform.get(t);
        double yValue = yTransform.get(t);
        double zValue = zTransform.get(t);

        Vector result = new Vector(xValue, yValue, zValue);

        // Rotates to player's direction
        if (orient && source != null) result = VectorUtils.rotateVector(result, source);

        return result;
    }

}
