package cryptography.labs.lab_1;

public interface Cipher {
    byte[] permute(byte[] inputBits, byte[][] keyBits, boolean isDecrypt);
}
