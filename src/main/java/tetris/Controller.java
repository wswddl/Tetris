package tetris;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import tetris.block.Block;
import tetris.block.Mino;
import tetris.block.MinoBlock;
import tetris.block.MinoI;
import tetris.block.MinoJ;
import tetris.block.MinoL;
import tetris.block.MinoO;
import tetris.block.MinoS;
import tetris.block.MinoT;
import tetris.block.MinoZ;
import tetris.ui.UiManager;

import static tetris.util.TetrisConstants.*;

public class Controller {

    // ui
    @FXML
    private Pane holdMinoBox;
    @FXML
    private Pane playingField;
    @FXML
    private Pane nextMinoBox;
    private UiManager ui;

    // minos
    private MinoBlock[][] inactiveBlocksArray; // only keep track of inactive blocks, not including active blocks
    public Bag<Mino> bagOfMinos;
    public Mino currentMino;
    private Mino nextMino;
    private Mino holdMino;

    // Signals for the game logic
    private boolean leftCollision, rightCollision, bottomCollision;
    private boolean allowSwapMino;
    private boolean isDeactivating;
    private boolean isEffectOn; // effect is on when there is line removal

    public boolean isGameOver;
    public boolean isTimesUp;

    // game counter
    private int deactivateCounter;
    private int autoDropCounter;
    private int gameCounter;
    private int effectCounter;
    private final int GAME_DURATION = 2 * 60 * FPS; // 2 minutes gameplay
    
    // =================================================
    // Initialize the controller
    // =================================================

