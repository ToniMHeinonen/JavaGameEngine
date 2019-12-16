package io.github.tonimheinonen.gametwo;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;

public class MenuButton extends GObject {

    private Game main;
    private String text;

    public MenuButton(Game main, String text) {
        this.main = main;
        this.text = text;
    }

    @Override
    public void createEvent() {
        spriteSet(spriteCreate("images/Button.png"));
        spriteResize(1.5);
    }

    @Override
    public void stepEvent() {
        if (mouseReleased()) {
            if (text.equals("Play")) {
                main.play();
            } else if (text.equals("Highscores")) {
                main.showHighscores();
            } else if (text.equals("Exit")) {
                main.exit();
            }
        }
    }

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