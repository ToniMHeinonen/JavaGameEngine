package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GButton;

public class RestartButton extends GButton {

    private Game main;

    public RestartButton(Game main) {
        this.main = main;
    }

    @Override
    public void createEvent() {

    }

    @Override
    public void onPressed() {
        main.restartGame();
    }

    @Override
    public void onReleased() {
    }

    @Override
    public void onMouseDragged() {
    }

    @Override
    public void onMouseEntered() {
    }

    @Override
    public void onMouseExited() {
    }
}