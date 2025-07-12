package com.rainy.ThreeBody;

import java.util.*;
import com.rainy.tool.Point;

public class SpatialIndex {
    private static final int GRID_SIZE = 5000; // 5k x 5k x 5k cells
    private final Map<String, List<Civilization>> grid = new HashMap<>();
    
    public void addCivilization(Civilization civ) {
        String cellKey = getCellKey(civ.getLocation());
        grid.computeIfAbsent(cellKey, k -> new ArrayList<>()).add(civ);
    }
    
    public void updateCivilization(Civilization civ, Point oldLocation) {
        String oldKey = getCellKey(oldLocation);
        String newKey = getCellKey(civ.getLocation());
        
        if (!oldKey.equals(newKey)) {
            List<Civilization> oldCell = grid.get(oldKey);
            if (oldCell != null) oldCell.remove(civ);
            
            grid.computeIfAbsent(newKey, k -> new ArrayList<>()).add(civ);
        }
    }
    
    public List<Civilization> getNearbyCivilizations(Point center, double range) {
        List<Civilization> result = new ArrayList<>();
        int gridRange = (int) Math.ceil(range / GRID_SIZE);
        
        long squareDistance = (long) (range * range);
        
        int centerX = center.getX() / GRID_SIZE;
        int centerY = center.getY() / GRID_SIZE;
        int centerZ = center.getZ() / GRID_SIZE;
        
        for (int x = centerX - gridRange; x <= centerX + gridRange; x++) {
            for (int y = centerY - gridRange; y <= centerY + gridRange; y++) {
                for (int z = centerZ - gridRange; z <= centerZ + gridRange; z++) {
                    String key = x + ":" + y + ":" + z;
                    List<Civilization> cell = grid.get(key);
                    if (cell != null) {
                        for (Civilization civ : cell) {
                        	//Initial use the sqrt,now use the square to power up.
                            if (civ.getLocation().squareDistance(center) <= squareDistance) {
                                result.add(civ);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    
    private String getCellKey(Point point) {
        int x = point.getX() / GRID_SIZE;
        int y = point.getY() / GRID_SIZE;
        int z = point.getZ() / GRID_SIZE;
        return x + ":" + y + ":" + z;
    }
}