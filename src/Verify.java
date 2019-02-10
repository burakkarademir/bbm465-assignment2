package com.company;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.company.CustomLogger.getLogger;

public class Verify {
    public void verify(Argument argument) throws InterruptedException{

        try {
            Reader readFile = new Reader();
            List<String> line=readFile.readRegistry(argument.getRegisteryFile());
            int i;
            String lines="";
            for(i=0;i<line.size();i++){
                lines=lines+line.get(i)+"\n";
            }
            Hash m = new Hash();
            String hashtext =m.getHash(lines,argument);
            Converter convert = new Converter();
            byte [] hashbyte=convert.HexToByte(hashtext);

            byte[] digitalSignature = readFile.readSignature(argument.getRegisteryFile());
            RSAPublicKey publicKey = argument.getPublicKey();
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(hashbyte);
            boolean verified = signature.verify(digitalSignature);
            File file = new File(argument.getLogFile());
            file.createNewFile();
            if (verified) {
                List<String> newfiles = new ArrayList<>();
                List<String> registryList = readFile.readRegistry(argument.getRegisteryFile());
                findChanges(new File(argument.getMonitoredPath()),argument,registryList, newfiles);
                isDeleted(argument,newfiles,registryList);

            } else {
                String mesg ="verification failed";
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String date=dtf.format(now)+" :";
                Files.write(Paths.get(argument.getLogFile()), date.getBytes(), StandardOpenOption.APPEND);
                Files.write(Paths.get(argument.getLogFile()), (mesg+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main m = new Main();
        m.time(argument);
        verify(argument);
    }


    private void findChanges(File directory, Argument argument,List<String > registryList,List<String> newfiles){

        for (File fileEntry : directory.listFiles()) {
            if (fileEntry.isDirectory()) {
                findChanges(fileEntry, argument,registryList,newfiles);
            } else {

                try {

                    if (isExistsInRegistry(fileEntry.getCanonicalPath(),registryList)) {
                        newfiles.add(String.valueOf(fileEntry));
                        if (isChanged(fileEntry, registryList, argument)) {
                            //changed
                            String msg = fileEntry.getCanonicalPath() + " altered";
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            String date=dtf.format(now)+" : ";
                            Files.write(Paths.get(argument.getLogFile()), date.getBytes(), StandardOpenOption.APPEND);
                            Files.write(Paths.get(argument.getLogFile()), (msg+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                        }
                    }
                    else {
                        String msg = fileEntry.getCanonicalPath() + " created";
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String date=dtf.format(now)+" :";
                        Files.write(Paths.get(argument.getLogFile()), date.getBytes(), StandardOpenOption.APPEND);
                        Files.write(Paths.get(argument.getLogFile()), (msg+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    private boolean isChanged(File name, List<String > registryList, Argument argument){
        Hash hashHelper = new Hash();
        Converter convert = new Converter();
        try {
            for (String str:registryList){
                String[] tokens = str.split("\\s+");
                if (tokens[0].equals(name.getCanonicalPath())){
                    if (!(tokens[1].equals(convert.ByteToHex(hashHelper.hash(name.toPath(),argument))))){
                        return true;
                    }
                }
            }
        }   catch (Exception e){

        }

        return false;
    }

    private boolean isExistsInRegistry(String name,List<String > registryList){
        for (String str:registryList){
            String[] tokens = str.split("\\s+");
            if (tokens[0].equals(name)){
                return true;
            }
        }
        return false;
    }



    private  void isDeleted(Argument argument, List<String> newfiles, List<String > registryList) throws IOException{
            for(String str:registryList){
            String[] tokens = str.split("\\s+");
            if(newfiles.contains(tokens[0])){

            }
            else {
                String msg = tokens[0] + " deleted";
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String date=dtf.format(now)+" :";
                Files.write(Paths.get(argument.getLogFile()), date.getBytes(), StandardOpenOption.APPEND);
                Files.write(Paths.get(argument.getLogFile()), (msg+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

            }
        }


    }
}
