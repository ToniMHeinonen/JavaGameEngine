package io.github.tonimheinonen.engine.tools;

import java.util.HashMap;

import javafx.scene.image.Image;

public abstract class FileManager {
    private static HashMap<String, Image> createdImages = new HashMap<>();

    public static Image getImage(String path) {
        Image img;
        
        if (createdImages.get(path) == null) {
            img = new Image(path);
            createdImages.put(path, new Image(path));
        } else {
            img = createdImages.get(path);
        }

        return img;
    }

    public static void clearStoredFiles() {
        createdImages.clear();
    }
}