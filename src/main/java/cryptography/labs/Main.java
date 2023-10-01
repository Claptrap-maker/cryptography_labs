package cryptography.labs;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the input as a 16 character hexadecimal value:");
        String input = new Scanner(System.in).nextLine();
        byte[] inputBits = new byte[64];
        // inputBits will store the 64 bits of the input as a byte array of
        // size 64.
        hexadecimalToBinary(input, inputBits);
        System.out.println("Enter the key as a 16 character hexadecimal value:");
        String key = new Scanner(System.in).nextLine();
        byte[] keyBits = new byte[64];
        hexadecimalToBinary(key, keyBits);
        System.out.println("\n+++ ENCRYPTION +++");
        Des des = new Des();
        byte[] outputBits = des.permute(inputBits, keyBits, false);
        des.permute(outputBits, keyBits, true);
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
