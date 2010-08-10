package org.richfaces.demo.common.data;

import java.util.Random;

public final class RandomHelper {
    
    private RandomHelper() {
      
    }
    
    public static int genRand() {
        return rand(1, 10000);
    }    
    
    public static int rand(int lo, int hi) {
        Random rn2 = new Random();
        int n = hi - lo + 1;
        int i = rn2.nextInt() % n;

        if (i < 0) {
            i = -i;
        }

        return lo + i;
    }

    public static String randomstring(int lo, int hi) {
        int n = rand(lo, hi);
        byte[] b = new byte[n];

        for (int i = 0; i < n; i++) {
            b[i] = (byte) rand('A', 'Z');
        }

        return new String(b);
    }
}
