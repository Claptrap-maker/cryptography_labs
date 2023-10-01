package cryptography.labs;

public interface Cipher {
    byte[] permute(byte[] inputBits, byte[][] keyBits, boolean isDecrypt);
}
