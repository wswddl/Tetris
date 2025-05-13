package tetris.util;

public class TetrisConstants {

    public static final int BLOCK_SIZE = 30;
    public static final int NUM_OF_BLOCKS_PER_MINO = 4;
    public static final int NUM_OF_ROW = 20;
    public static final int PLAYING_FIELD_HEIGHT = 20 * BLOCK_SIZE;
    public static final int NUM_OF_COL = 10;
    public static final int PLAYING_FIELD_WIDTH = 10 * BLOCK_SIZE;
    public static final int MINO_START_PIXEL_X = PLAYING_FIELD_WIDTH / 2 - BLOCK_SIZE;
    public static final int MINO_START_PIXEL_Y = BLOCK_SIZE;
    public static final int MINO_START_X = NUM_OF_COL / 2 - 1;  // start from the fifth column
    public static final int MINO_START_Y = 1;  // start from the second row
    public static final int FPS = 120;
    public static final int AUTO_DROP_INTERVAL = 120 * 3 / 4;
}
