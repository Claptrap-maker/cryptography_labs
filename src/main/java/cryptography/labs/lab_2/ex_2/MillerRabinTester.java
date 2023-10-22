package cryptography.labs.lab_2.ex_2;

public class MillerRabinTester extends PrimalityTester {

    @Override
    public int getK(int n, float error_p) {
        double first_k = Math.ceil(Utils.customLog(2, n));
        double second_k = Math.ceil(Utils.customLog( 0.5, error_p) / 2);
        return first_k > second_k ? (int)first_k : (int)second_k;
    }

    // It returns false if n is composite and
    // returns true if n is probably prime.
    // d is an odd number such that 2^r * d
    // = n-1 for some r >= 1
    @Override
    public boolean singleIteration(int n) {
        // Pick a random number in [2..n-2]
        int a = 2 + (int)(Math.random() % (n - 4));

        // Find r such that n = 2^d * r + 1
        // for some r >= 1
        int d = n - 1;
        int r = 0;
        while (d % 2 == 0) {
            d /= 2;
            r += 1;
        }

        // Compute a^d % n
        int x = Utils.getPower(a, d, n);

        if (x == 1 || x == n - 1)
            return true;

        // Keep squaring x while one of the
        // following doesn't happen
        // (i) d does not reach n-1
        // (ii) (x^2) % n is not 1
        // (iii) (x^2) % n is not n-1
        for (int i = 0; i < r - 1; i++) {
            x = (x * x) % n;
            if (x == 1)
                return false;
            if (x == n - 1)
                return true;
        }
        // Return composite
        return false;
    }
}
