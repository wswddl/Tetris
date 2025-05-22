package tetris.logic;

import javafx.scene.effect.GaussianBlur;
import tetris.ui.GameScreen;
import tetris.ui.PauseMenu;

public class PauseController{
    GameScreen gameplayUI;
    PauseMenu pauseMenuUI;

    public PauseController(GameScreen gameplayUI, PauseMenu pauseMenuUI) {
        this.gameplayUI = gameplayUI;
        this.pauseMenuUI = pauseMenuUI;
    }

    public void handlePausePress() {
        gameplayUI.getRoot().setEffect(new GaussianBlur(10));
        pauseMenuUI.getRoot().setVisible(true);

        // NOTE: Don't reset the pausePress to false, needed to detect resume press

        // TODO: TetrisPanel.OST.pause();

    }
    public void handleResumePress() {
        gameplayUI.getRoot().setEffect(null); // remove blur
        pauseMenuUI.getRoot().setVisible(false);

        KeyInputController.resumePress = false;
        KeyInputController.pausePress = false;

        // TODO:
        // TetrisPanel.OST.resume();
    }
}
