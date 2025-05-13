package tetris.block;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;

import static tetris.TetrisConstants.*;

public class Block {
    private Rectangle rectangle;
    private int row;  // row on the 20x10 matrix playing field
    private int col;  // column on the 20x10 matrix playing field
    private int pixelX; // X coordinate for the rectangle position in the UI
    private int pixelY; // Y coordinate for the rectangle position in the UI
    private Color color;
    private boolean isShadowBlock;

    public Block(Color color, boolean isShadowBlock) {
        this.row = MINO_START_X;
        this.col = MINO_START_Y;
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
        rectangle.setY(oldPixelX - BLOCK_SIZE);
    }
    public void moveRight() {
        this.col++;
        this.pixelX += BLOCK_SIZE;

        double oldPixelX = rectangle.getY();
        rectangle.setY(oldPixelX + BLOCK_SIZE);
    }

}
