package fi.tuni.game;

import fi.tuni.engine.*;

public class Player extends GObject {

    @Override
    public void createEvent() {
        spriteCreate("player.png");
        spriteResize(32, 32);
    }

    @Override
    public void stepEvent() {

    }

    @Override
    public void drawEvent() {
        spriteDraw();
    }
    
}