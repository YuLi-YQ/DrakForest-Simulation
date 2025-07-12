package com.rainy.ThreeBody;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomGenerator {
    
	
	public static int[] generateUniqueRandom(int count, int min, int max) {
    	
    	
        if (count <= 0 || min > max) 
            throw new IllegalArgumentException("Invalid parameters");
        
        int range = max - min + 1;
        if (count > range) 
            throw new IllegalArgumentException("Count exceeds range size");

        BitSet used = new BitSet(range);
        int[] result = new int[count];
        Random rnd = new Random();
        
        for (int i = 0; i < count; i++) {
            int num;
            do {
                num = rnd.nextInt(range); 
            } while (used.get(num));
            
            used.set(num);               
            result[i] = num + min;
            
        }
        return result;
    }
    
	public static int[] generateNonNegative(int count, int sum) {
        if (count == 0 && sum == 0) return new int[0];
        if (count == 0) throw new IllegalArgumentException("count=0 requires sum=0");
        if (sum == 0) return new int[count];
        if (count == 1) return new int[]{sum};
        
        /*
        final int n = count - 1;
        final int max = sum + n;
        
        int[] dividers = generateUniqueRandom(n, 0, max);
        Arrays.sort(dividers); 


        int[] result = new int[count];
        result[0] = dividers[0];
        for (int i = 1; i < n; i++) {
            result[i] = dividers[i] - dividers[i - 1] - 1;
        }
        result[count - 1] = max - dividers[n - 1]; 
        
        return result;
        */
        
        int[] res = new int[count];
        int remaining = sum;
        Random rnd = new Random();
        
        for (int i = 0; i < count-1; i++) {
            res[i] = rnd.nextInt(remaining + 1);
            remaining -= res[i];
        }
        res[count-1] = remaining;
        return res;
    }
	
	public static long[] generateNonNegativeLong(int count, long sum) {
	    if (count == 0) {
	        if (sum == 0) return new long[0];
	        throw new IllegalArgumentException("count=0 requires sum=0");
	    }
	    if (sum == 0) return new long[count];
	    if (count == 1) return new long[]{sum};
	    
	    long[] points = new long[count - 1];
	    Random rnd = new Random();
	    for (int i = 0; i < count - 1; i++) {
	        points[i] = rnd.nextLong(sum);
	    }
	    Arrays.sort(points);
	    
	    long[] result = new long[count];
	    result[0] = points[0];
	    for (int i = 1; i < count - 1; i++) {
	        result[i] = points[i] - points[i - 1];
	    }
	    result[count - 1] = sum - points[count - 2];
	    return result;
	}

	public static long[] generateUniqueRandomLong(int count, long min, long max) {
	    if (count <= 0 || min > max) 
	        throw new IllegalArgumentException("Invalid parameters");
	    
	    long range = max - min + 1;
	    if (count > range) 
	        throw new IllegalArgumentException("Count exceeds range size");

	    Set<Long> used = new HashSet<>();
	    long[] result = new long[count];
	    Random rnd = new Random();
	    
	    for (int i = 0; i < count; i++) {
	        long num;
	        do {
	            num = min + nextLong(rnd, range);
	        } while (used.contains(num));
	        
	        used.add(num);
	        result[i] = num;
	    }
	    return result;
	}

	private static long nextLong(Random rnd, long bound) {
	    long bits, val;
	    do {
	        bits = rnd.nextLong() >>> 1;
	        val = bits % bound;
	    } while (bits - val + (bound - 1) < 0);
	    return val;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
}