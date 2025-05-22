package tetris.logic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import tetris.Bag;
import tetris.block.*;
import tetris.ui.GameScreen;
import tetris.ui.PauseMenu;

import static tetris.util.TetrisConstants.*;

public class TetrisManager {
    private CollisionDetector collisionDetector;
    private GameplayManager gameplayManager;
    private PauseController pauseController;
    private GameScreen ui;
    private GameScreen gameplayUI;
    private PauseMenu pauseMenuUI;
    private GameState gameState;
    private GameMetrics gameMetrics;
    private Timeline gameLoop;

    //private MinoManager minoManager;
    //private InputHandler inputHandler;

    // ui
    //@FXML
    private Pane holdMinoBox;
    //@FXML
    private Pane playingField;
    //@FXML
    private Pane nextMinoBox;
    //private UiManager ui;


    // minos
    private MinoBlock[][] inactiveBlocksArray; // only keep track of inactive blocks, not including active blocks
    public Bag<Mino> bagOfMinos;
    public Mino currentMino;
    private Mino nextMino;
    private Mino holdMino;

    // Signals for the game logic
    private boolean allowSwapMino;
    private boolean isDeactivating;
    private boolean isEffectOn; // effect is on when there is line removal
    private boolean isTSpin;
    public boolean isGameOver;
    public boolean isPaused;
    public boolean isTimesUp;

    // game counter
    private int deactivateCounter;
    private int autoDropCounter;
    private int gameCounter;
    private int effectCounter;
    private final int GAME_DURATION = 2 * 60 * FPS; // 2 minutes gameplay

    // game metrics


    // =================================================
    // Initialize the controller
    // =================================================

    /**
     * Is called by loader.load()
     */
    //public void initialize() {
    public TetrisManager(GameScreen gameplayUI, PauseMenu pauseMenuUI) {
        setUpGameLoop();

        //ui = new UiManager(playingField, nextMinoBox, holdMinoBox);
        this.ui = gameplayUI;
        this.gameplayUI = gameplayUI;
        this.pauseMenuUI = pauseMenuUI;

        this.inactiveBlocksArray = new MinoBlock[NUM_OF_ROW][NUM_OF_COL];

        this.gameState = new GameState(gameLoop);
        this.gameMetrics = new GameMetrics();

        //this.fillInBagOfMinos();
        //this.initializeMinos();
        //this.initializeSignals();
        //this.collisionDetector = new CollisionDetector(inactiveBlocksArray);
        //this.initializeCounters();


        this.gameplayManager = new GameplayManager(gameState, gameplayUI);
        this.pauseController = new PauseController(gameplayUI, pauseMenuUI);



    }
    public GameState getGameState() {
        return this.gameState;
    }

