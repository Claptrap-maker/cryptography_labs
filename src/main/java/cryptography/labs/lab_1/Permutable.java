package cryptography.labs.lab_1;

public interface Permutable {

    byte[][] roundKeys(byte[] keyBits);

    byte[] encrypt(byte[] inputBits, byte[] keyBits);
}
