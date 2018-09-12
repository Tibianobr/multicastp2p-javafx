package sample;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/*
    [CRIPTOGRAFIA] É uma classe auxiliar, que possui três métodos.
*/

public class Criptografia {
    // Para encriptar passamos o texto e a chave que desejamos usar para encriptação
    //(podendo ser tanto privada quanto pública). Ela retorna o texto já codificado.
    public static String encriptar(Key key, String plaintext)
            throws InvalidKeyException, NoSuchPaddingException,
            NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted =  cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Esse método funciona de maneira inversa, passamos o texto codificado e ele desencripta com a chave referente.
    public static String desencriptar(Key key, String ciphertext)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] encrypted = Base64.getDecoder().decode(ciphertext);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(encrypted));
    }

    // Esse método é interessante, ele serve para montar uma chave pública a partir de um byte array
    // como armazenamos a chave como byte array, essa função deixa de maneira mais simples codificar ela quando
    // quisermos
    public static PublicKey loadPublicKey(byte[] data) throws GeneralSecurityException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);
    }
}
