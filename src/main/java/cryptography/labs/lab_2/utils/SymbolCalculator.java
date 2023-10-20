package cryptography.labs.lab_2.utils;

import java.math.BigInteger;
import java.security.GeneralSecurityException;

public class SymbolCalculator {
//    private boolean isQuadraticResidue(int a, int p) {
//        boolean rsl = false;
//        for (int i = 0; i < p; i++) {
//            if (Math.pow(i, 2) % p == a % p) {
//                rsl = true;
//                break;
//            }
//        }
//        return rsl;
//    }

    public int legendreSymbol(int a, int p) throws Exception {
        a = a % p;
        if (a == 0 || a == 1) {
            return a;
        }
        if (a % 2 == 0){
            var res = Math.pow(-1,((Math.pow(p, 2) - 1) / 8));
            return legendreSymbol(a / 2, p) * (int)res;
        }
        if (a % 2 == 1){
            var res = Math.pow(-1, (float)((a - 1) * (p - 1) / 4));
            return legendreSymbol(p, a) * (int)(res);
        }
//            BigInteger q = p.subtract(BigInteger.ONE).shiftRight(1);
//            BigInteger t = x.modPow(q, p);
//            if (t.equals(BigInteger.ONE)) {
//                return 1;
//            } else if (t.equals(BigInteger.ZERO)) {
//                return 0;
//            } else if (t.add(BigInteger.ONE).equals(p)) {
//                return -1;
//            } else {
//                System.out.println("p is not prime");
//                return 404;
//            }
        throw new Exception("p must be prime");
    }

    private int[] extendedGcd(int a, int b) {
        int[] gcd;
        if (a == 0) {
            gcd = new int[]{b, 0, 1};
        } else {
            gcd = extendedGcd(b % a, a);
            int temp = gcd[1];
            gcd[1] = gcd[2] - (b / a) * temp;
            gcd[2] = temp;
        }
        return gcd;
    }

    public int jacobiSymbol(int a, int n) {
        if (a >= n) {
            a = a % n;
        }
        if (a == 0) {
            return 0;
        }
        if (a == 1) {
            return 1;
        }
        if (a < 0) {
            if ((n - 1) / 2 % 2 == 0) {
                return jacobiSymbol(-a, n);
            } else {
                return - jacobiSymbol(- a, n);
            }
        }
        if (a % 2 == 0) {
            if ((Math.pow(n, 2) - 1) / 8 % 2 == 0) {
                return jacobiSymbol(a / 2, n);
            } else {
                return - jacobiSymbol(a / 2, n);
            }
        }

        int g = extendedGcd(a, n)[0];
        if (g == a) {
            return 0;
        }
        else if (g != 1) {
            return jacobiSymbol(g, n) * jacobiSymbol(a / g, n);
        } else if ((a - 1) * ((n - 1) / 4) % 2 == 0) {
            return jacobiSymbol(n, a);
        } else {
            return - jacobiSymbol(n, a);
        }
    }
}
