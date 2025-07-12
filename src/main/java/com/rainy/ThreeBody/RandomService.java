package com.rainy.ThreeBody;

import java.util.Random;

public final class RandomService {
    private static final ThreadLocal<Random> localRandom = 
        ThreadLocal.withInitial(() -> new Random(System.nanoTime()));
    
    private RandomService() {}
    
    public static Random current() {
        return localRandom.get();
    }
    
    public static int nextInt(int bound) {
        return current().nextInt(bound);
    }
    
    public static double nextDouble() {
        return current().nextDouble();
    }
    
    public static boolean nextBoolean() {
        return current().nextBoolean();
    }
}