package tetris.logic;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.Node;

public class KeyInputController {

    public static boolean upPress, downPress, leftPress, rightPress, escapePress, spacePress, holdPress, restartPress, resumePress, restartPressForTimesUp, pausePress;
    //  public Controller gameController;
    public static MinoAction minoAction;
    public GameState gameState;

    //public KeyInputHandler(Scene scene, Controller gameController) {
    public KeyInputController(Scene scene, GameState gameState) {
        // Set key event handlers on the scene
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);

        this.gameState = gameState;

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
        if (gameState.isGameOver()) {
            this.keyPressedWhenGameOver(ke);
        }
        if (!gameState.isGamePaused()) {
            this.keyPressedWhenGameIsPlaying(ke);
        } else {
            this.keyPressedWhenGameIsPaused(ke);
        }
    }
    private void keyPressedWhenGameOver(KeyEvent ke) {
        var code = ke.getCode();

        if (code == KeyCode.R) {
            //****restartPress = true;
            gameState.restartGame();
            gameState.playGameLoop();
        }
    }
    public void keyPressedWhenGameIsPlaying(KeyEvent ke) {
        var code = ke.getCode();

        if (code == KeyCode.W || code == KeyCode.UP) {
            //upPress = true;
            minoAction = MinoAction.UP;
        }
        else if (code == KeyCode.A || code == KeyCode.LEFT) {
            // leftPress = true;
            minoAction = MinoAction.LEFT;
        }
        else if (code == KeyCode.S || code == KeyCode.DOWN) {
            //downPress = true;
            minoAction = MinoAction.DOWN;
        }
        else if (code == KeyCode.D || code == KeyCode.RIGHT) {
           //rightPress = true;
            minoAction = MinoAction.RIGHT;
        }
        else if (code == KeyCode.ESCAPE) {
            //escapePress = true;
            /*
            if (!pausePress) {
                pausePress = true;
            } else {
                resumePress = true;
                pausePress = false;
            }*/
        }
        else if (code == KeyCode.R) {
            //pausePress = false;
            //resumePress = true;
        }
        else if (code == KeyCode.SPACE) {
            //spacePress = true;
            minoAction = MinoAction.SPACE;
        }
        // hold
        else if (code == KeyCode.SHIFT || code == KeyCode.TAB) {
            //holdPress = true;
            minoAction = MinoAction.HOLD;
        }
    }

    public void keyPressedWhenGameIsPaused(KeyEvent ke) {

    }


    private void keyReleased(KeyEvent ke) {
        // You can handle key releases if needed
        // This is a stub for handling when the key is released, if necessary
    }

    public static void ignoreMovement() {
        upPress = downPress = leftPress = rightPress = spacePress = holdPress = false;
    }

    public static void resetPressFlags() {
        upPress = downPress = leftPress = rightPress = escapePress = spacePress = holdPress = restartPress = resumePress = restartPressForTimesUp = pausePress = false;
    }

    public static void enableTabKey(Node node) {
        // In JavaFX, Tab key focus management needs to be done via `setFocusTraversable`
        node.setFocusTraversable(true);
    }
}