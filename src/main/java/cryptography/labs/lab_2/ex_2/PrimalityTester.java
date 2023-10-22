package cryptography.labs.lab_2.ex_2;

public abstract class PrimalityTester implements PrimalityTestable {
    @Override
    public final boolean isPrime(int n, float p) throws Exception {
        if (n < 2 || n % 2 == 0)
            return false;
        float error_p = 1 - p;
        int k = getK(n, error_p);
        for (int i = 0; i < k; i++) {
            if (!singleIteration(n)) {
                return false;
            }
        }
        return true;
    }
    public abstract int getK(int n, float error_p);

    public abstract boolean singleIteration(int n) throws Exception;
}
