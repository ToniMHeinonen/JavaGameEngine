package io.github.tonimheinonen.game;

import io.github.tonimheinonen.engine.GButton;

public class MenuButton extends GButton {

    @Override
    public void onPressed() {
        System.out.println("Press");

    }

    @Override
    public void onReleased() {
        System.out.println("Release");
    }
    
}