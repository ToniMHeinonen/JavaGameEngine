package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;

public class RestartBtn extends GObject {

    private Game main;

    public RestartBtn(Game main) {
        this.main = main;
    }

    @Override
    public void createEvent() {
        spriteSet(spriteCreate("images/Button.png"));
    }

    @Override
    public void stepEvent() {
        if (mouseReleased()) {
            destroyInstance(this);
            main.restartGame();
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
        Draw.text("Restart", getX(), getY());
    }

}