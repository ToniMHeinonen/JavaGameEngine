package io.github.tonimheinonen.engine.tools;

import java.util.ArrayList;

import javafx.scene.input.KeyEvent;

/**
 * Controls keyboard and mouse input.
 */
public abstract class Input {
    private static ArrayList<String> pressedInput = new ArrayList<>();
    private static ArrayList<String> releasedInput = new ArrayList<>();
    private static ArrayList<String> holdInput = new ArrayList<>();

    private static double mousePosX = -9999, mousePosY = -9999;
    private static double mousePressedX = -9999, mousePressedY = -9999;
    private static double mouseReleasedX = -9999, mouseReleasedY = -9999;

    /**
     * Handles pressed key event.
     * @param e pressed key
     */
    public static void handlePressed(KeyEvent e) {
        String code = e.getCode().toString();

        // Modify some key strings
        code = modifyInputCode(code);

        // only add once... prevent duplicates
        if (!holdInput.contains(code)) {
            holdInput.add(code);
            pressedInput.add(code);
        }
    }

    /**
     * Handles released key event.
     * @param e released key
     */
    public static void handleReleased(KeyEvent e) {
        String code = e.getCode().toString();
        code = modifyInputCode(code);

        // Remove released key from lists handling pressed events
        pressedInput.remove(code);
        holdInput.remove(code);
        // Add to list of released buttons
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
     * Clears released input, pressed input and mouse values.
     * 
     * All these input values will be cleared after each frame.
     */
    public static void resetInput() {
        if (!releasedInput.isEmpty())
            releasedInput.clear();

        if (!pressedInput.isEmpty())
            pressedInput.clear();

        mousePressedX = -9999;
        mousePressedY = -9999;
        mouseReleasedX = -9999;
        mouseReleasedY = -9999;
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

    /**
     * Updates position where mouse currenty is.
     * @param x coordinate
     * @param y coordinate
     */
    public static void updateMousePosition(double x, double y) {
        mousePosX = x;
        mousePosY = y;
    }
    
    /** 
     * Updates position where mouse was pressed.
     * @param x coordinate
     * @param y coordinate
     */
    public static void updateMousePressed(double x, double y) {
        mousePressedX = x;
        mousePressedY = y;
    }
    
    /** 
     * Updates position where mouse was released.
     * @param x coordinate
     * @param y coordinate
     */
    public static void updateMouseReleased(double x, double y) {
        mouseReleasedX = x;
        mouseReleasedY = y;
    }
    
    /** 
     * Returns current mouse x coordinate.
     * @return current mouse x coordinate
     */
    public static double getMousePosX() {
        return mousePosX;
    }
    
    /** 
     * Returns current mouse y coordinate.
     * @return current mouse y coordinate
     */
    public static double getMousePosY() {
        return mousePosY;
    }

    /** 
     * Returns x coordinate where mouse was pressed.
     * @return x coordinate where mouse was pressed
     */
    public static double getMousePressedX() {
        return mousePressedX;
    }
    
    /** 
     * Returns y coordinate where mouse was pressed.
     * @return y coordinate where mouse was pressed
     */
    public static double getMousePressedY() {
        return mousePressedY;
    }
    
    /** 
     * Returns x coordinate where mouse was released.
     * @return x coordinate where mouse was released
     */
    public static double getMouseReleasedX() {
        return mouseReleasedX;
    }
    
    /** 
     * Returns y coordinate where mouse was released.
     * @return y coordinate where mouse was released
     */
    public static double getMouseReleasedY() {
        return mouseReleasedY;
    }
}