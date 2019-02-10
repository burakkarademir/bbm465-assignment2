package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Formatter;




public class Registry {
    private Argument argument;

    public Registry(Argument argument) {
        this.argument = argument;
    }

    /*recursively read file create hash and write them*/
    public void createRegistryFile(File directory, Argument argument) {
        for (File fileEntry : directory.listFiles()) {
            if (fileEntry.isDirectory()) {
                createRegistryFile(fileEntry, argument);
            } else {
                Hash hashObj = new Hash();
                byte[] hashedBytes = hashObj.hash(fileEntry.toPath(), argument);
                Converter converter = new Converter();
                String a =converter.ByteToHex(hashedBytes);
                try {
                    String name = fileEntry.getCanonicalPath() + " ";
                    Files.write(Paths.get(argument.getRegisteryFile()), name.getBytes(), StandardOpenOption.APPEND);
                    Files.write(Paths.get(argument.getRegisteryFile()), a.getBytes(), StandardOpenOption.APPEND);
                    Files.write(Paths.get(argument.getRegisteryFile()), "\n".getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
