package io.github.tonimheinonen.engine.tools;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Helper class for using certain classes, withouth the need
 * to import them to the class.
 */
public interface Global {
    // Colors
    public final Color C_BLACK = Color.BLACK;
    public final Color C_GREY = Color.GREY;
    public final Color C_WHITE = Color.WHITE;
    public final Color C_RED = Color.RED;
    public final Color C_GREEN = Color.GREEN;
    public final Color C_BLUE = Color.BLUE;
    public final Color C_YELLOW = Color.YELLOW;
    public final Color C_ORANGE = Color.ORANGE;
    public final Color C_PURPLE = Color.PURPLE;
    public final Color C_CYAN = Color.CYAN;

    // Alignment
    public final TextAlignment HA_LEFT = TextAlignment.LEFT;
    public final TextAlignment HA_CENTER = TextAlignment.CENTER;
    public final TextAlignment HA_RIGHT = TextAlignment.RIGHT;
    public final VPos VA_TOP = VPos.TOP;
    public final VPos VA_CENTER = VPos.CENTER;
    public final VPos VA_BOTTOM = VPos.BOTTOM;

    /**
     * Checks if provided seconds have passed.
     * @param from starting time to check
     * @param seconds how many seconds to pass
     * @return true if provided amount of seconds has passed
     */
    public default boolean secondsPassed(long from, double seconds) {
        if (System.currentTimeMillis() >= from + seconds * 1000)
            return true;
        
        return false;
    }

    /**
     * Returns random int between provided min and max.
     * @param min minimun allowed value
     * @param max maximum allowed value
     * @return random int
     */
    public default int randomRange(int min, int max) {
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }

    /**
     * Returns random double between provided min and max.
     * @param min minimun allowed value
     * @param max maximum allowed value
     * @return random int
     */
    public default double randomRange(double min, double max) {
        return (Math.random() * ((max - min) + 1)) + min;
    }
}