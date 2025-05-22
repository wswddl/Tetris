package tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tetris.logic.GameState;
import tetris.logic.KeyInputController;
import tetris.logic.GameController;
import tetris.ui.GameScreen;
import tetris.ui.MainWindow;
import tetris.ui.PauseMenuScreen;

public class Tetris extends Application {
    private MainWindow mainWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {

            this.mainWindow = new MainWindow(primaryStage);
            mainWindow.show();
            mainWindow.fillInnerParts();
            mainWindow.setUpGame();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
