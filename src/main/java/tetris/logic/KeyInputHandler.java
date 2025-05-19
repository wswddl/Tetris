package tetris.logic;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.Node;
import tetris.logic.Controller;

public class KeyInputHandler {

    public static boolean upPress, downPress, leftPress, rightPress, pausePress, spacePress, holdPress, restartPress, resumePress, restartPressForTimesUp;
    public Controller gameController;

    public KeyInputHandler(Scene scene, Controller gameController) {
        // Set key event handlers on the scene
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);

        this.gameController = gameController;
    }

    private void keyPressed(KeyEvent ke) {
        var code = ke.getCode();

        if (gameController.isGameOver && code != KeyCode.SPACE) {
            restartPress = true;
        }
        else if (gameController.isTimesUp && code == KeyCode.R) {
            restartPressForTimesUp = true;
        }
        else if (code == KeyCode.W || code == KeyCode.UP) {
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
            if (!pausePress) {
                pausePress = true;
            } else {
                resumePress = true;
                pausePress = false;
            }
        }
        else if (code == javafx.scene.input.KeyCode.R) {
            pausePress = false;
        }
        else if (code == javafx.scene.input.KeyCode.SPACE) {
            spacePress = true;
        }
        // hold
        else if (code == javafx.scene.input.KeyCode.SHIFT || code == javafx.scene.input.KeyCode.TAB) {
            holdPress = true;
        }
    }

    private void keyReleased(KeyEvent ke) {
        // You can handle key releases if needed
        // This is a stub for handling when the key is released, if necessary
    }

    public static void ignoreMovement() {
        upPress = downPress = leftPress = rightPress = spacePress = holdPress = false;
    }

    public static void enableTabKey(Node node) {
        // In JavaFX, Tab key focus management needs to be done via `setFocusTraversable`
        node.setFocusTraversable(true);
    }
}