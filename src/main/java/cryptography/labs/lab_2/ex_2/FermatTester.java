package cryptography.labs.lab_2.ex_2;

public class FermatTester extends PrimalityTester {
    @Override
    public int getK(int n, float error_p) {
        return (int) Math.ceil(Utils.customLog(0.5, error_p));
    }

    @Override
    public boolean singleIteration(int n) {
        // Pick a random number in [2..n-2]
        int a = 2 + (int)(Math.random() % (n - 4));
        return Utils.getPower(a, n - 1, n) == 1;
    }
}
