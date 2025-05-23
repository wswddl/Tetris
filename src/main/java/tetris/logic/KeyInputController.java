package tetris.logic;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.Node;

public class KeyInputController {

    public GameState gameState;
    public GameController gameController;
    public GameplayManager gameplayManager;
    public ActiveStateManager activeStateManager;

    public KeyInputController(Scene scene, GameState gameState, GameController gameController) {
        // Set key event handlers on the scene
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);

        this.gameState = gameState;
        this.gameController = gameController;
        this.gameplayManager = gameController.getGameplayManager();
        this.activeStateManager = gameplayManager.getActiveStateManager();


        //this.gameController = gameController;
    }
    /*
    private void keyPressed(KeyEvent ke) {
        var code = ke.getCode();

        if ((gameState.isGameOver() || gameState.isTimesUp()) && code == KeyCode.R) {
            //****restartPress = true;
            gameState.restartGame();
            gameState.playGameLoop();
        }
       /* else if (gameState.isTimesUp() && code == KeyCode.R) {
            restartPressForTimesUp = true;
        }*//*
        if (code == KeyCode.W || code == KeyCode.UP) {
            upPress = true;
        }
        else if (code == KeyCode.A || code == KeyCode.LEFT) {
            leftPress = true;
        }
        else if (code == KeyCode.S || code == KeyCode.DOWN) {
            downPress = true;
        }
        else if (code == KeyCode.D || code == KeyCode.RIGHT) {
            rightPress = true;
        }
        else if (code == KeyCode.ESCAPE) {
            escapePress = true;
            /*
            if (!pausePress) {
                pausePress = true;
            } else {
                resumePress = true;
                pausePress = false;
            }*//*
        }
        else if (code == KeyCode.R) {
            //pausePress = false;
            resumePress = true;
        }
        else if (code == KeyCode.SPACE) {
            spacePress = true;
        }
        // hold
        else if (code == KeyCode.SHIFT || code == KeyCode.TAB) {
            holdPress = true;
        }
    }*/
    private void keyPressed(KeyEvent ke) {
        if (gameController.isIgnoreKeyInput()) {
            return;
        }

        if (gameState.isGameOver()) {
            this.keyPressedWhenGameOver(ke);
        } else if (!gameState.isGamePaused()) {
            this.keyPressedWhenGameIsPlaying(ke);
        } else if (gameState.isGamePaused()) {
            this.keyPressedWhenGameIsPaused(ke);
        }
    }
    private void keyPressedWhenGameOver(KeyEvent ke) {
        var code = ke.getCode();

        if (code == KeyCode.R) {
            gameController.restartGame();
            //****restartPress = true;
            //gameState.restartGame();
            //gameState.playGameLoop();
        }
    }
    public void keyPressedWhenGameIsPlaying(KeyEvent ke) {
        var code = ke.getCode();

        if (code == KeyCode.W || code == KeyCode.UP) {
            activeStateManager.handleUpPress();
        }
        else if (code == KeyCode.A || code == KeyCode.LEFT) {
            activeStateManager.handleLeftPress();
        }
        else if (code == KeyCode.S || code == KeyCode.DOWN) {
            activeStateManager.handleDownPress();
        }
        else if (code == KeyCode.D || code == KeyCode.RIGHT) {
            activeStateManager.handleRightPress();
        }
        else if (code == KeyCode.SPACE) {
            activeStateManager.handleSpacePress();
        }
        // hold
        else if (code == KeyCode.SHIFT || code == KeyCode.TAB) {
            //holdPress = true;
            activeStateManager.handleHoldPress();
        }
        // pause game
        else if (code == KeyCode.ESCAPE) {
            gameController.pauseGame();
        }
    }

    public void keyPressedWhenGameIsPaused(KeyEvent ke) {
        var code = ke.getCode();
        // resume game
        if (code == KeyCode.ESCAPE || code == KeyCode.R) {
            gameController.resumeGame();
        }
    }


    private void keyReleased(KeyEvent ke) {
    }

    public static void ignoreMovement() {

    }

    public static void resetPressFlags() {

    }

    public static void enableTabKey(Node node) {
        // In JavaFX, Tab key focus management needs to be done via `setFocusTraversable`
        node.setFocusTraversable(true);
    }
}