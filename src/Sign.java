package com.company;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class Sign {
    public Sign(Argument argument) {
        PrivateKey privateKey;
        RSAPublicKey publicKey;
        Hash hashHelper = new Hash();
        try {
            privateKey = argument.getPrivateKey();
            publicKey = argument.getPublicKey();
            //keys were created

            byte[] data = new byte[0];
            data = hashHelper.hash(Paths.get(argument.getRegisteryFile()),argument);
            //registry path were hashed
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(privateKey);
            sig.update(data);
            byte[] signatureBytes = sig.sign();
            Files.write(Paths.get(argument.getRegisteryFile()), Base64.getEncoder().encodeToString(signatureBytes).getBytes(), StandardOpenOption.APPEND);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException e) {
            e.printStackTrace();
        }
    }
}
