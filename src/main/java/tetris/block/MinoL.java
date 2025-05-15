package tetris.block;

import javafx.scene.paint.Color;

import static tetris.util.TetrisConstants.*;

public class MinoL extends Mino {
    public static final int MINO_L_NEXT_PIXEL_X = NEXT_BOX_HEIGHT_WIDTH / 2;
    public static final int MINO_L_NEXT_PIXEL_Y = NEXT_BOX_HEIGHT_WIDTH / 2;
    public MinoL() {
        super();
        create(Color.ORANGE);
    }
    @Override
    public void setPlayingFieldStartPosition() {
        blocks[0].setPosition(MINO_START_PIXEL_X, MINO_START_PIXEL_Y,
                MINO_START_X, MINO_START_Y);
        blocks[1].setPosition(MINO_START_PIXEL_X - BLOCK_SIZE, MINO_START_PIXEL_Y,
                MINO_START_X - 1, MINO_START_Y);
        blocks[2].setPosition(MINO_START_PIXEL_X + BLOCK_SIZE, MINO_START_PIXEL_Y,
                MINO_START_X + 1, MINO_START_Y);
        blocks[3].setPosition(MINO_START_PIXEL_X + BLOCK_SIZE, MINO_START_PIXEL_Y - BLOCK_SIZE,
                MINO_START_X + 1, MINO_START_Y - 1);
    }
    @Override
    public void setNextAndHoldBoxPosition() {
        int deltaX = -14;
        int deltaY = 0;
        blocks[0].setRectangleCoor(MINO_L_NEXT_PIXEL_X + deltaX,
                MINO_L_NEXT_PIXEL_Y + deltaY);
        blocks[1].setRectangleCoor(MINO_L_NEXT_PIXEL_X - BLOCK_SIZE + deltaX,
                MINO_L_NEXT_PIXEL_Y + deltaY);
        blocks[2].setRectangleCoor(MINO_L_NEXT_PIXEL_X + BLOCK_SIZE + deltaX,
                MINO_L_NEXT_PIXEL_Y + deltaY);
        blocks[3].setRectangleCoor(MINO_L_NEXT_PIXEL_X + BLOCK_SIZE + deltaX,
                MINO_L_NEXT_PIXEL_Y - BLOCK_SIZE + deltaY);

        // Don't need to set the row & col
    }
    @Override
    public void getD1() {
        ghostBlocks[0].setColRow(blocks[0].getCol(), blocks[0].getRow());
        ghostBlocks[1].setColRow(blocks[0].getCol() - 1, blocks[0].getRow());
        ghostBlocks[2].setColRow(blocks[0].getCol() + 1, blocks[0].getRow());
        ghostBlocks[3].setColRow(blocks[0].getCol() + 1, blocks[0].getRow() - 1);
    }
    @Override // Rotate
    public void getD2() {
        ghostBlocks[0].setColRow(blocks[0].getCol(), blocks[0].getRow());
        ghostBlocks[1].setColRow(blocks[0].getCol(), blocks[0].getRow() - 1);
        ghostBlocks[2].setColRow(blocks[0].getCol(), blocks[0].getRow() + 1);
        ghostBlocks[3].setColRow(blocks[0].getCol() + 1, blocks[0].getRow() + 1);
    }
    @Override // Rotate
    public void getD3() {
        ghostBlocks[0].setColRow(blocks[0].getCol(), blocks[0].getRow());
        ghostBlocks[1].setColRow(blocks[0].getCol() + 1, blocks[0].getRow());
        ghostBlocks[2].setColRow(blocks[0].getCol() - 1, blocks[0].getRow());
        ghostBlocks[3].setColRow(blocks[0].getCol() - 1, blocks[0].getRow() + 1);
    }
    @Override // Rotate
    public void getD4() {
        ghostBlocks[0].setColRow(blocks[0].getCol(), blocks[0].getRow());
        ghostBlocks[1].setColRow(blocks[0].getCol(), blocks[0].getRow() + 1);
        ghostBlocks[2].setColRow(blocks[0].getCol(), blocks[0].getRow() - 1);
        ghostBlocks[3].setColRow(blocks[0].getCol() - 1, blocks[0].getRow() - 1);
    }
    @Override
    public MinoL copy() {
        return new MinoL();
    }
}
