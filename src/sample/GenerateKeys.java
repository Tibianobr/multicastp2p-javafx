package sample;

import java.security.*;

/*
    [GENERATEKEYS] Principal responsavel pelas chaves de todos

 */

public class GenerateKeys {

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    // ao iniciar ele já faz a configuração de qual tipo de instancia usar
    // para evitar problemas de talvez duas instancias no mesmo projeto
    // e configura já o gerador das chaves
    public GenerateKeys(int keylength) throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keylength);
    }

    // Método que gera as chaves para a classe e instancia nela mesma
    public void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}