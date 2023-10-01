package cryptography.labs;

public interface Permutable {

    byte[][] roundKeys(byte[] keyBits);

    byte[] encrypt(byte[] inputBits, byte[] keyBits);
}
