package cryptography.labs.lab_2.ex_2;

import cryptography.labs.lab_2.ex_1.SymbolCalculator;

public class SolovayStrassenTester extends PrimalityTester {
    private SymbolCalculator symbolCalculator = new SymbolCalculator();

    @Override
    public int getK(int n, float error_p) {
        return (int) Math.ceil(Utils.customLog(0.5, error_p));
    }

    @Override
    public boolean singleIteration(int n) throws Exception {
        int a = 2 + (int)(Math.random() % (n - 4));
        if (symbolCalculator.extendedGcd(a, n)[0] > 1)
            return false;
        int legendreSymbol = symbolCalculator.legendreSymbol(a, n);
        int temp = Utils.getPower(a, (n - 1) / 2, n);
        return legendreSymbol != 0 && (temp == Math.floorMod(legendreSymbol, n));
    }
}
