package com.company;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class Reader {
    public byte[] readSignature(String path) throws IOException {
        FileReader file = new FileReader(path);
        List<String> Lines = new ArrayList<>();
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            Lines.add(sc.nextLine());
        }
        sc.close();
        return Base64.getDecoder().decode(Lines.get(Lines.size() - 1).getBytes("UTF-8"));

    }

    public List<String> readRegistry(String path) throws IOException {
        FileReader file = new FileReader(path);
        List<String> Lines = new ArrayList<>();
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            Lines.add(sc.nextLine());
        }
        sc.close();
        Lines.remove(Lines.size() -1);
        return  Lines;
    }
}
