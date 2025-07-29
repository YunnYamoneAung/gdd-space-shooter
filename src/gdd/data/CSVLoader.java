package gdd.data;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CSVLoader {
    public static List<SpawnEntry> loadSpawns(String filepath) {
        List<SpawnEntry> entries = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filepath));
            for (int i = 1; i < lines.size(); i++) { // skip header
                String[] parts = lines.get(i).split(",");
                int frame = Integer.parseInt(parts[0].trim());
                int x = Integer.parseInt(parts[1].trim());
                int y = Integer.parseInt(parts[2].trim());
                String type = parts[3].trim();
                entries.add(new SpawnEntry(frame, x, y, type));
            }
        } catch (IOException e) {
            System.err.println("CSV Loading Error: " + e.getMessage());
        }
        return entries;
    }
}