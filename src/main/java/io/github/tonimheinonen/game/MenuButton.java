package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GButton;

public class MenuButton extends GButton {

    @Override
    public void createEvent() {
        setText("Menu");
    }

    @Override
    public void onPressed() {
        setText("Pressed");
    }

    @Override
    public void onReleased() {
        setText("Released");
    }
}