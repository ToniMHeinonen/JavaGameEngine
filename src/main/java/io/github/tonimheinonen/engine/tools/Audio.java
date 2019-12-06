package io.github.tonimheinonen.engine.tools;

import java.nio.file.Paths;
import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Controls audio for the game.
 */
public abstract class Audio {

    private static HashMap<String, MediaPlayer> audioPlayers = new HashMap<>();
    private static HashMap<String, Media> medias = new HashMap<>();
    
    /** 
     * Plays sound from given path.
     * @param path location of the sound file
     * @param loop whether to loop sound or not
     */
    public static void playSound(String path, boolean loop) {
        Media media = loadMedia(path);
        MediaPlayer player = new MediaPlayer(media);
        audioPlayers.put(path, player);
        player.play();

        if (loop) {
            player.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    player.seek(Duration.ZERO);
                    player.play();
                }
            });
        } else {
            player.setOnEndOfMedia(()->soundFreeFromMemory(path));
        }
    }
    
    /** 
     * Stops given sound.
     * @param path location of the sound file
     */
    public static void stopSound(String path) {
        MediaPlayer player = audioPlayers.get(path);
        player.stop();
        soundFreeFromMemory(path);
    }
    
    /** 
     * Pauses given sound.
     * @param path location of the sound file
     */
    public static void pauseSound(String path) {
        MediaPlayer player = audioPlayers.get(path);
        player.pause();
    }
    
    /** 
     * Resumes paused sound.
     * @param path location of the sound file
     */
    public static void resumeSound(String path) {
        MediaPlayer player = audioPlayers.get(path);
        player.play();
    }
    
    /** 
     * Creates new media if media does not exist.
     * @param path location of the sound file
     * @return loaded media
     */
    private static Media loadMedia(String path) {
        Media media;

        if (!medias.containsKey(path)) {
            media = new Media(Paths.get("src/main/resources/" + path).toUri().toString());
            medias.put(path, media);
        } else {
            media = medias.get(path);
        }

        return media;
    }

    
    /** 
     * Frees sound media player from memory.
     * @param path location of the sound file
     */
    private static void soundFreeFromMemory(String path) {
        audioPlayers.remove(path);
    }
}