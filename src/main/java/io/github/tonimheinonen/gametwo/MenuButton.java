package io.github.tonimheinonen.gametwo;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;

public class MenuButton extends GObject {

    private Game main;
    private String text;

    /**
     * Gets main class and text for the button.
     * @param main engine class
     * @param text text to display
     */
    public MenuButton(Game main, String text) {
        this.main = main;
        this.text = text;
    }

    /**
     * Creates sprite for the button.
     */
    @Override
    public void createEvent() {
        spriteSet(spriteCreate("images/Button.png"));
        spriteResize(1.5);
    }

    /**
     * Checks button presses.
     */
    @Override
    public void stepEvent() {
        if (mouseReleased()) {
            if (text.equals("Play")) {
                main.play();
            } else if (text.equals("Highscores") ||
                        text.equals("Results")) {
                main.showHighscores();
            } else if (text.equals("Exit")) {
                main.exit();
            } else if (text.equals("Back")) {
                destroyInstanceAll();
                main.createMenu();
            }
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
        Draw.text(text, getX(), getY());
    }

}