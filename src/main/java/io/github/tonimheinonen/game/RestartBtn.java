package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;

public class RestartBtn extends GObject {

    private Game main;

    /**
     * Gets main class reference.
     * @param main engine class
     */
    public RestartBtn(Game main) {
        this.main = main;
    }

    /**
     * Creates sprite for the button.
     */
    @Override
    public void createEvent() {
        spriteSet(spriteCreate("images/Button.png"));
    }

    /**
     * Checks button presses.
     */
    @Override
    public void stepEvent() {
        if (mouseReleased()) {
            destroyInstance(this);
            main.restartGame();
        }
    }

    /**
     * Draws button.
     */
    @Override
    public void drawEvent() {
        if (!mouseOver())
            Draw.setColor(C_BLACK);
        else
            Draw.setColor(C_ORANGE);

        Draw.setHorizontalAlign(HA_CENTER);
        Draw.setVerticalAlign(VA_CENTER);
        Draw.setTextFont("Verdana");
        Draw.setTextSize(20);
        drawSelf();
        Draw.text("Restart", getX(), getY());
    }

}