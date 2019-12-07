package io.github.tonimheinonen.engine;

import javafx.scene.control.Button;

public abstract class GButton extends Button {
    public abstract void onPressed();

    public abstract void onReleased();
}