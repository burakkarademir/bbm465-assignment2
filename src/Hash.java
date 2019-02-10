package com.company;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Hash {
    public byte[] hash(Path path, Argument argument) {

        try (FileInputStream inputStream = new FileInputStream(String.valueOf(path))) {
            MessageDigest digest = MessageDigest.getInstance(argument.getHashFunction());

            byte[] bytesBuffer = new byte[2048];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                digest.update(bytesBuffer, 0, bytesRead);
            }
            byte[] a = digest.digest();
            return a;
        } catch (NoSuchAlgorithmException | IOException ex) {
            ex.printStackTrace();
        }

        return new byte[0];
    }

    public static String getHash(String input, Argument argument)
    {
        try {

            // Static getInstance method is called with hashing with mp5 or SHA-512(taken from command line arguments)
            MessageDigest md = MessageDigest.getInstance(argument.getHashFunction());

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
