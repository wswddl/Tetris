package tetris.block;

import static tetris.util.TetrisConstants.BLOCK_SIZE;
import static tetris.util.TetrisConstants.MINO_START_PIXEL_X;
import static tetris.util.TetrisConstants.MINO_START_PIXEL_Y;
import static tetris.util.TetrisConstants.MINO_START_X;
import static tetris.util.TetrisConstants.MINO_START_Y;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;




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
        rectangle.setY(oldPixelX - BLOCK_SIZE);
    }
    public void moveRight() {
        this.col++;
        this.pixelX += BLOCK_SIZE;

        double oldPixelX = rectangle.getY();
        rectangle.setY(oldPixelX + BLOCK_SIZE);
    }

    public void setRectangleCoor(int pixelX, int pixelY) {
        rectangle.setX(pixelX);
        rectangle.setY(pixelY);

        this.pixelX = pixelX;
        this.pixelY = pixelY;
    }

    public void setColRow(int col, int row) {
        this.col = col;
        this.row = row;
    }


}
