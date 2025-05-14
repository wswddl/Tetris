package tetris.block;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static tetris.util.TetrisConstants.*;


public class Block {
    private Rectangle rectangle;
    private int row;  // row on the 20x10 matrix playing field
    private int col;  // column on the 20x10 matrix playing field
    private int pixelX; // X coordinate for the rectangle position in the UI
    private int pixelY; // Y coordinate for the rectangle position in the UI
    private Color color;
    private boolean isShadowBlock;

    public Block(Color color, boolean isShadowBlock) {
        this.row = MINO_START_Y;
        this.col = MINO_START_X;
        this.pixelX = MINO_START_PIXEL_X;
        this.pixelY = MINO_START_PIXEL_Y;
        this.color = color;
        this.isShadowBlock = isShadowBlock;

        rectangle = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        rectangle.setFill(color);
        rectangle.setX(MINO_START_PIXEL_X);
        rectangle.setY(MINO_START_PIXEL_Y);
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    // Might be used in the future for auto-fitting
    public void moveUp() {
        this.row--;
        this.pixelY -= BLOCK_SIZE;

        double oldPixelY = rectangle.getY();
        rectangle.setY(oldPixelY - BLOCK_SIZE);
    }
    public void moveDown() {
        this.row++;
        this.pixelY += BLOCK_SIZE;

        double oldPixelY = rectangle.getY();
        rectangle.setY(oldPixelY + BLOCK_SIZE);
    }
    public void moveLeft() {
        this.col--;
        this.pixelX -= BLOCK_SIZE;

        double oldPixelX = rectangle.getX();
        rectangle.setX(oldPixelX - BLOCK_SIZE);
    }
    public void moveRight() {
        this.col++;
        this.pixelX += BLOCK_SIZE;

        double oldPixelX = rectangle.getX();
        rectangle.setX(oldPixelX + BLOCK_SIZE);
    }

    public void setPosition(int pixelX, int pixelY, int col, int row) {
        rectangle.setX(pixelX);
        rectangle.setY(pixelY);

        this.pixelX = pixelX;
        this.pixelY = pixelY;

        this.col = col;
        this.row = row;
    }
    public void setRectangleCoor(int pixelX, int pixelY) {
        rectangle.setX(pixelX);
        rectangle.setY(pixelY);

        this.pixelX = pixelX;
        this.pixelY = pixelY;
    }
    public void dropSmoothly(int numBlocks) {
        col += numBlocks;
        pixelY += numBlocks * BLOCK_SIZE;

        int dropPixelDistance = numBlocks * BLOCK_SIZE;

        TranslateTransition transition = new TranslateTransition(Duration.millis(BLOCK_DROPING_DURATION), rectangle);
        transition.setByY(dropPixelDistance); // Move down by dropPixelDistance pixels

        // After dropping, set the rectangle position.
        // NOTE: transition only move the rectangle virtually and doesn't change the rectangle's x,y value
        // NOTE: where the rectangle will appear on the screen is based on rectangle's X,Y + rectangle's translateX,Y
        transition.setOnFinished(e -> {
            rectangle.setY(rectangle.getY() + rectangle.getTranslateY()); // Apply the translation
            rectangle.setTranslateY(0); // Reset the translation
        });
        transition.play();
    }
    public void fade(Pane playingField) {
        FadeTransition fade = new FadeTransition(Duration.millis(BLOCK_FADING_DURATION), rectangle); // 500ms fade out
        fade.setFromValue(1.0);  // Fully visible
        fade.setToValue(0.0);    // Fully transparent

        // After fade completes, remove from playing field
        fade.setOnFinished(e -> playingField.getChildren().remove(rectangle));

        // Start the animation
        fade.play();
    }


}
