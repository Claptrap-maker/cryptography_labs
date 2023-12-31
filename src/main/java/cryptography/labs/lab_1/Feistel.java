package cryptography.labs.lab_1;

public class Feistel implements Cipher {

    private Permutable permutable;

    public Feistel(Permutable permutable) {
        this.permutable = permutable;
    }

    @Override
    public byte[] permute(byte[] inputBits, byte[][] keyBits, boolean isDecrypt) {
        // L and R arrays are created to store the Left and Right halves of the subkey
        byte[] L = new byte[32];
        byte[] R = new byte[32];
        System.arraycopy(inputBits, 0, L, 0, 32);
        System.arraycopy(inputBits, 32, R, 0, 32);
        System.out.print("\nL0 = ");
        displayBits(L);
        System.out.print("R0 = ");
        displayBits(R);
        // 16 rounds will start here
        for (int n = 0; n < 16; n++) {
            System.out.println("\n-------------");
            System.out.println("Round " + (n + 1) + ":");
            // newR is the new R half generated by the Fiestel function. If it
            // is encrpytion, the stored subkeys are used in reverse order for decryption.
            byte[] newR;
            if (isDecrypt) {
                newR = permutable.encrypt(R, keyBits[15 - n]);
                System.out.print("Round key = ");
                displayBits(keyBits[15 - n]);
            } else {
                newR = permutable.encrypt(R, keyBits[n]);
                System.out.print("Round key = ");
                displayBits(keyBits[n]);
            }
            // xor-ing the L and new R gives the new L value. new L is stored
            // in R and new R is stored in L
            byte[] newL = xor(L, newR);
            L = R;
            R = newL;
            System.out.print("L[" + (n) + "] = ");
            displayBits(L);
            System.out.print("R[" + (n) + "] = ");
            displayBits(R);
        }
        System.arraycopy(R, 0, inputBits, 0, 32);
        System.arraycopy(L, 0, inputBits, 32, 32);
        return inputBits;
    }

    private void displayBits(byte[] bits) {
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

    private byte[] xor(byte[] a, byte[] b) {
        // Simple xor function on two int arrays
        byte[] answer = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            answer[i] = (byte) (0xff & ((int) a[i] ^ (int) b[i]));
        }
        return answer;
    }
}
