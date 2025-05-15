package tetris;

import tetris.block.*;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

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
    public Bag<Mino> bagOfMinos;
    public Mino currentMino;
    private Mino nextMino;
    private Mino holdMino;

    // Signals for the game logic
    private boolean leftCollision, rightCollision, bottomCollision;
    private boolean allowSwapMino;
    private boolean isDeactivating;

    public boolean isGameOver;
    public boolean isTimesUp;

    // game counter
    private int deactivateCounter;
    private int autoDropCounter;
    private int gameCounter;
    private final int GAME_DURATION = 2 * 60 * FPS; // 2 minutes gameplay
    
    // =================================================
    // Initialize the controller
    // =================================================

    /**
     * Is called by loader.load()
     */
    public void initialize() {
        this.inactiveBlocksArray = new Block[NUM_OF_ROW][NUM_OF_COL];

        this.fillInBagOfMinos();
        this.initializeMinos();
        this.initializeSignals();
        this.initializeCounters();
    }
    private void fillInBagOfMinos() {
        this.bagOfMinos = new Bag<Mino>(new MinoI(), new MinoJ(), new MinoL(), new MinoO(),
                new MinoS(), new MinoT(), new MinoZ());
    }
    private void initializeMinos() {
        assert bagOfMinos != null;

        currentMino = bagOfMinos.pickRandomly();
        nextMino = bagOfMinos.pickRandomly();

        addMinoToPlayingField(currentMino);
        addMinoToNextMinoBox(nextMino);
        // Don't add mino to hold box
    }
    private void initializeCounters() {
        this.deactivateCounter = 0;
        this.autoDropCounter = 0;
        this.gameCounter = 0;
    }
    private void initializeSignals() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;
        isDeactivating = false;
        isGameOver = false;
        isTimesUp = false;

        allowSwapMino = true;
    }

    // =================================================
    // Add / Remove Rectangles from Pane (UI)
    // =================================================

    public void addMinoToPlayingField(Mino mino) {
        mino.setPlayingFieldStartPosition();
        mino.setShadowPosition(inactiveBlocksArray);

        // add shadow blocks before normal blocks so the normal block will be in front when they overlap
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            playingField.getChildren().add(mino.shadowBlocks[i].getRectangle());
        }
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            playingField.getChildren().add(mino.blocks[i].getRectangle());
        }
    }
    public void removeMinoFromPlayingField(Mino mino) {
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            playingField.getChildren().remove(mino.shadowBlocks[i].getRectangle());
            playingField.getChildren().remove(mino.blocks[i].getRectangle());
        }
    }
    public void removeBlockFromPlayingField(Block toBeRemovedBlock) {
        playingField.getChildren().remove(toBeRemovedBlock.getRectangle());
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

        if (currentMino.isActive() && KeyInputHandler.holdPress && allowSwapMino) {
            handleHoldPress();
        } else if (currentMino.isActive()) {
            updateWhenMinoIsActiveOnly();
        } else {
            updateWhenMinoIsInactiveOnly();
        }

        KeyInputHandler.holdPress = false;
        gameCounter = gameCounter + 1;
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

    }
    private void updateWhenMinoIsInactiveOnly() {
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            int row = currentMino.blocks[i].getRow();
            int col = currentMino.blocks[i].getCol();
            inactiveBlocksArray[row][col] = currentMino.blocks[i];
        }
        this.checkRemoveLine();
        removeMinoFromNextMinoBox(nextMino);
        currentMino = nextMino;
        nextMino = bagOfMinos.pickRandomly();
        addMinoToPlayingField(currentMino);
        addMinoToNextMinoBox(nextMino);

        this.allowSwapMino = true;

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
                currentMino.deactivate(this);
            }
            resetDeactivation();

        }
    }
    public void resetDeactivation() {
        this.deactivateCounter = 0;
        this.isDeactivating = false;
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
        currentMino.tryRotatingMino(inactiveBlocksArray, this);
        // after rotation, set the shadow position again
        currentMino.setShadowPosition(inactiveBlocksArray);
        KeyInputHandler.upPress = false;
    }
    private void handleHoldPress() {
        if (holdMino == null) {
            // update UI
            System.out.println("handling hold press");
            removeMinoFromPlayingField(currentMino);
            removeMinoFromNextMinoBox(nextMino);

            // update minos
            holdMino = currentMino;
            holdMino.setNextAndHoldBoxPosition();
            currentMino = nextMino;
            currentMino.setPlayingFieldStartPosition();
            nextMino = bagOfMinos.pickRandomly();
            nextMino.setNextAndHoldBoxPosition();

            // update UI
            addMinoToPlayingField(currentMino);
            addMinoToNextMinoBox(nextMino);
            addMinoToHoldMinoBox(holdMino);

        } else {
            System.out.println("handling hold press BUT HOLD NOT NULL");
            // update UI
            removeMinoFromPlayingField(currentMino);
            removeMinoFromHoldMinoBox(holdMino);

            // swap hold and current mino
            Mino tempMino = currentMino;
            currentMino = holdMino;
            currentMino.setPlayingFieldStartPosition();
            holdMino = tempMino;
            holdMino.setNextAndHoldBoxPosition();

            // update UI
            addMinoToPlayingField(currentMino);
            addMinoToHoldMinoBox(holdMino);
        }
        this.allowSwapMino = false; // allow one swap per "round"
        KeyInputHandler.holdPress = false;

        // Sound effect for Hold (Shift)
        //TetrisPanel.soundEffect.play(11, false); // TODO: sound effect for hold
    }
    private void handleSpacePress() {
        while(!bottomCollision) {
            currentMino.moveDown();
            this.checkCurrentMinoMovementCollision();
        }
        // deactivate immediately
        this.currentMino.deactivate(this);
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
            // after moving to the left, set the shadow position again
            currentMino.setShadowPosition(inactiveBlocksArray);
        }
        KeyInputHandler.leftPress = false;
    }
    private void handleRightPress() {
        if (!rightCollision) {
            currentMino.moveRight();
            // after moving to the right, set the shadow position again
            currentMino.setShadowPosition(inactiveBlocksArray);
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
