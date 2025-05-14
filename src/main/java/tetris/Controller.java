package tetris;

import tetris.block.Block;
import tetris.block.Mino;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import tetris.block.MinoL;

import java.security.Key;
import java.util.ArrayList;

import static tetris.util.TetrisConstants.*;

public class Controller {

    @FXML
    private Pane holdMinoBox;
    @FXML
    private Pane playingField;
    @FXML
    private Pane nextMinoBox;

    private Block[][] inactiveBlocksArray; // only keep track of inactive blocks, not including active blocks
    public Mino currentMino;
    private Mino nextMino;
    private Mino holdMino;

    // Signals for the game logic
    private boolean leftCollision, rightCollision, bottomCollision;
    private boolean allowSwapMino;
    private boolean isDeactivating;
    private int deactivateCounter;
    public boolean isGameOver;
    public boolean isTimesUp;

    // game counter
    private int autoDropCounter;
    private int gameCounter;
    private final int GAME_DURATION = 2 * 60 * FPS; // 2 minutes gameplay
    
    // =================================================
    // Initialze the controller
    // =================================================

    /**
     * Is called by loader.load()
     */
    public void initialize() {
        this.inactiveBlocksArray = new Block[NUM_OF_ROW][NUM_OF_COL];

        this.currentMino = new MinoL(); // TODO: implement pcikMino
        this.nextMino = new MinoL(); // TODO: implement pcikMino

        this.autoDropCounter = 0;
        this.gameCounter = 0;

        addMinoToPlayingField(currentMino);
        addMinoToNextMinoBox(nextMino);
        addMinoToHoldMinoBox(new MinoL());
    }

    // =================================================
    // Add / Remove Rectangles from Pane (UI)
    // =================================================

    public void addMinoToPlayingField(Mino mino) {
        mino.setPlayingFieldStartPosition();
        for (Block block : mino.blocks) {
            playingField.getChildren().add(block.getRectangle());
        }
    }
    public void removeMinoFromPlayingField(Mino mino) {
        for (Block block : mino.blocks) {
            playingField.getChildren().remove(block.getRectangle());
        }
    }
    public void addMinoToNextMinoBox(Mino mino) {
        mino.setNextAndHoldBoxPosition();
        for (Block block : mino.blocks) {
            nextMinoBox.getChildren().add(block.getRectangle());
        }
    }
    public void removeMinoFromNextMinoBox(Mino mino) {
        for (Block block : mino.blocks) {
            nextMinoBox.getChildren().remove(block.getRectangle());
        }
    }
    public void addMinoToHoldMinoBox(Mino mino) {
        mino.setNextAndHoldBoxPosition();
        for (Block block : mino.blocks) {
            holdMinoBox.getChildren().add(block.getRectangle());
        }
    }
    public void removeMinoFromHoldMinoBox(Mino mino) {
        for (Block block : mino.blocks) {
            holdMinoBox.getChildren().remove(block.getRectangle());
        }
    }
    
    // =================================================
    // Update the game
    // =================================================
    
    /**
     * Gets invoke by {@Code Tetris} gameLoop every time interval.
     * Updates the Minos and other UI components every time interval.
     */
    public void update() {

        if (currentMino.isActive()) {
            updateWhenMinoIsActiveOnly();
        }

        if (!currentMino.isActive()) {
            updateWhenMinoIsInactiveOnly();
        }


        gameCounter++;
    }
    
    public void updateWhenMinoIsActiveOnly() {
        if (isDeactivating) {
            this.startDeactivatingCurrentMino();
        }

        // keyboard input handler...
        if (KeyInputHandler.upPress) {
            handleUpPress();
        }

        this.checkCurrentMinoMovementCollision();

        if (KeyInputHandler.spacePress) {
            handleSpacePress();
        }

        if (KeyInputHandler.downPress) {
            handleDownPress();
        }
        if (KeyInputHandler.leftPress) {
            handleLeftPress();
        }
        if (KeyInputHandler.rightPress) {
            handleRightPress();
        }

        // auto drop
        this.autoDropMechanism();

        // TODO: shadow
    }

    public void updateWhenMinoIsInactiveOnly() {
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            int row = currentMino.blocks[i].getRow();
            int col = currentMino.blocks[i].getCol();
            inactiveBlocksArray[row][col] = currentMino.blocks[i];
        }
        this.checkRemoveLine();
        removeMinoFromNextMinoBox(nextMino);
        currentMino = nextMino;
        addMinoToPlayingField(currentMino);
        nextMino = new MinoL(); // TODO: pickmino
        addMinoToNextMinoBox(nextMino);

