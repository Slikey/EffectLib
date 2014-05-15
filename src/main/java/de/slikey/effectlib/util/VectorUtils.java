package de.slikey.effectlib.util;

import org.bukkit.util.Vector;

public final class VectorUtils {

	private VectorUtils() {
	}

	public static final Vector rotateAroundAxisX(Vector v, double angle) {
		double y, z, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		y = v.getY() * cos - v.getZ() * sin;
		z = v.getY() * sin + v.getZ() * cos;
		return v.setY(y).setZ(z);
	}

	public static final Vector rotateAroundAxisY(Vector v, double angle) {
		double x, z, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		x = v.getX() * cos + v.getZ() * sin;
		z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

	public static final Vector rotateAroundAxisZ(Vector v, double angle) {
		double x, y, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		x = v.getX() * cos - v.getY() * sin;
		y = v.getX() * sin + v.getY() * cos;
		return v.setX(x).setY(y);
	}

	public static final Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
		// double x = v.getX(), y = v.getY(), z = v.getZ();
		// double cosX = Math.cos(angleX), sinX = Math.sin(angleX), cosY =
		// Math.cos(angleY), sinY = Math.sin(angleY), cosZ = Math.cos(angleZ),
		// sinZ = Math.sin(angleZ);
		// double nx, ny, nz;
		// nx = (x * cosY + z * sinY) * (x * cosZ - y * sinZ);
		// ny = (y * cosX - z * sinX) * (x * sinZ + y * cosZ);
		// nz = (y * sinX + z * cosX) * (-x * sinY + z * cosY);
		// return v.setX(nx).setY(ny).setZ(nz);
		// Having some strange behavior up there.. Have to look in it later. TODO
		rotateAroundAxisX(v, angleX);
		rotateAroundAxisY(v, angleY);
		rotateAroundAxisZ(v, angleZ);
		return v;
	}

	public static final double angleToXAxis(Vector vector) {
		return Math.atan2(vector.getX(), vector.getY());
	}

}
