package tetris.block;

import javafx.scene.paint.Color;

import static tetris.util.TetrisConstants.*;

public class MinoL extends Mino {

    public MinoL() {
        super();
        create(Color.RED);
        blocks[0].getRectangle().setX(MINO_START_PIXEL_X);
        blocks[0].getRectangle().setY(MINO_START_PIXEL_Y);
        blocks[1].getRectangle().setX(MINO_START_PIXEL_X - BLOCK_SIZE);
        blocks[1].getRectangle().setY(MINO_START_PIXEL_Y);
        blocks[2].getRectangle().setX(MINO_START_PIXEL_X + BLOCK_SIZE);
        blocks[2].getRectangle().setY(MINO_START_PIXEL_Y);
        blocks[3].getRectangle().setX(MINO_START_PIXEL_X + BLOCK_SIZE);
        blocks[3].getRectangle().setY(MINO_START_PIXEL_Y - BLOCK_SIZE);

    }

}
