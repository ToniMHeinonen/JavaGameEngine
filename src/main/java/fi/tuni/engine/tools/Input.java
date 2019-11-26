package fi.tuni.engine.tools;

import java.util.ArrayList;

import javafx.scene.input.KeyEvent;

public abstract class Input {
    private static ArrayList<String> pressedInput = new ArrayList<>();
    private static ArrayList<String> releasedInput = new ArrayList<>();
    private static ArrayList<String> holdInput = new ArrayList<>();

    public static void handlePressed(KeyEvent e) {
        String code = e.getCode().toString();

        code = modifyInputCode(code);

        // only add once... prevent duplicates
        if (!holdInput.contains(code)) {
            holdInput.add(code);
            pressedInput.add(code);
        }
    }

    public static void handleReleased(KeyEvent e) {
        String code = e.getCode().toString();
        code = modifyInputCode(code);

        pressedInput.remove(code);
        holdInput.remove(code);
        releasedInput.add(code);
    }

    /**
     * Checks if provided key is pressed.
     * @param key to check
     * @return if pressed
     */
    public static boolean isKeyPressed(String key) {
        return checkInput(pressedInput, key);
    }

    /**
     * Checks if provided key is pressed and held.
     * @param key to check
     * @return if pressed and held
     */
    public static boolean isKeyPressedHold(String key) {
        return checkInput(holdInput, key);
    }

    /**
     * Checks if provided key is released.
     * @param key to check
     * @return if released
     */
    public static boolean isKeyReleased(String key) {
        return checkInput(releasedInput, key);
    }

    /**
     * Checks if provided input array contains provided key.
     * @param input array to check
     * @param key key to check
     * @return if input contains the key
     */
    private static boolean checkInput(ArrayList<String> input, String key) {
        // Change to uppercase to allow lowercase code
        key = key.toUpperCase();

        if (input.contains(key))
            return true;

        return false;
    }

    /**
     * Clears released input and pressed input.
     * 
     * Pressed input and released input will be cleared after each frame.
     */
    public static void resetInput() {
        if (!releasedInput.isEmpty())
            releasedInput.clear();

        if (!pressedInput.isEmpty())
            pressedInput.clear();
    }

    /**
     * Modifys some input key codes for better usability.
     * @param code to check if it should be modified
     * @return modified code
     */
    private static String modifyInputCode(String code) {
        // Remove word DIGIT from numbers
        if (code.startsWith("DIGIT"))
            code = code.substring(5, code.length());

        return code;
    }
}