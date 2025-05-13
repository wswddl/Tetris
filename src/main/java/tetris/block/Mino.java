package tetris.block;

import javafx.scene.paint.Color;

import static tetris.util.TetrisConstants.NUM_OF_BLOCKS_PER_MINO;

public abstract class Mino {
    public Block[] blocks;
    public Block[] shadowBlocks;
    public int direction = 1;  // 1, 2, 3, 4 directions
    public boolean leftCollision, rightCollision, bottomCollision;
    private boolean isActive;
    private Color color;
    private Color shadowColor;
    private int autoDropCounter;  // move the mino down when autoDropCounter hits dropInterval

    /**
     * Is invoked by the constructor of Mino classes that inherits this class
     */
    public Mino create(Color color) {
        this.color = color;
        this.shadowColor = color;
        this.isActive  =true;

        blocks = new Block[NUM_OF_BLOCKS_PER_MINO];
        shadowBlocks = new Block[NUM_OF_BLOCKS_PER_MINO];
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            blocks[i] = new Block(color, false);

            shadowBlocks[i] = new Block(color, true);
        }
        return this;

    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }

    public void moveUp() {
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            blocks[i].moveUp();
        }
    }
    public void moveDown() {
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            blocks[i].moveDown();
        }
    }
    public void moveLeft() {
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            blocks[i].moveLeft();
        }
    }
    public void moveRight() {
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            blocks[i].moveRight();
        }
    }
}
