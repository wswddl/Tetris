package tetris.block;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import tetris.util.GameSignal;

import static tetris.util.TetrisConstants.*;


public class Block {
    private Rectangle rectangle;
    protected int row;  // row on the 20x10 matrix playing field
    protected int col;  // column on the 20x10 matrix playing field
    private int prevPixelX;
    private int prevPixelY;
    public int pixelX; // X coordinate for the rectangle position in the UI
    public int pixelY; // Y coordinate for the rectangle position in the UI

    public Block() {
        /*
        this.row = MINO_START_Y;
        this.col = MINO_START_X;
        prevPixelX =
        this.pixelX = MINO_START_PIXEL_X;
        this.pixelY = MINO_START_PIXEL_Y;
        this.isPrevCoordinateImmutable = false;*/

        rectangle = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        rectangle.setFill(Color.TRANSPARENT);
        //rectangle.setX(MINO_START_PIXEL_X);
        //rectangle.setY(MINO_START_PIXEL_Y);
/*
        this.isShadowBlock = isShadowBlock;
        if (isShadowBlock) {
            //rectangle.setFill(Color.GRAY);
            rectangle.setFill(Color.TRANSPARENT);
            this.color = Color.GREY;
        } else {
            //rectangle.setFill(color);
            rectangle.setFill(Color.TRANSPARENT);
            this.color = color;
        }*/
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
        this.prevPixelY = this.pixelY;
        this.pixelY -= BLOCK_SIZE;

        double oldPixelY = rectangle.getY();
        rectangle.setY(oldPixelY - BLOCK_SIZE);
    }
    public void moveDown() {
        this.row++;
        this.prevPixelY = this.pixelY;
        this.pixelY += BLOCK_SIZE;

        double oldPixelY = rectangle.getY();
        rectangle.setY(oldPixelY + BLOCK_SIZE);
    }
    public void moveLeft() {
        this.col--;
        this.prevPixelX = this.pixelX;
        this.pixelX -= BLOCK_SIZE;

        double oldPixelX = rectangle.getX();
        rectangle.setX(oldPixelX - BLOCK_SIZE);
    }
    public void moveRight() {
        this.col++;
        this.prevPixelX = this.pixelX;
        this.pixelX += BLOCK_SIZE;

        double oldPixelX = rectangle.getX();
        rectangle.setX(oldPixelX + BLOCK_SIZE);
    }

    public void setPosition(int pixelX, int pixelY, int col, int row) {
        rectangle.setX(pixelX);
        rectangle.setY(pixelY);

        this.prevPixelX = pixelX;
        this.prevPixelY = pixelY;
        this.pixelX = pixelX;
        this.pixelY = pixelY;

        this.col = col;
        this.row = row;
    }
    public void setRectangleCoor(int pixelX, int pixelY) {
        rectangle.setX(pixelX);
        rectangle.setY(pixelY);

        this.prevPixelX = this.pixelY;
        this.prevPixelY = this.pixelY;
        this.pixelX = pixelX;
        this.pixelY = pixelY;
    }
    /**
     * Copy the {@code row}, {@code col}, {@code pixelX}, {@code pixelY} and rectangle's position.
     */
    public void copyPosition(Block toBeCopyBlock) {
        this.row = toBeCopyBlock.row;
        this.col = toBeCopyBlock.col;
        this.prevPixelX = toBeCopyBlock.prevPixelX;
        this.prevPixelY = toBeCopyBlock.prevPixelY;
        this.pixelX = toBeCopyBlock.pixelX;
        this.pixelY = toBeCopyBlock.pixelY;
        this.rectangle.setX(toBeCopyBlock.rectangle.getX());
        this.rectangle.setY(toBeCopyBlock.rectangle.getY());
    }
    public void dropImmediately(int numLineDrop) {
        row += numLineDrop;
    }
    public void dropSmoothly(int numLineDrop, int effectCounter) {

    }
    /*
    public void dropSmoothly(int numBlocks) {
        col += numBlocks;
        pixelY += numBlocks * BLOCK_SIZE;

        int dropPixelDistance = numBlocks * BLOCK_SIZE;

        TranslateTransition transition = new TranslateTransition(Duration.millis(BLOCK_DROPPING_DURATION), rectangle);
        transition.setByY(dropPixelDistance); // Move down by dropPixelDistance pixels

        // After dropping, set the rectangle position.
        // NOTE: transition only move the rectangle virtually and doesn't change the rectangle's x,y value
        // NOTE: where the rectangle will appear on the screen is based on rectangle's X,Y + rectangle's translateX,Y
        transition.setOnFinished(e -> {
            rectangle.setY(rectangle.getY() + rectangle.getTranslateY()); // Apply the translation
            rectangle.setTranslateY(0); // Reset the translation
        });
        transition.play();
    }*/
    /*
    public void fade(Pane playingField) {
        FadeTransition fade = new FadeTransition(Duration.millis(BLOCK_FADING_DURATION), rectangle); // 500ms fade out
        fade.setFromValue(1.0);  // Fully visible
        fade.setToValue(0.0);    // Fully transparent

        // After fade completes, remove from playing field
        fade.setOnFinished(e -> playingField.getChildren().remove(rectangle));

        // Start the animation
        fade.play();
    }*/



    public void drawAdd(GraphicsContext gc) {

        /*
        gc.setFill(color);
        gc.fillRect(pixelX, pixelY, 30, 30);*/
    }

    public void drawRemove(GraphicsContext gc) {
        gc.clearRect(pixelX, pixelY, BLOCK_SIZE, BLOCK_SIZE);
    }

}
