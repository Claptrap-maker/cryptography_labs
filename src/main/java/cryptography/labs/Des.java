package cryptography.labs;

public class Des {

    private static final byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9,  1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    // Final(Inverse) permutation table
    private static final byte[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9,  49, 17, 57, 25
    };

    private Permutable desPermutation = new DesPermutation();

    private Feistel feistel = new Feistel(desPermutation);

    private byte[][] subKeys = new byte[16][48];

    public byte[] permute(byte[] inputBits, byte[] keyBits, boolean isDecrypt) {
        // Initial permutation takes input bits and permutes into the newBits array
        byte[] newBits = new byte[inputBits.length];
        for (int i = 0; i < inputBits.length; i++) {
            newBits[i] = inputBits[IP[i] - 1];
        }
        if (!isDecrypt) {
            this.subKeys = desPermutation.roundKeys(keyBits);
        }
        byte[] output = feistel.permute(newBits, subKeys, isDecrypt);
        byte[] finalOutput = new byte[64];
        // After concat of two halves, Final Permutation starts
        for (int i = 0; i < 64; i++) {
            finalOutput[i] = output[FP[i] - 1];
        }
        // Since the final output is stored as an int array of bits, then converting
        // it into a hex string:
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            StringBuilder bin = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                bin.append(finalOutput[(4 * i) + j]);
            }
            int decimal = Integer.parseInt(bin.toString(), 2);
            hex.append(Integer.toHexString(decimal));
        }
        if (isDecrypt) {
            System.out.print("Decrypted text: ");

        } else {
            System.out.print("Encrypted text: ");
        }
        System.out.println(hex.toString().toUpperCase());
        return finalOutput;
    }


    private class DesPermutation implements Permutable {

        private Permutations permutations = new Permutations();

        // Permuted Choice 1 table
        private static final byte[] PC1 = {
                57, 49, 41, 33, 25, 17, 9,
                1,  58, 50, 42, 34, 26, 18,
                10, 2,  59, 51, 43, 35, 27,
                19, 11, 3,  60, 52, 44, 36,
                63, 55, 47, 39, 31, 23, 15,
                7,  62, 54, 46, 38, 30, 22,
                14, 6,  61, 53, 45, 37, 29,
                21, 13, 5,  28, 20, 12, 4
        };

        // Permuted Choice 2 table
        private static final byte[] PC2 = {
                14, 17, 11, 24, 1,  5,
                3,  28, 15, 6,  21, 10,
                23, 19, 12, 4,  26, 8,
                16, 7,  27, 20, 13, 2,
                41, 52, 31, 37, 47, 55,
                30, 40, 51, 45, 33, 48,
                44, 49, 39, 56, 34, 53,
                46, 42, 50, 36, 29, 32
        };

        // Expansion Permutation table
        private static final byte[] E = {
                32, 1,  2,  3,  4,  5,
                4,  5,  6,  7,  8,  9,
                8,  9,  10, 11, 12, 13,
                12, 13, 14, 15, 16, 17,
                16, 17, 18, 19, 20, 21,
                20, 21, 22, 23, 24, 25,
                24, 25, 26, 27, 28, 29,
                28, 29, 30, 31, 32, 1
        };

        // Array to store the number of rotations that are to be done on each round (shift left)
        private static final byte[] ROTATIONS = {
                1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
        };

        // 28 bits each, used as storage in the KS (Key Structure) rounds to
        // generate round keys (subkeys)
        private byte[] C = new byte[28];
        private byte[] D = new byte[28];

        @Override
        public byte[][] roundKeys(byte[] keyBits) {

            // Permuted Choice 1 is done here
            for (int i = 0; i < 28; i++) {
                C[i] = keyBits[PC1[i] - 1]; //left key part
            }
            for (int i = 28; i < 56; i++) {
                D[i - 28] = keyBits[PC1[i] - 1]; //right key part
            }

            byte[][] subKeys = new byte[16][48];
            for (int n = 0; n < 16; n++) {
                // The kS (Key Structure) function generates the round keys.
                // leftShift() method is used for rotation
                leftShift(C, ROTATIONS[n]);
                leftShift(D, ROTATIONS[n]);
                // CnDn stores the combined C1 and D1 halves
                byte[] CnDn = new byte[56];
                System.arraycopy(C, 0, CnDn, 0, 28);
                System.arraycopy(D, 0, CnDn, 28, 28);
                // Kn stores the subkey, which is generated by applying the PC2 table
                // to CnDn
                byte[] Kn = new byte[48];
                for (int i = 0; i < Kn.length; i++) {
                    Kn[i] = CnDn[PC2[i] - 1];
                }
                // Now I store C1 and D1 in C and D, and they become the
                // old C and D for the next round. Subkey is stored and returned.
                subKeys[n] = Kn;
            }
            return subKeys;
        }

        private void leftShift(byte[] bits, int n) {
            // In this function each bit is rotated to the left and the leftmost bit
            // is stored at the rightmost bit.
            for (int i = 0; i < n; i++) {
                byte temp = bits[0];
                for (int j = 0; j < bits.length - 1; j++) {
                    bits[j] = bits[j + 1];
                }
                bits[bits.length - 1] = temp;
            }
        }

        @Override
        public byte[] encrypt(byte[] R, byte[] roundKey) {
            // First the 32 bits of the R array are expanded using E table.
            byte[] expandedR = new byte[48];
            for (int i = 0; i < 48; i++) {
                expandedR[i] = R[E[i] - 1];
            }
            // I xor the expanded R and the generated round key
            byte[] temp = xor(expandedR, roundKey);
            return permutations.pBox(permutations.sBlock(temp));
        }

        private byte[] xor(byte[] a, byte[] b) {
            // Simple xor function on two byte arrays
            byte[] answer = new byte[a.length];
            for (int i = 0; i < a.length; i++) {
                answer[i] = (byte)(0xff & ((int)a[i] ^ (int)b[i]));
            }
            return answer;
        }
    }
}
