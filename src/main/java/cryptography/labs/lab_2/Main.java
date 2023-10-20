package cryptography.labs.lab_2;

import cryptography.labs.lab_2.utils.SymbolCalculator;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) throws Exception {
        SymbolCalculator sC = new SymbolCalculator();
        //System.out.println(sC.jacobiSymbol(51, 449));
        System.out.println(sC.legendreSymbol(68, 113));
    }
}
