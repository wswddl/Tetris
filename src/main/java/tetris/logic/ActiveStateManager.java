package tetris.logic;

import tetris.block.Mino;
import tetris.block.MinoBlock;
import tetris.ui.GameScreen;

public class ActiveStateManager {
    GameState gameState;
    GameScreen gameplayUI;
    MinoManager minoManager;
    MinoBlock[][] inactiveBlocksArray;


    CollisionDetector collisionDetector;
    InputHandler inputHandler;

    public ActiveStateManager(GameState gameState, GameScreen gameplayUI, MinoManager minoManager, MinoBlock[][] inactiveBlocksArray) {
        this.gameState = gameState;
        this.gameplayUI = gameplayUI;
        this.minoManager = minoManager;
        this.inactiveBlocksArray = inactiveBlocksArray;


        collisionDetector = new CollisionDetector(inactiveBlocksArray);
        inputHandler = new InputHandler(gameState, gameplayUI, minoManager, inactiveBlocksArray, collisionDetector);
    }
    // gameState.
    public void update() {
        /*
        if (KeyInputHandler.holdPress && gameState.isAllowSwapMino()) {
            inputHandler.handleHoldPress();
        }

        // keyboard input handler...
        if (KeyInputHandler.upPress) {
            inputHandler.handleUpPress();
        }

        //this.checkCurrentMinoMovementCollision();
        Mino currentMino = minoManager.getCurrentMino();
        collisionDetector.checkCurrentMinoMovementCollision(currentMino);

        if (KeyInputHandler.spacePress) {
            inputHandler.handleSpacePress();
        }
        if (KeyInputHandler.downPress) {
            inputHandler.handleDownPress();
        }
        if (KeyInputHandler.leftPress) {
            inputHandler.handleLeftPress();
        }
        if (KeyInputHandler.rightPress) {
            inputHandler.handleRightPress();
        }

        // auto drop
        this.autoDropMechanism();

        // after actions, check if the mino is deactivating
        // NOTE: don't check isDeactivating before any action (KeyInput handling) to prevent buggy output (e.g. floating inactive blocks)
        if (gameState.isDeactivating()) {
            this.startDeactivatingCurrentMino();
        }*/

        if (KeyInputController.minoAction == MinoAction.HOLD && gameState.isAllowSwapMino()) {
            inputHandler.handleHoldPress();
        }

        // keyboard input handler...
        if (KeyInputController.minoAction == MinoAction.UP) {
            inputHandler.handleUpPress();
        }

        //this.checkCurrentMinoMovementCollision();
        Mino currentMino = minoManager.getCurrentMino();
        collisionDetector.checkCurrentMinoMovementCollision(currentMino);

        if (KeyInputController.minoAction == MinoAction.SPACE) {
            inputHandler.handleSpacePress();
        }
        if (KeyInputController.minoAction == MinoAction.DOWN) {
            inputHandler.handleDownPress();
        }
        if (KeyInputController.minoAction == MinoAction.LEFT) {
            inputHandler.handleLeftPress();
        }
        if (KeyInputController.minoAction == MinoAction.RIGHT) {
            inputHandler.handleRightPress();
        }

        // auto drop
        this.autoDropMechanism();

        // after actions, check if the mino is deactivating
        // NOTE: don't check isDeactivating before any action (KeyInput handling) to prevent buggy output (e.g. floating inactive blocks)
        if (gameState.isDeactivating()) {
            this.startDeactivatingCurrentMino();
        }
    }

    /**
     * Deactivates the mino when {@code DEACTIVATE_INTERVAL} is reached and {@code bottomCollision} is still true.
     * This method is invoked by {@code update()} when {@code isDeactivating} is true.
     */
    public void startDeactivatingCurrentMino() {
        gameState.incrementDeactivationCounter();

        if (gameState.hasReachedDeactivateInterval()) {
            // check if the mino is still hitting the bottom after 1 second
            // if still hitting the bottom, deactivate it
            // reset the deactivate counter and flag
            Mino currentMino = minoManager.getCurrentMino();
            collisionDetector.checkCurrentMinoMovementCollision(currentMino);

            if (collisionDetector.hasBottomCollision()) {
                // Clear the all shadow when touch down (prevent bugs)
                gameplayUI.clearAllShadowInPlayingField();
                minoManager.deactivateCurrentMino();
            }
            gameState.resetDeactivation();
        }
    }

     private void autoDropMechanism() {
        //***** if (bottomCollision) {
        if (collisionDetector.hasBottomCollision()) {
            //gameState.isDeactivating = true;
            gameState.setDeactivating();
        } else {
            gameState.incrementAutoDropCounter();
            if (gameState.hasReachAutoDropInterval()) {
                // Clear UI
                Mino currentMino = minoManager.getCurrentMino();
                gameplayUI.removeMinoInPlayingField(currentMino);

                currentMino.moveDown();

                // Set UI
                gameplayUI.addMinoInPlayingField(currentMino);

                gameState.resetAutoDropCounter();
            }
        }
    }

}
