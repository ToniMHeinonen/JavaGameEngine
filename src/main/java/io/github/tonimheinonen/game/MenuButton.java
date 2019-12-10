package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GButton;

public class MenuButton extends GButton {

    @Override
    public void createEvent() {
        setText("Menu");
        setSprite("images/Coin.png");
        setMaxSize(50, 25);
    }

    @Override
    public void onPressed() {
        System.out.println("Pressed");
    }

    @Override
    public void onReleased() {
        setText("Released");
    }

    @Override
    public void onMouseDragged() {
        System.out.println("dragging");
    }

    @Override
    public void onMouseEntered() {
        System.out.println("entered");
    }

    @Override
    public void onMouseExited() {
        System.out.println("exited");
    }
}