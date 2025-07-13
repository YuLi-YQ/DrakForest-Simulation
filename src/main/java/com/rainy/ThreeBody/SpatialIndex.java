package com.rainy.ThreeBody;

import java.util.ArrayList;
import java.util.List;

import com.rainy.tool.Point;

public class SpatialIndex {
	
	
	private static final int GRID_SIZE = Math.max(5_000, Math.min(50_000, 
	        SimulationConfig.UNIVERSE_SIZE_X / (int)Math.cbrt(SimulationConfig.CIVILIZATION_COUNT)
		    ));
    private static final int MAX_GRID_X = SimulationConfig.UNIVERSE_SIZE_X / GRID_SIZE + 1;
    private static final int MAX_GRID_Y = SimulationConfig.UNIVERSE_SIZE_Y / GRID_SIZE + 1;
    private static final int MAX_GRID_Z = SimulationConfig.UNIVERSE_SIZE_Z / GRID_SIZE + 1;
    

    private final List<Civilization>[][][] grid = new List[MAX_GRID_X][MAX_GRID_Y][MAX_GRID_Z];
    
    private int toGridIndex(int coordinate) {
        return coordinate / GRID_SIZE;
    }
    
    public void addCivilization(Civilization civ) {
        Point loc = civ.getLocation();
        int x = toGridIndex(loc.getX());
        int y = toGridIndex(loc.getY());
        int z = toGridIndex(loc.getZ());
        
        if (grid[x][y][z] == null) {
            grid[x][y][z] = new ArrayList<>(4);
        }
        grid[x][y][z].add(civ);
    }
    
    public List<Civilization> getNearbyCivilizations(Point center, double range) {
        List<Civilization> result = new ArrayList<>(32);
        long squareRange = (long)(range * range);
        int centerX = toGridIndex(center.getX());
        int centerY = toGridIndex(center.getY());
        int centerZ = toGridIndex(center.getZ());
        
        int gridRange = (int) Math.ceil(range / GRID_SIZE);
        int minX = Math.max(0, centerX - gridRange);
        int maxX = Math.min(MAX_GRID_X - 1, centerX + gridRange);
        int minY = Math.max(0, centerY - gridRange);
        int maxY = Math.min(MAX_GRID_Y - 1, centerY + gridRange);
        int minZ = Math.max(0, centerZ - gridRange);
        int maxZ = Math.min(MAX_GRID_Z - 1, centerZ + gridRange);
        
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    List<Civilization> cell = grid[x][y][z];
                    if (cell != null) {
                        for (Civilization civ : cell) {
                            if (civ.getLocation().squareDistance(center) <= squareRange) {
                                result.add(civ);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
	
    public void updateCivilization(Civilization civ, Point oldLocation) {

        int oldX = toGridIndex(oldLocation.getX());
        int oldY = toGridIndex(oldLocation.getY());
        int oldZ = toGridIndex(oldLocation.getZ());
        

        Point newLocation = civ.getLocation();
        int newX = toGridIndex(newLocation.getX());
        int newY = toGridIndex(newLocation.getY());
        int newZ = toGridIndex(newLocation.getZ());
        

        if (oldX == newX && oldY == newY && oldZ == newZ) {
            return;
        }
        

        List<Civilization> oldCell = grid[oldX][oldY][oldZ];
        if (oldCell != null) {
            oldCell.remove(civ);
            

            if (oldCell.isEmpty()) {
                grid[oldX][oldY][oldZ] = null;
            }
        }
        

        if (grid[newX][newY][newZ] == null) {
            grid[newX][newY][newZ] = new ArrayList<>(4);
        }
        grid[newX][newY][newZ].add(civ);
    }
    
	
}