package cryptography.labs.lab_2.ex_2;

public class Utils {
    public static int getPower(int x, int y, int p) {
        int res = 1; // Initialize result

        //Update x if it is more than or
        // equal to p
        x = x % p;

        while (y > 0) {

            // If y is odd, multiply x with result
            if ((y & 1) == 1)
                res = (res * x) % p;

            // y must be even now
            y = y >> 1; // y = y/2
            x = (x * x) % p;
        }

        return res;
    }

    public static double customLog(double base, double logNumber) {
        return Math.log(logNumber) / Math.log(base);
    }
}