        // TODO: combo and soundeffect
    }

    // =================================================
    // Mino update mechanism
    // =================================================

    /**
     * Deactivates the mino when {@code DEACTIVATE_INTERVAL} is reached and {@code bottomCollision} is still true.
     * This method is invoked by {@code update()} when {@code isDeactivating} is true.
     */
    public void startDeactivatingCurrentMino() {
        deactivateCounter++;
        if (deactivateCounter == DEACTIVATE_INTERVAL) {
            // check if the mino is still hitting the bottom after 1 second
            // if still hitting the bottom, deactivate it
            // reset the deactivate counter and boolean
            this.checkCurrentMinoMovementCollision();
            if (bottomCollision) {
                currentMino.deactivate();
            }
            deactivateCounter = 0;
            isDeactivating = false;

        }
    }
    /**
     * Moves the mino down one block every {@Code AUTO_DROP_INTERVAL}.
     */
    private void autoDropMechanism() {
        if (bottomCollision) {
            this.isDeactivating = true;
        } else {
            this.autoDropCounter++;
            if (autoDropCounter == AUTO_DROP_INTERVAL) {
                currentMino.moveDown();
                autoDropCounter = 0;
            }
        }
    }
    private void handleUpPress() {
        // rotate
        switch(currentMino.direction) {
            case 1: currentMino.getD2(); break;
            case 2: currentMino.getD3(); break;
            case 3: currentMino.getD4(); break;
            case 4: currentMino.getD1(); break;
        }
        boolean isResetDeactivation = currentMino.tryRotatingMino(inactiveBlocksArray);
        if (isResetDeactivation) {
            deactivateCounter = 0;
            isDeactivating = false;
        }
        KeyInputHandler.upPress = false;
    }
    private void handleSpacePress() {
        while(!bottomCollision) {
            currentMino.moveDown();
            this.checkCurrentMinoMovementCollision();
        }
        // deactivate immediately
        this.currentMino.deactivate();
        KeyInputHandler.spacePress = false;

        // Sound effect for Space
        //TetrisPanel.soundEffect.play(12, false); TODO: sound effect for space
    }
    private void handleDownPress() {
        if (!bottomCollision) {
            currentMino.moveDown();
            autoDropCounter = 0;
        }
        KeyInputHandler.downPress = false;
    }
    private void handleLeftPress() {
        if (!leftCollision) {
            currentMino.moveLeft();
        }
        KeyInputHandler.leftPress = false;
    }
    private void handleRightPress() {
        if (!rightCollision) {
            currentMino.moveRight();
        }
        KeyInputHandler.rightPress = false;
    }

    
    // =================================================
    // Removing lines and effects
    // =================================================

    /**
     * Starts from the lowest row to the top, if there is a full row (10 blocks in a row), remove it from the
     * playing field, else drop the inactive blocks on top to fill in the gap of the deleted row.
     */
    private void checkRemoveLine() {
        int numLineClear = 0;
        for (int r = NUM_OF_ROW - 1; r >= 0; r--) {
            int numBlocksInARow = 0;
            for (int c = 0; c < NUM_OF_COL; c++) {
                if (inactiveBlocksArray[r][c] != null) {
                    numBlocksInARow++;
                }
            }
            // remove line
            if (numBlocksInARow == NUM_OF_COL) {
                numLineClear++;
                for (int c = 0; c < inactiveBlocksArray[0].length; c++) {
                    Block toBeRemovedBlock = inactiveBlocksArray[r][c];
                    if (toBeRemovedBlock != null) {
                        toBeRemovedBlock.fade(playingField); // fade the block out and remove it from playing field
                        inactiveBlocksArray[r][c] = null;
                    }
                }
            } else {
                for (int c = 0; c < inactiveBlocksArray[0].length; c++) {
                    Block toBeDropBlock = inactiveBlocksArray[r][c];
                    if (toBeDropBlock != null) {
                        toBeDropBlock.dropSmoothly(numLineClear); // drop the block numLineClear number of rows
                        inactiveBlocksArray[r][c] = null;
                        inactiveBlocksArray[r + numLineClear][c] = toBeDropBlock;
                    }
                }
            }
        }
    }
    
    // =================================================
    // Collision detection
    // =================================================
    
    /**
     * Check if the mino collides with the border or any inactive blocks.
     * <p>A collision means that the mino is "in contact" with the border/inactive blocks. </>
     */
    public void checkCurrentMinoMovementCollision() {
        leftCollision = rightCollision = bottomCollision = false;
        for (Block block : currentMino.blocks) {
            // check if the mino collides with the border
            isBorderCollision(block);
            // check if the mino collides with inactive blocks
            isLeftBlocked(block);
            isBottomBlocked(block);
            isRightBlocked(block);
        }
    }
    public void isBorderCollision(Block block) {
        assert block != null;

        int row = block.getRow();
        int col = block.getCol();
        if (col <= LEFTMOST_X) {
            leftCollision = true;
        }
        if (col >= RIGHTMOST_X) {
            rightCollision = true;
        }
        if (row >= BOTTOMMOST_Y) {
            bottomCollision = true;
        }
    }
    /**
     * Checks if the left side of the block is an inactive block.
     */
    public void isLeftBlocked(Block block) {
        assert block != null;

        int row = block.getRow();
        int col = block.getCol();
        if (col <= LEFTMOST_X || inactiveBlocksArray[row][col - 1] != null) {
            leftCollision = true;
        }
    }
    /**
     * Checks if the bottom of the block is an inactive block.
     */
    public void isBottomBlocked(Block block) {
        assert block != null;

        int row = block.getRow();
        int col = block.getCol();
        if (row >= BOTTOMMOST_Y || inactiveBlocksArray[row + 1][col] != null) {
            bottomCollision = true;
        }
    }
    /**
     * Checks if the right side of the block is an inactive block.
     */
    public void isRightBlocked(Block block) {
        assert block != null;

        int row = block.getRow();
        int col = block.getCol();
        if (col >= RIGHTMOST_X || inactiveBlocksArray[row][col + 1] != null) {
            rightCollision = true;
        }
    }
    

    

    




}
