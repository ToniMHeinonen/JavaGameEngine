package fi.tuni.engine.tools;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Highscore {
    
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Double> scores = new ArrayList<>();
    private String path = "src/main/resources/Highscore.ini";
    private String valueIndicator = "::";
    private String nameID = "name" + valueIndicator;
    private String sep = "/";
    private String scoreID = "score" + valueIndicator;
    private boolean sortByDesc = true;

    /**
     * Loads previously saved highscores.
     */
    public Highscore() {
        try {
            // Read all the lines to a list
            List<String> list = Files.readAllLines(Path.of(path));

            // Loop through every line
            for (String n : list) {
                // Find saved name and score from line
                int valueSep = n.indexOf(valueIndicator);
                int separator = n.indexOf(sep);
                String name = n.substring(valueSep + 2, separator);
                valueSep = n.indexOf(valueIndicator, separator);
                Double score = Double.valueOf(n.substring(valueSep + 2));

                // Add found name and score to a list
                names.add(name);
                scores.add(score);
            }
        } catch (IOException e) {}
    }

    /**
     * Adds score to the list.
     * @param name to add
     * @param score to add
     */
    public void addScore(String name, double score) {
        names.add(name);
        scores.add(score);
        sortScores();
        saveScores();
    }
    
    /** 
     * Removes name and score at the given position.
     * @param index to remove at
     */
    public void removeScoreAt(int index) {
        names.remove(index);
        scores.remove(index);
        saveScores();
    }
    
    /** 
     * Sets how to sort the list.
     * @param value sort descending or ascending
     */
    public void sortByDescendingOrder(boolean value) {
        if (sortByDesc != value) {
            sortByDesc = value;
            sortScores();
            saveScores();
        }
    }

    /**
     * Sorts scores by either descending or ascending order.
     */
    private void sortScores() {
        for (int j = 0; j < names.size(); j++) {
            boolean modified = false;
            
            for (int i = 0; i < names.size() - 1; i++) {
                String name = names.get(i);
                double score = scores.get(i);
                String nextName = names.get(i + 1);
                double nextScore = scores.get(i + 1);
                double score1, score2;

                if (sortByDesc) {
                    score1 = nextScore;
                    score2 = score;
                } else {
                    score1 = score;
                    score2 = nextScore;
                }

                // Swap current and next name and score
                if (score1 > score2) {
                    names.set(i, nextName);
                    names.set(i + 1, name);
                    scores.set(i, nextScore);
                    scores.set(i + 1, score);

                    modified = true;
                }
            }

            // If nothing was modified, lists are in order
            if (!modified)
                break;
        }
    }

    /**
     * Saves scores to a file.
     */
    private void saveScores() {
        try {
            // Overwrite provided file in path
            PrintWriter out = new PrintWriter(path);
            
            // Print all the names and scores to a new line
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

    /**
     * Removes all the names and scores.
     */
    public void clearScores() {
        scores.clear();
        saveScores();
    }

    
    /** 
     * Gets all the names.
     * @return ArrayList<String> all names
     */
    public ArrayList<String> getNames() {
        return names;
    }

    
    /** 
     * Gets all the scores.
     * @return ArrayList<Double> all scores
     */
    public ArrayList<Double> getScores() {
        return scores;
    }
}