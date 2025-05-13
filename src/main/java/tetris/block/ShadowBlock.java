package tetris.block;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ShadowBlock {
    private Rectangle rectangle;
    private int row;  // row on the 20x10 matrix playing field
    private int col;  // column on the 20x10 matrix playing field
    private int pixelX; // X coordinate for the rectangle position in the UI
    private int pixelY; // Y coordinate for the rectangle position in the UI
    private Color shadowColor;
}
