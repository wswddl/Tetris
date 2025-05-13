package tetris.util;

public class TetrisConstants {

    public static final int BLOCK_SIZE = 30;
    public static final int NUM_OF_BLOCKS_PER_MINO = 4;
    public static final int NUM_OF_ROW = 20;
    public static final int PLAYING_FIELD_HEIGHT = NUM_OF_ROW * BLOCK_SIZE;
    public static final int NUM_OF_COL = 10;
    public static final int PLAYING_FIELD_WIDTH = NUM_OF_COL * BLOCK_SIZE;
    public static final int LEFTMOST_PIXEL = 0;
    public static final int LEFTMOST_X = 0;
    public static final int RIGHTMOST_PIXEL = NUM_OF_COL * BLOCK_SIZE;
    public static final int RIGHTMOST_X = NUM_OF_COL - 1;
    public static final int TOPMOST_Y = 0;
    public static final int BOTTOMMOST_Y = NUM_OF_ROW - 1;
    public static final int MINO_START_PIXEL_X = PLAYING_FIELD_WIDTH / 2 - BLOCK_SIZE;
    public static final int MINO_START_PIXEL_Y = BLOCK_SIZE;
    public static final int MINO_START_X = NUM_OF_COL / 2 - 1;  // start from the fifth column
    public static final int MINO_START_Y = 1;  // start from the second row
    public static final int FPS = 120;
    public static final int AUTO_DROP_INTERVAL = 30 * 3 / 4;
}
