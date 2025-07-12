package com.rainy.tool;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A simple point can resolve a location.
 * Support integer.
 * @author Rainy
 * @date 2025/7/8 13:44
 * @version 1.0
 */


public class Point {
	
	public static final Point ORIGIN = new Point(0, 0, 0);
	
	public static final Point UNIT_X = new Point(1, 0, 0);
	
	public static final Point UNIT_Y = new Point(0, 1, 0);
	
	public static final Point UNIT_Z = new Point(0, 0, 1);
	
	
	private static final ConcurrentMap<String, Point> CACHE = new ConcurrentHashMap<>(1024);
	
	private final int x;
	private final int y;
	private final int z;
	
	private Point(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public final int getZ() {
		return z;
	}

	public static Point valueOf(int x, int y, int z) {
		
		if (x == 0 && y == 0 && z == 0) return ORIGIN;
        if (x == 1 && y == 0 && z == 0) return UNIT_X;
        if (x == 0 && y == 1 && z == 0) return UNIT_Y;
        if (x == 0 && y == 0 && z == 1) return UNIT_Z;
        
		String key = createKey(x, y, z);
	    return CACHE.computeIfAbsent(key, k -> new Point(x, y, z));
	}
	
	private static String createKey(int x, int y, int z) {
        return new StringBuilder(24)
            .append(x).append(':')
            .append(y).append(':')
            .append(z)
            .toString();
    }
	
	public Point add(Point other) {
		return new Point(this.x + other.x, this.y + other.y, this.z + other.z);
	}
	
	public Point subtract(Point other) {
		return new Point(this.x - other.x, this.y - other.y, this.z - other.z);
	}
	
	public double distance(Point other) {
		long dx = (long) this.x - other.x;
	    long dy = (long) this.y - other.y;
	    long dz = (long) this.z - other.z;
	    return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public long squareDistance(Point other) {
		long dx = (long) this.x - other.x;
	    long dy = (long) this.y - other.y;
	    long dz = (long) this.z - other.z;
	    return dx * dx + dy * dy + dz * dz;
	}
	
	@Override
	public String toString() {
		return "[x = " + this.x + ",y = " + this.y + ",z = " + this.z + "]";
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == this)
			return true;
		if ( !(other instanceof Point) )
			return false;
		Point p = (Point) other;
	    return x == p.x && y == p.y && z == p.z;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
	
	
	
	
}
