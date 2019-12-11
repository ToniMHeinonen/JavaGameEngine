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

    // Alignment
    public final TextAlignment HA_LEFT = TextAlignment.LEFT;
    public final TextAlignment HA_CENTER = TextAlignment.CENTER;
    public final TextAlignment HA_RIGHT = TextAlignment.RIGHT;
    public final VPos VA_TOP = VPos.TOP;
    public final VPos VA_CENTER = VPos.CENTER;
    public final VPos VA_BOTTOM = VPos.BOTTOM;
}