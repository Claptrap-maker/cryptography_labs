package cryptography.labs;

import java.util.Scanner;

/**
 * Implementation of DES algorithm
 */
public class Des {

    // Initial Permutation table
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

    // Array to store the number of rotations that are to be done on each round (shift left)
    private static final byte[] ROTATIONS = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
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

    // S-boxes
    private static final byte[][] S = {{
            14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7,
            0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8,
            4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0,
            15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13
    }, {
            15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10,
            3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5,
            0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15,
            13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9
    }, {
            10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8,
            13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1,
            13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7,
            1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12
    }, {
            7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15,
            13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9,
            10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4,
            3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14
    }, {
            2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9,
            14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6,
            4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14,
            11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3
    }, {
            12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11,
            10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8,
            9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6,
            4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13
    }, {
            4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1,
            13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6,
            1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2,
            6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12
    }, {
            13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7,
            1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2,
            7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8,
            2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11
    } };

    // The P-box Permutation table
    private static final byte[] P = {
            16, 7,  20, 21,
            29, 12, 28, 17,
            1,  15, 23, 26,
            5,  18, 31, 10,
            2,  8,  24, 14,
            32, 27, 3,  9,
            19, 13, 30, 6,
            22, 11, 4,  25
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

    // 28 bits each, used as storage in the KS (Key Structure) rounds to
    // generate round keys (subkeys)
    private static byte[] C = new byte[28];
    private static byte[] D = new byte[28];
    private static int counter;
    private static byte[][] subkey = new byte[16][48];

    public static void main(String[] args) {
        System.out.println("Enter the input as a 16 character hexadecimal value:");
        String input = new Scanner(System.in).nextLine();
        byte[] inputBits = new byte[64];
        // inputBits will store the 64 bits of the input as an int array of
        // size 64.
        hexadecimalToBinary(input, inputBits);
        System.out.println("Enter the key as a 16 character hexadecimal value:");
        String key = new Scanner(System.in).nextLine();
        byte[] keyBits = new byte[64];
        hexadecimalToBinary(key, keyBits);
        System.out.println("\n+++ ENCRYPTION +++");
        byte[] outputBits = permute(inputBits, keyBits, false);
        permute(outputBits, keyBits, true);
    }

    private static byte[] permute(byte[] inputBits, byte[] keyBits, boolean isDecrypt) {
        // Initial permutation takes input bits and permutes into the newBits array
        byte[] newBits = new byte[inputBits.length];
        for (int i = 0; i < inputBits.length; i++) {
            newBits[i] = inputBits[IP[i] - 1];
        }
        // 16 rounds will start here
        // L and R arrays are created to store the Left and Right halves of the subkey
        byte[] L = new byte[32];
        byte[] R = new byte[32];

        // Permuted Choice 1 is done here
        for (int i = 0; i < 28; i++) {
            C[i] = keyBits[PC1[i] - 1]; //left key part
        }
        for (int i = 28; i < 56; i++) {
            D[i - 28] = keyBits[PC1[i] - 1]; //right key part
        }

        // After that, L[i] and R[i] will be printed every round in case of checking
        System.arraycopy(newBits, 0, L, 0, 32);
        System.arraycopy(newBits, 32, R, 0, 32);
        System.out.print("\nL0 = ");
        displayBits(L);
        System.out.print("R0 = ");
        displayBits(R);
        for (int n = 0; n < 16; n++) {
            System.out.println("\n-------------");
            System.out.println("Round " + (n + 1) + ":");
            // newR is the new R half generated by the Fiestel function. If it
            // is encrpytion, the stored subkeys are used in reverse order for decryption.
            byte[] newR;
            if (isDecrypt) {
                newR = fiestel(R, subkey[15 - n]);
                System.out.print("Round key = ");
                displayBits(subkey[15 - n]);
            } else {
                newR = fiestel(R, kS(n));
                System.out.print("Round key = ");
                displayBits(subkey[n]);
            }
            // xor-ing the L and new R gives the new L value. new L is stored
            // in R and new R is stored in L
            byte[] newL = xor(L, newR);
            L = R;
            R = newL;
            System.out.print("L[" + (++counter) + "] = ");
            displayBits(L);
            System.out.print("R[" + (counter) + "] = ");
            displayBits(R);
        }
        // R and L has the two halves of the output before applying the final
        // permutation.
        byte[] output = new byte[64];
        System.arraycopy(R, 0, output, 0, 32);
        System.arraycopy(L, 0, output, 32, 32);
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

    private static byte[] kS(int round) {
        // The kS (Key Structure) function generates the round keys.
        // C1 and D1 are the new values of C and D which will be generated in
        // this round.
        byte[] C1;
        byte[] D1;
        // The rotation array is used to set how many rotations are to be done
        int rotationTimes = ROTATIONS[round];
        // leftShift() method is used for rotation
        C1 = leftShift(C, rotationTimes);
        D1 = leftShift(D, rotationTimes);
        // CnDn stores the combined C1 and D1 halves
        byte[] CnDn = new byte[56];
        System.arraycopy(C1, 0, CnDn, 0, 28);
        System.arraycopy(D1, 0, CnDn, 28, 28);
        // Kn stores the subkey, which is generated by applying the PC2 table
        // to CnDn
        byte[] Kn = new byte[48];
        for (int i = 0; i < Kn.length; i++) {
            Kn[i] = CnDn[PC2[i] - 1];
        }
        // Now I store C1 and D1 in C and D, and they become the
        // old C and D for the next round. Subkey is stored and returned.
        subkey[round] = Kn;
        C = C1;
        D = D1;
        return Kn;
    }

    private static byte[] leftShift(byte[] bits, int n) {
        // In this function each bit is rotated to the left and the leftmost bit
        // is stored at the rightmost bit.
        byte[] answer = new byte[bits.length];
        System.arraycopy(bits, 0, answer, 0, bits.length);
        for (int i = 0; i < n; i++) {
            byte temp = answer[0];
            for (int j = 0; j < bits.length - 1; j++) {
                answer[j] = answer[j + 1];
            }
            answer[bits.length - 1] = temp;
        }
        return answer;
    }

    private static byte[] fiestel(byte[] R, byte[] roundKey) {
        // Method to implement Fiestel function.
        // First the 32 bits of the R array are expanded using E table.
        byte[] expandedR = new byte[48];
        for (int i = 0; i < 48; i++) {
            expandedR[i] = R[E[i] - 1];
        }
        // I xor the expanded R and the generated round key
        byte[] temp = xor(expandedR, roundKey);
        // The S boxes are then applied to this xor result
        return sBlock(temp);
    }

    private static byte[] sBlock(byte[] bits) {
        // S-boxes are applied in this method.
        byte[] output = new byte[32];
        // As input will be of 32 bits, => it will be 8 rounds
        // (divided by 4 as I will take 4 bits of input at each iteration).
        for (int i = 0; i < 8; i++) {
            // The first and 6th bit of the current iteration gives the row bits.
            byte[] row = new byte[2];
            row[0] = bits[6 * i];
            row[1] = bits[(6 * i) + 5];
            String sRow = row[0] + "" + row[1];
            // The 4 bits between the two row bits give the column bits
            byte[] column = new byte[4];
            column[0] = bits[(6 * i) + 1];
            column[1] = bits[(6 * i) + 2];
            column[2] = bits[(6 * i) + 3];
            column[3] = bits[(6 * i) + 4];
            String sColumn = column[0] + "" + column[1] + "" + column[2] + "" + column[3];
            // Converting binary into decimal value, to be given into the
            // array as input
            int iRow = Integer.parseInt(sRow, 2);
            int iColumn = Integer.parseInt(sColumn, 2);
            int x = S[i][(iRow * 16) + iColumn];
            // The given decimal value of the S-box converting into binary:
            StringBuilder s = new StringBuilder(Integer.toBinaryString(x));
            // Padding is required since Java returns a decimal '5' as '111' in
            // binary, when I require '0111'.
            while (s.length() < 4) {
                s.insert(0, "0");
            }
            // The binary bits are appended to the output
            for (int j = 0; j < 4; j++) {
                output[(i * 4) + j] = Byte.parseByte(s.charAt(j) + "");
            }
        }
        // The P-box permutation starts here
        byte[] finalOutput = new byte[32];
        for (int i = 0; i < 32; i++) {
            finalOutput[i] = output[P[i] - 1];
        }
        return finalOutput;
    }

    private static byte[] xor(byte[] a, byte[] b) {
        // Simple xor function on two int arrays
        byte[] answer = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            answer[i] = (byte)(0xff & ((int)a[i] ^ (int)b[i]));
        }
        return answer;
    }

    private static void displayBits(byte[] bits) {
        //Function to print bits array
        for (int i = 0; i < bits.length; i += 4) {
            StringBuilder output = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                output.append(bits[i + j]);
            }
            System.out.print(Integer.toHexString(Integer.parseInt(output.toString(), 2)));
        }
        System.out.println();
    }

    private static void hexadecimalToBinary(String input, byte[] inputBits) {
        for (int i = 0; i < 16; i++) {
            // For every character in the 16 bit input, I get its binary value
            // by first parsing it into an int and then converting to a binary
            // string
            StringBuilder s = new StringBuilder(Integer.toBinaryString(Integer.parseInt(input.charAt(i) + "", 16)));

            // Java does not add padding zeros, (5 is returned as 111) but
            // I require 0111. So loop adds padding 0's to the binary value.
            while (s.length() < 4) {
                s.insert(0, "0");
            }
            // Add the 4 bits which have been extracted into the array of bits.
            for (int j = 0; j < 4; j++) {
                inputBits[(4 * i) + j] = Byte.parseByte(s.charAt(j) + "");
            }
        }
    }
}

