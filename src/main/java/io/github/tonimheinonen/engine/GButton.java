package io.github.tonimheinonen.engine;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Helper class for creating buttons. Buttons can also be created the normal
 * way, but these methods help setting up options what to do when button is
 * clicked, released etc.
 */
public abstract class GButton extends Button {
    
    private ImageView view = new ImageView();

    /**
     * Runs when button is created.
     */
    public abstract void createEvent();
    
    /**
     * Runs when button is pressed.
     */
    public abstract void onPressed();

    /**
     * Runs when button is released.
     */
    public abstract void onReleased();

    /**
     * Runs when button is dragged.
     */
    public abstract void onMouseDragged();

    /**
     * Runs when mouse enters the button.
     */
    public abstract void onMouseEntered();

    /**
     * Runs when mouse exits the button.
     */
    public abstract void onMouseExited();

    /**
     * Sets image for a button.
     * 
     * If path is null, image will be removed.
     * @param path image to draw on button
     */
    public void setSprite(String path) {
        // If image is null, remove image
        if (path == null) {
            setGraphic(null);
            return;
        }
        
        // Load image and set it to the button's view.
        Image sprite = new Image(path);
        view.setImage(sprite);
        setGraphic(view);
    }

    /**
     * Sets maximum size of the button.
     * @param width maximum width of the button
     * @param height maximum height of the button
     */
    public void setMaxSize(double width, double height) {
        super.setMaxSize(width, height);

        // Update button's view's size
        view.setFitWidth(width);
        view.setFitHeight(height);
    }
}