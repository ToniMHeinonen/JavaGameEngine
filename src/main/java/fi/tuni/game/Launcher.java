package fi.tuni.game;

/*
    This seems to be the only way to create a working jar when using Java 11 and JavaFX.
    I have tried at least 10 different ways, and this is the only one that worked.
*/

public class Launcher {
    /**
     * Launches the game.
     * @param args given arguments
     */
    public static void main(String[] args) {
        Game.main(args);
    }
}