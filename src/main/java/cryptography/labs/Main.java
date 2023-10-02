package cryptography.labs;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        File keyFile = new File("D:\\des_src\\DesKey.txt");
        File textFile = new File("D:\\des_src\\DesPlainText.txt");
        File cipherFile = new File("D:\\des_src\\DesCipherText.txt");
        FileReader keyFileReader = new FileReader(keyFile);
        BufferedReader bufferedReader = new BufferedReader(keyFileReader);
        byte[] keyBits = new byte[64];
        String key = bufferedReader.readLine();
        hexadecimalToBinary(key, keyBits);
        byte[] inputBits = new byte[64];
        StringBuilder message = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(textFile)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                message.append(line);
            }
        }
        hexadecimalToBinary(message.toString(), inputBits);
        Des des = new Des();
        String cipherString = des.permute(inputBits, keyBits, false);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(cipherFile))) {
            writer.write(cipherString+"");
        }
        //des.permute(outputBits, keyBits, true);
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
