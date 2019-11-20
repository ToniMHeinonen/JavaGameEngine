package fi.tuni.engine;

/*
    This seems to be the only way to create a working jar when using Java 11 and JavaFX.
    I have tried at least 10 different ways, and this is the only one that worked.
*/

public class Launcher {
    public static void main(String[] args) {
        GEngine.main(args);
    }
}