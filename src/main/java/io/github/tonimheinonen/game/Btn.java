package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GObject;
import io.github.tonimheinonen.engine.tools.*;

public class Btn extends GObject {

    @Override
    public void createEvent() {
        spriteSet(spriteCreate("images/Button.png"));
    }

    @Override
    public void stepEvent() {

    }

    @Override
    public void drawEvent() {
        Draw.setColor(C_BLACK);
        Draw.setHorizontalAlign(HA_CENTER);
        Draw.setVerticalAlign(VA_CENTER);
        Draw.setTextFont("Verdana");
        Draw.setTextSize(20);
        Draw.text("Restart", getX(), getY());
    }

}