    /**
     * Is called by loader.load()
     */
    public void initialize() {
        ui = new UiManager(playingField, nextMinoBox, holdMinoBox);
        ui.drawPlayingFieldGrid();

        this.inactiveBlocksArray = new MinoBlock[NUM_OF_ROW][NUM_OF_COL];

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
        this.effectCounter = 0;
    }
    private void initializeSignals() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;
        isDeactivating = false;
        isEffectOn = false;
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
        ui.addMinoShadowInPlayingField(mino);
        ui.addMinoInPlayingField(mino);
    }
    public void removeMinoFromPlayingField(Mino mino) {
        ui.removeMinoInPlayingField(mino);
        ui.removeMinoShadowInPlayingField(mino);
    }
    public void addMinoToNextMinoBox(Mino mino) {
        mino.setNextAndHoldBoxPosition();
        ui.addMinoInNextBox(mino);
    }
    public void removeMinoFromNextMinoBox(Mino mino) {
        ui.removeMinoInNextBox(mino);
    }
    public void addMinoToHoldMinoBox(Mino mino) {
        mino.setNextAndHoldBoxPosition();
        ui.addMinoInHoldBox(mino);
    }
    public void removeMinoFromHoldMinoBox(Mino mino) {
        ui.removeMinoInHoldBox(mino);
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
        } else if (currentMino.isActive()){
            if (!isEffectOn) {
                updateWhenMinoIsActiveOnly();
            } else {
                // current mino is active (next round) but the effect is still going on
                // don't proceed the game (by not calling updateWhenMinoIsActiveOnly()) until the effect is over
                //System.out.println("HANDLING SPECIAL EFFECT");
                handleClearLineSpecialEffect();
            }
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

        // Clear UI
        removeMinoFromNextMinoBox(nextMino);

        currentMino = nextMino;
        nextMino = bagOfMinos.pickRandomly();

        // Set UI
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
                currentMino.deactivate(playingField);
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
                // Clear UI
                ui.removeMinoInPlayingField(currentMino);

                currentMino.moveDown();

                // Set UI
                ui.addMinoInPlayingField(currentMino);

                autoDropCounter = 0;
            }
        }
    }
    private void handleUpPress() {
        // Clear UI
        ui.removeMinoInPlayingField(currentMino);
        ui.removeMinoShadowInPlayingField(currentMino);

        // set the ghost block position (rotation)
        switch(currentMino.direction) {
            case 1: currentMino.getD2(); break;
            case 2: currentMino.getD3(); break;
            case 3: currentMino.getD4(); break;
            case 4: currentMino.getD1(); break;
        }
        // try to rotate the mino and snap to valid position
        currentMino.tryRotatingMino(inactiveBlocksArray, this);
        // after rotation, set the shadow position again
        currentMino.setShadowPosition(inactiveBlocksArray);

        // Set UI
        // Note: the order of adding mino shadow and mino doesn't matter since different Canvas object is used.
        ui.addMinoShadowInPlayingField(currentMino);
        ui.addMinoInPlayingField(currentMino);


        KeyInputHandler.upPress = false;
    }
    private void handleHoldPress() {
        if (holdMino == null) {
            // clear UI
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
            // clear UI
            removeMinoFromPlayingField(currentMino);
            ui.removeMinoInHoldBox(holdMino);

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

        // Clear UI
        ui.removeMinoInPlayingField(currentMino); // Note: don't need to remove shadow in UI as it doesn't change position
        ui.removeMinoShadowInPlayingField(currentMino);

        while(!bottomCollision) {
            currentMino.moveDown();
            this.checkCurrentMinoMovementCollision();
        }
        // deactivate immediately
        this.currentMino.deactivate(playingField);

        // Set UI
        ui.addMinoInPlayingField(currentMino);

        autoDropCounter = 0;
        KeyInputHandler.spacePress = false;

        // Sound effect for Space
        //TetrisPanel.soundEffect.play(12, false); TODO: sound effect for space
    }
    private void handleDownPress() {
        // Clear UI
        ui.removeMinoInPlayingField(currentMino); // Note: just remove the shadow and add it back, lag may cause the shadow to not be removed
        ui.removeMinoShadowInPlayingField(currentMino);

        if (!bottomCollision) {
            currentMino.moveDown();
            autoDropCounter = 0;
        }

        // Set UI
        ui.addMinoInPlayingField(currentMino);
        ui.addMinoShadowInPlayingField(currentMino);

        KeyInputHandler.downPress = false;
    }
    private void handleLeftPress() {
        if (!leftCollision) {
            // Clear UI
            ui.removeMinoInPlayingField(currentMino);
            ui.removeMinoShadowInPlayingField(currentMino);

            currentMino.moveLeft();
            // after moving to the left, set the shadow position again
            currentMino.setShadowPosition(inactiveBlocksArray);

            // Set UI
            ui.addMinoInPlayingField(currentMino);
            ui.addMinoShadowInPlayingField(currentMino);

        }
        KeyInputHandler.leftPress = false;
    }
    private void handleRightPress() {
        if (!rightCollision) {
            // Clear UI
            ui.removeMinoInPlayingField(currentMino);
            ui.removeMinoShadowInPlayingField(currentMino);

            currentMino.moveRight();
            // after moving to the right, set the shadow position again
            currentMino.setShadowPosition(inactiveBlocksArray);

            // Set UI
            ui.addMinoInPlayingField(currentMino);
            ui.addMinoShadowInPlayingField(currentMino);
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
        int numLinesClear = 0;
        boolean gotLineRemoval = false;
        for (int r = NUM_OF_ROW - 1; r >= 0; r--) {
            int numBlocksInARow = 0;
            for (int c = 0; c < NUM_OF_COL; c++) {
                if (inactiveBlocksArray[r][c] != null) {
                    numBlocksInARow++;
                }
            }
            // remove line
            if (numBlocksInARow == NUM_OF_COL) {
                gotLineRemoval = true;
                this.isEffectOn = true;
                numLinesClear++;
                for (int c = 0; c < inactiveBlocksArray[0].length; c++) {
                    MinoBlock toBeRemovedBlock = inactiveBlocksArray[r][c];
                    if (toBeRemovedBlock != null) {
                        //ui.removeBlock(toBeRemovedBlock);
                        ui.addFadingBlock(toBeRemovedBlock);
                        inactiveBlocksArray[r][c] = null;
                    }
                }
            } else {
                for (int c = 0; c < inactiveBlocksArray[0].length; c++) {
                    MinoBlock fallingBlock = inactiveBlocksArray[r][c];
                    if (fallingBlock != null) {
                        //toBeDropBlock.dropSmoothly(numLineClear); // drop the block numLineClear number of rows
                        //ui.dropBlockSmoothly(toBeDropBlock, numLineClear);
                        //ui.removeBlock(fallingBlock);
                        //ui.addBlock(fallingBlock);
                        if (gotLineRemoval) {
                            fallingBlock.dropImmediately(numLinesClear);
                            ui.addFallingBlock(fallingBlock, numLinesClear);
                            inactiveBlocksArray[r][c] = null;
                            inactiveBlocksArray[r + numLinesClear][c] = fallingBlock;
                        }

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
    
    // =================================================
    // Handle special effects
    // =================================================

    public void handleClearLineSpecialEffect() {
        ui.handleClearLineSpecialEffect(effectCounter, SPECIAL_EFFECT_DURATION);

        effectCounter++;

        if (effectCounter > SPECIAL_EFFECT_DURATION) {
            // clear cached block in ui
            ui.clear();
            System.out.println("check ui cache is empty = " + (ui.fallingBlocks.size() == 0 && ui.fadingBlocks.size() == 0));

            isEffectOn = false;
            effectCounter = 0;
        }


    }
    

    




}
