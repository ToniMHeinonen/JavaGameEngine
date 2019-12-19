package io.github.tonimheinonen.engine.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Controls audio for the game.
 */
public abstract class Audio {

    private static HashMap<String, ArrayList<MediaPlayer>> audioPlayers = new HashMap<>();
    private static HashMap<String, Media> medias = new HashMap<>();
    
    /** 
     * Plays sound from given path.
     * @param path location of the sound file
     * @param loop whether to loop sound or not
     */
    public static void playSound(String path, boolean loop) {
        Media media = loadMedia(path);
        MediaPlayer player = new MediaPlayer(media);

        // If current path audio has not been played yet, create arraylist
        if (!audioPlayers.containsKey(path)) {
            ArrayList<MediaPlayer> players = new ArrayList<>();
            players.add(player);
            audioPlayers.put(path, players);
        } else {
            ArrayList<MediaPlayer> players = audioPlayers.get(path);
            players.add(player);
        }

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
            player.setOnEndOfMedia(()->soundFreeFromMemory(path, player));
        }
    }
    
    /** 
     * Stops given sound.
     * @param path location of the sound file
     */
    public static void stopSound(String path) {
        Iterator<MediaPlayer> i = audioPlayers.get(path).iterator();

        while (i.hasNext()) {
            MediaPlayer m = i.next();
            m.stop();
            soundFreeFromMemory(null, m);
            i.remove();
        }
    }
    
    /** 
     * Pauses given sound.
     * @param path location of the sound file
     */
    public static void pauseSound(String path) {
        audioPlayers.get(path).forEach(p->p.pause());
    }
    
    /** 
     * Resumes paused sound.
     * @param path location of the sound file
     */
    public static void resumeSound(String path) {
        audioPlayers.get(path).forEach(p->p.play());
    }
    
    /** 
     * Creates new media if media does not exist.
     * @param path location of the sound file
     * @return loaded media
     */
    private static Media loadMedia(String path) {
        Media media;

        if (!medias.containsKey(path)) {
            media = new Media(Audio.class.getResource("/" + path).toString());
            medias.put(path, media);
        } else {
            media = medias.get(path);
        }

        return media;
    }

    
    /** 
     * Frees sound media player from memory.
     * @param path location of the sound file
     * @param player mediaplayer to dispose and remove
     */
    private static void soundFreeFromMemory(String path, MediaPlayer player) {
        // If path is null, player is removed elsewhere
        if (path != null)
            audioPlayers.get(path).remove(player);

        player.dispose();
    }
}