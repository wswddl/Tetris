package tetris.block;

import javafx.scene.paint.Color;

import static tetris.util.TetrisConstants.*;

public class MinoL extends Mino {

    public MinoL() {
        super();
        create(Color.RED);
        blocks[0].setRectangleCoor(MINO_START_PIXEL_X, MINO_START_PIXEL_Y);
        blocks[1].setRectangleCoor(MINO_START_PIXEL_X - BLOCK_SIZE, MINO_START_PIXEL_Y);
        blocks[2].setRectangleCoor(MINO_START_PIXEL_X + BLOCK_SIZE, MINO_START_PIXEL_Y);
        blocks[3].setRectangleCoor(MINO_START_PIXEL_X + BLOCK_SIZE, MINO_START_PIXEL_Y - BLOCK_SIZE);

        blocks[0].setColRow(MINO_START_X, MINO_START_Y);
        blocks[1].setColRow(MINO_START_X - 1, MINO_START_Y);
        blocks[2].setColRow(MINO_START_X + 1, MINO_START_Y);
        blocks[3].setColRow(MINO_START_X + 1, MINO_START_Y - 1);

    }

}
