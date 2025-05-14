package tetris.block;

import javafx.scene.paint.Color;

import static tetris.util.TetrisConstants.*;

public abstract class Mino {
    public Block[] blocks;
    public Block[] shadowBlocks;
    public GhostBlock[] ghostBlocks;
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
        this.isActive = true;

        blocks = new Block[NUM_OF_BLOCKS_PER_MINO];
        shadowBlocks = new Block[NUM_OF_BLOCKS_PER_MINO];
        ghostBlocks = new GhostBlock[NUM_OF_BLOCKS_PER_MINO];
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

    // =================================================
    // Mino movements
    // =================================================

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

    // =================================================
    // Mino rotations
    // =================================================

    /**
     * Checks if the rotated mino will exceed boundary or overlap with other inactive blocks.
     */
    public boolean isAlternatePositionValid(Block[][] inactiveBlockArray) {
        assert inactiveBlockArray.length == NUM_OF_ROW;
        assert inactiveBlockArray[0].length == NUM_OF_COL;

        for (GhostBlock ghostBlock : ghostBlocks) {
            int row = ghostBlock.row;
            int col = ghostBlock.col;
            // exceed playing field left boundary
            if (col < LEFTMOST_X) {
                return true;
            }
            // exceed playing field right boundary
            if (col > RIGHTMOST_X) {
                return true;
            }
            // exceed playing field bottom boundary
            if (row > BOTTOMMOST_Y) {
                return true;
            }
            // overlap with inactive block
            if (inactiveBlockArray[row][col] != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tries to rotate the mino.
     *
     * <p>After ghost block has been set up, if the position of the mino overlap with other inactive blocks or exceed
     * boundary, this method will try to find an alternate position to "snap" the mino in place. If no alternate
     * position can be found, the mino won't be rotated.
     * Try snapping it to the left, right, bottom, bottom left, bottom right, top left and top right.</>
     *
     * @return true if the mino is being push upwards, else return false;
     */
    public boolean tryRotatingMino(Block[][] inactiveBlockArray) {
        int altPosition = 1;
        boolean canRotate = true;

        while(isAlternatePositionValid(inactiveBlockArray)) {
            if (altPosition == 1) { // push left
                for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
                    ghostBlocks[i].col -= 1;
                }
                altPosition++;

            } else if (altPosition == 2) { // push right
                for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
                    ghostBlocks[i].col += 1;
                    ghostBlocks[i].col += 1;

                }
                altPosition++;

            } else if (altPosition == 3) { // push down

                for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
                    ghostBlocks[i].col -= 1;
                    ghostBlocks[i].row += 1;
                }
                altPosition++;

            } else if (altPosition == 4) { // push down left

                for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
                    ghostBlocks[i].col -= 1;
                }
                altPosition++;

            } else if (altPosition == 5) { // push down right

                for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
                    ghostBlocks[i].col += 1;
                    ghostBlocks[i].col += 1;
                }
                altPosition++;

            } else if (altPosition == 6) { // push up left

                for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
                    ghostBlocks[i].row -= 1;
                    ghostBlocks[i].row -= 1;
                    ghostBlocks[i].col -= 1;
                    ghostBlocks[i].col -= 1;
                }
                altPosition++;

            } else if (altPosition == 7) { // push up right

                for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
                    ghostBlocks[i].col += 1;
                    ghostBlocks[i].col += 1;
                }
                altPosition++;

            } else {
                // Don't push up because it will cause infinite falling
                // Reset to original position
                for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
                    ghostBlocks[i].col -= 1;
                    ghostBlocks[i].row += 1;
                    // altPosition++;
                }
                canRotate = false;
                break;
            }

        }
        if (canRotate) {
            int numOfDirections = 4;
            this.direction = this.direction < numOfDirections ? this.direction + 1 : 1;

            if (altPosition == 6 || altPosition == 7) {
                return true;
            }


        } else {
            return false;
        }
        /*

        if (canRotate) {
      if (this.direction <= 3) {
        this.direction++;
      } else {
        this.direction = 1;
      }

      if (altPosition == 6 || altPosition == 7) { // push up must reset deactivating
        deactivateCounter = 0;
        isDeactivating = false;
      }
      b[0].x = tempB[0].x;
      b[0].y = tempB[0].y;
      b[1].x = tempB[1].x;
      b[1].y = tempB[1].y;
      b[2].x = tempB[2].x;
      b[2].y = tempB[2].y;
      b[3].x = tempB[3].x;
      b[3].y = tempB[3].y;
    }

        while(checkAfterRotateMinoFucked()) {
            if (altPosition == 1) { // push left
                for (int i = 0; i < tempB.length; i++) {
                    tempB[i].x -= Block.SIZE;
                }
                altPosition++;

            } else if (altPosition == 2) { // push right
                for (int i = 0; i < tempB.length; i++) {
                    tempB[i].x += Block.SIZE;
                    tempB[i].x += Block.SIZE;

                }
                altPosition++;

            } else if (altPosition == 3) { // push down

                for (int i = 0; i < tempB.length; i++) {
                    tempB[i].x -= Block.SIZE;
                    tempB[i].y += Block.SIZE;
                }
                altPosition++;

            } else if (altPosition == 4) { // push down left

                for (int i = 0; i < tempB.length; i++) {
                    tempB[i].x -= Block.SIZE;
                }
                altPosition++;

            } else if (altPosition == 5) { // push down right

                for (int i = 0; i < tempB.length; i++) {
                    tempB[i].x += Block.SIZE;
                    tempB[i].x += Block.SIZE;
                }
                altPosition++;

            } else if (altPosition == 6) { // push up left

                for (int i = 0; i < tempB.length; i++) {
                    tempB[i].y -= Block.SIZE;
                    tempB[i].y -= Block.SIZE;
                    tempB[i].x -= Block.SIZE;
                    tempB[i].x -= Block.SIZE;
                }
                altPosition++;

            } else if (altPosition == 7) { // push up right

                for (int i = 0; i < tempB.length; i++) {
                    tempB[i].x += Block.SIZE;
                    tempB[i].x += Block.SIZE;
                }
                altPosition++;

            } else { // we don't push up cuz it will be infinite falling haha

                for (int i = 0; i < tempB.length; i++) {
                    tempB[i].x -= Block.SIZE;
                    tempB[i].y += Block.SIZE;
                    // altPosition++;
                }
                canRotate = false;
                break;
            }
        */
        }

    // =================================================
    // Abstract methods
    // =================================================
    public abstract void setPlayingFieldStartPosition();
    public abstract void setNextAndHoldBoxPosition();
    /**
     * set the ghost blocks to the 1st direction configuration.
     */
    public abstract void getD1();
    /**
     * set the ghost blocks to the 2nd direction configuration.
     */
    public abstract void getD2();
    /**
     * set the ghost blocks to the 3rd direction configuration.
     */
    public abstract void getD3();
    /**
     * set the ghost blocks to the 4th direction configuration.
     */
    public abstract void getD4();

}
