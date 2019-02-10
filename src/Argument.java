package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Argument {
    private String type;
    private String monitoredPath;
    private String privateKeyFile;
    private String publicKeyFile;
    private String registeryFile;
    private String logFile;
    private String hashFunction;
    private int intervalTime = -1;
    private PrivateKey privateKey;
    private RSAPublicKey publicKey;


    public Argument(String[] args) {
        fillArgumentType(args);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonitoredPath() {
        return monitoredPath;
    }

    public void setMonitoredPath(String monitoredPath) {
        this.monitoredPath = monitoredPath;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getPublicKeyFile() {
        return publicKeyFile;
    }

    public void setPublicKeyFile(String publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    public String getRegisteryFile() {
        return registeryFile;
    }

    public void setRegisteryFile(String registeryFile) {
        this.registeryFile = registeryFile;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getHashFunction() {
        return hashFunction;
    }

    public void setHashFunction(String hashFunction) {
        this.hashFunction = hashFunction;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    private void fillArgumentType(String[] args) {
        for (String arg : args) {
            if (arg.equals("start")) {
                this.setType("start");
                return;
            }
            if (arg.equals("stop")) {
                this.setType("stop");
                return;
            }
        }
        this.setType("cronJob");
    }

    public void fillMonitoredPath(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-p")) {
                this.setMonitoredPath(args[i + 1]);
                return;
            }
        }
    }

    public void fillIntervalTime(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-i")) {
                this.setIntervalTime(Integer.parseInt(args[i + 1]));
                return;
            }
        }
    }

    public void fillKeys(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-k")) {
                try{
                    if (this.getType().equals("start")) {
                        this.setPrivateKeyFile(args[i + 1]);
                        this.setPublicKeyFile(args[i + 2]);
                        this.setPrivateKey();
                        this.setPublicKey();

                        return;
                    } else if (this.getType().equals("cronJob")) {
                        this.setPublicKeyFile(args[i + 1]);
                        this.setPublicKey();
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();;
                }


            }
        }
    }

    public void fillHashArgument(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-h")) {
                this.setHashFunction(args[i + 1]);
                return;
            }
        }
    }

    public void fillLogPath(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-l")) {
                this.setLogFile(args[i + 1]);
                return;
            }
        }
    }


    public void fillRegistryPath(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-r")) {
                this.setRegisteryFile(args[i + 1]);
                return;
            }
        }
    }

    private void setPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException, IOException, SignatureException, InvalidKeyException {

        String privateKeyContent = new String(Files.readAllBytes(Paths.get(this.getPrivateKeyFile())));

        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        byte[] b1 = Base64.getDecoder().decode(privateKeyContent);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(kf.generatePrivate(spec));
        byte[] s = privateSignature.sign();
        String a =Base64.getEncoder().encodeToString(s);


        this.setPrivateKey( kf.generatePrivate(keySpecPKCS8));
    }

    private void setPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException, IOException {
        String publicKeyContent = new String(Files.readAllBytes(Paths.get(this.getPublicKeyFile())));

        KeyFactory kf = KeyFactory.getInstance("RSA");

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        this.setPublicKey((RSAPublicKey) kf.generatePublic(keySpecX509));
    }
}