    private void setUpGameLoop() {
        // Create the game loop
        gameLoop = new Timeline(
            new KeyFrame(Duration.seconds(1.0 / FPS),
                e -> {
                    if (!gameState.isGamePaused() && !gameState.isGameOver()) {
                        this.update();
                    }
                }
            )
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE); // repeat forever
        gameLoop.play(); // start the loop
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
        this.effectCounter = 0;
    }
    private void initializeSignals() {
        isDeactivating = false;
        isEffectOn = false;
        isTSpin = false;
        isGameOver = false;
        isPaused = false;
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
        if (!isEffectOn) {
            ui.addMinoShadowInPlayingField(mino);
        }
        // if effect is on, effect handler will add the shadow at the end of special effect (i.e. when the effect is about to end)
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

    public void update() {
        if (gameState.isGameOver()) {
            gameLoop.pause();
            /*
            if (KeyInputHandler.restartPress) {
                // reset the game over flag
                gameState.restartGame();
                // handle restart game logic
            }*/

        }
/*
        else if (gameState.isGamePaused()) { // game has been paused
            if (KeyInputHandler.escapePress || KeyInputHandler.resumePress|| pauseMenuUI.isResumeButtonClicked()) {
                // resume the game
                gameState.resumeTheGame();
                // handle resume
                pauseController.handleResumePress();
            }
        }
*/
        else if (!gameState.isGamePaused()) { // game isn't paused
            if (KeyInputController.escapePress) {
                // pause the game
                gameState.pauseTheGame();
                // handle pause
                pauseController.handlePausePress();

                gameLoop.pause();

            } else {
                gameplayManager.update();
            }
        }
        KeyInputController.resetPressFlags();
        KeyInputController.minoAction = null;

/*

        if (!KeyInputHandler.pausePress && !KeyInputHandler.resumePress && !isGameOver) {
            gameplayController.update();
        } else if (KeyInputHandler.pausePress && !isGameOver) {
            KeyInputHandler.ignoreMovement();
            pauseController.handlePausePress();
            //TetrisPanel.OST.pause(); handled in pausecontroller
        } else if (KeyInputHandler.resumePress && !isGameOver) {
            //TetrisPanel.OST.resume(); handled in pausecontroller
            // KeyInputHandler.resumePress = false;
            pauseController.handleResumePress();
        } else if (isGameOver && KeyInputHandler.restartPress) {
            // restart();
        /*} else if (TetrisFunctions.isTimesUp && KeyInputHandler.restartPressForTimesUp) {
            tf.restart();*//*
        } else { // gameOver and NO restart
            KeyInputHandler.ignoreMovement();
            KeyInputHandler.pausePress = false;
        }*/

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    /**
     * Gets invoke by {@Code Tetris} gameLoop every time interval.
     * Updates the Minos and other UI components every time interval.
     */
    public void update123() {
        if (!KeyInputController.pausePress && !KeyInputController.resumePress && !isGameOver) {
            updateNormalGameplay();
        } else if (KeyInputController.pausePress && !isGameOver) {
            KeyInputController.ignoreMovement();
            handlePausePress();
            //TetrisPanel.OST.pause();
        } else if (KeyInputController.resumePress && !isGameOver) {
            //TetrisPanel.OST.resume();
            KeyInputController.resumePress = false;
            handleResumePress();
        } else if (isGameOver && KeyInputController.restartPress) {
            // restart();
        /*} else if (TetrisFunctions.isTimesUp && KeyInputHandler.restartPressForTimesUp) {
            tf.restart();*/
        } else { // gameOver and NO restart
            KeyInputController.ignoreMovement();
            KeyInputController.pausePress = false;
        }
    }

    public void updateNormalGameplay() {
        if (currentMino.isActive() && KeyInputController.holdPress && allowSwapMino) {
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

        KeyInputController.holdPress = false;
        gameCounter = gameCounter + 1;
    }



    public void updateWhenMinoIsActiveOnly() {
        if (isDeactivating) {
            this.startDeactivatingCurrentMino();
        }

        // keyboard input handler...
        if (KeyInputController.upPress) {
            handleUpPress();
        }

        //this.checkCurrentMinoMovementCollision();
        collisionDetector.checkCurrentMinoMovementCollision(currentMino);

        if (KeyInputController.spacePress) {
            handleSpacePress();
        }
        if (KeyInputController.downPress) {
            handleDownPress();
        }
        if (KeyInputController.leftPress) {
            handleLeftPress();
        }
        if (KeyInputController.rightPress) {
            handleRightPress();
        }

        // auto drop
        this.autoDropMechanism();

    }
    private void updateWhenMinoIsInactiveOnly() {
        if (this.checkGameOver()) {
            this.isGameOver = true;
        }
        this.handleRemoveLine();

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

            //this.checkCurrentMinoMovementCollision();  *******
            collisionDetector.checkCurrentMinoMovementCollision(currentMino);

            // ********* if (bottomCollision) {
            if (collisionDetector.hasBottomCollision()) {
                // Clear the all shadow when touch down (prevent bugs)
                ui.clearAllShadowInPlayingField();

                currentMino.deactivate(playingField); // passing playing field isnt used in deactivate() lol
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
        //***** if (bottomCollision) {
        if (collisionDetector.hasBottomCollision()) {
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

    // =================================================
    // Key Press Hnadler
    // =================================================

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


        KeyInputController.upPress = false;
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
        KeyInputController.holdPress = false;

        // Sound effect for Hold (Shift)
        //TetrisPanel.soundEffect.play(11, false); // TODO: sound effect for hold
    }
    private void handleSpacePress() {

        // Clear UI
        ui.removeMinoInPlayingField(currentMino);
        ui.clearAllShadowInPlayingField(); // clear all shadow and not just the mino position to prevent bugs

        //****** while(!bottomCollision) {
        while(!collisionDetector.hasBottomCollision()) {

            currentMino.moveDown();

            // ******* this.checkCurrentMinoMovementCollision();
            collisionDetector.checkCurrentMinoMovementCollision(currentMino);
        }
        // deactivate immediately
        this.currentMino.deactivate(playingField);

        // Set UI
        ui.addMinoInPlayingField(currentMino);

        autoDropCounter = 0;
        KeyInputController.spacePress = false;

        // Sound effect for Space
        //TetrisPanel.soundEffect.play(12, false); TODO: sound effect for space
    }
    private void handleDownPress() {
        //if (!bottomCollision) {
        if (!collisionDetector.hasBottomCollision()) {
            // Clear UI
            ui.removeMinoInPlayingField(currentMino);
            ui.removeMinoShadowInPlayingField(currentMino);

            currentMino.moveDown();
            autoDropCounter = 0;

            // Set UI
            ui.addMinoInPlayingField(currentMino);
            ui.addMinoShadowInPlayingField(currentMino);
        } else {
            ui.removeMinoShadowInPlayingField(currentMino); // remember to remove the shadow when "touch down"
        }

        KeyInputController.downPress = false;
    }
    private void handleLeftPress() {
        // Clear UI
        ui.removeMinoInPlayingField(currentMino);
        ui.removeMinoShadowInPlayingField(currentMino);

        // **** if (!leftCollision) {
        if (!collisionDetector.hasLeftCollision()) {
            currentMino.moveLeft();
            // after moving to the left, set the shadow position again
            currentMino.setShadowPosition(inactiveBlocksArray);
        }

        // Set UI
        ui.addMinoInPlayingField(currentMino);
        ui.addMinoShadowInPlayingField(currentMino);

        KeyInputController.leftPress = false;
    }
    private void handleRightPress() {
        // Clear UI
        ui.removeMinoInPlayingField(currentMino);
        ui.removeMinoShadowInPlayingField(currentMino);

        // ****** if (!rightCollision) {
        if (!collisionDetector.hasRightCollision()) {
            currentMino.moveRight();
            // after moving to the right, set the shadow position again
            currentMino.setShadowPosition(inactiveBlocksArray);
        }

        // Set UI
        ui.addMinoInPlayingField(currentMino);
        ui.addMinoShadowInPlayingField(currentMino);

        KeyInputController.rightPress = false;
    }
    public void handlePausePress() {
        ui.getRoot().setEffect(new GaussianBlur(10));
        pauseMenuUI.getRoot().setVisible(true);

        // don't reset the pausePress to false, needed to detect resume press
    }
    public void handleResumePress() {
        ui.getRoot().setEffect(null); // remove blur
        pauseMenuUI.getRoot().setVisible(false);

        KeyInputController.resumePress = false;
        KeyInputController.pausePress = false;
    }


    // =================================================
    // Removing lines and effects
    // =================================================

    /**
     * Starts from the lowest row to the top, if there is a full row (10 blocks in a row), remove it from the
     * playing field, else drop the inactive blocks on top to fill in the gap of the deleted row.
     */
    private void handleRemoveLine() {
        int numLinesClear = 0;
        boolean gotLineRemoval = false;

        // check if it is potentially a t spin before adding current mino into inactive block array
        boolean isPotentialTSpin = currentMino.checkTSpinConfiguration(inactiveBlocksArray);

        // add the current mino blocks into the inactive block array
        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            int row = currentMino.blocks[i].getRow();
            int col = currentMino.blocks[i].getCol();
            inactiveBlocksArray[row][col] = currentMino.blocks[i];
        }

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
                numLinesClear++;
                for (int c = 0; c < inactiveBlocksArray[0].length; c++) {
                    MinoBlock toBeRemovedBlock = inactiveBlocksArray[r][c];
                    if (toBeRemovedBlock != null) {
                        ui.addFadingBlock(toBeRemovedBlock);
                        inactiveBlocksArray[r][c] = null;
                    }
                }
            } else {
                for (int c = 0; c < inactiveBlocksArray[0].length; c++) {
                    MinoBlock fallingBlock = inactiveBlocksArray[r][c];
                    if (fallingBlock != null && gotLineRemoval) {
                        fallingBlock.dropImmediately(numLinesClear);
                        ui.addFallingBlock(fallingBlock, numLinesClear);
                        inactiveBlocksArray[r][c] = null;
                        inactiveBlocksArray[r + numLinesClear][c] = fallingBlock;
                    }
                }
            }
        }
        // after scanning through the board
        if (gotLineRemoval) {
            this.isEffectOn = true;
            this.isTSpin = isPotentialTSpin;
            if (isTSpin) {
                // TODO: t spin sound effect
            }
            // TODO: sound effect based on numLinesClear

        }

        gameMetrics.updateScore(numLinesClear, false);
        ui.updateScore(gameMetrics.getScore());


    }

    // =================================================
    // game over & restart
    // =================================================

    public boolean checkGameOver() {
        if (currentMino.isAtStartingPosition()) {
            return true;
        }
        return false;
    }

    // =================================================
    // Handle special effects
    // =================================================

    /**
     * Handles special effects when there is a line cleared.
     */
    public void handleClearLineSpecialEffect() {
        ui.handleClearLineSpecialEffect(effectCounter);

        if (isTSpin) {
            ui.handleTSpinSpecialEffect(effectCounter);
        }

        effectCounter++;

        if (effectCounter > SPECIAL_EFFECT_DURATION) {
            // clear cached block in ui
            ui.clearEffect();

            // add the shadow when the animation is finished
            ui.addMinoShadowInPlayingField(currentMino);

            // reset all the flags
            isEffectOn = false;
            isTSpin = false;
            effectCounter = 0;
        }
    }
}
