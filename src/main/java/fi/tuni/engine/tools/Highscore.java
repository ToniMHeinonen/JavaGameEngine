package fi.tuni.engine.tools;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Highscore {
    
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Double> scores = new ArrayList<>();
    private String path = "Highscore.ini";
    private String nameID = "name::";
    private String sep = "/";
    private String scoreID = "score::";

    public Highscore() {
        // Load scores
        try {
            List<String> list = Files.readAllLines(Path.of(path));

            for (String n : list) {
                int valueSep = n.indexOf("::");
                int separator = n.indexOf(sep);
                String name = n.substring(valueSep + 2, separator);
                valueSep = n.indexOf("::", separator);
                Double score = Double.valueOf(n.substring(valueSep + 2));

                names.add(name);
                scores.add(score);
            }
        } catch (IOException e) {}
    }

    public void addScore(String name, double score) {
        names.add(name);
        scores.add(score);
        sortScores();
        saveScores();
    }

    private void sortScores() {
        for (int j = 0; j < names.size(); j++) {
            boolean modified = false;
            
            for (int i = 0; i < names.size() - 1; i++) {
                String name = names.get(i);
                double score = scores.get(i);
                String nextName = names.get(i + 1);
                double nextScore = scores.get(i + 1);

                if (nextScore > score) {
                    names.set(i, nextName);
                    names.set(i + 1, name);
                    scores.set(i, nextScore);
                    scores.set(i + 1, score);

                    modified = true;
                }
            }

            if (!modified)
                break;
        }
    }

    private void saveScores() {

        try {
            PrintWriter out = new PrintWriter(path);
            
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                Double score = scores.get(i);

                out.println(nameID + name + sep + scoreID + score);
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearScores() {
        scores.clear();
        saveScores();
    }
}