package tetris.block;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static tetris.Tetris.BLOCK_SIZE;

public class ShadowBlock extends Block {
    private Color shadowColor;
    private int margin = 2;

    public ShadowBlock(Color shadowColor) {
        if (shadowColor.equals(Color.BLUE)) {
            this.shadowColor = Color.rgb(0, 100, 255);
        } else {
            this.shadowColor = shadowColor;
        }
    }

    public void drawAdd(GraphicsContext gc) {
        /*gc.setLineWidth(1.0); // Equivalent to BasicStroke(1f)
        gc.setStroke(this.shadowColor);
        gc.strokeRect(
                this.pixelX + margin,
                this.pixelY + margin,
                BLOCK_SIZE - margin * 2,
                BLOCK_SIZE - margin * 2
        );*/
        gc.setFill(Color.GREY);
        gc.fillRect(pixelX + 2, pixelY + 2, BLOCK_SIZE - 4, BLOCK_SIZE - 4);
    }

}
