package tetris;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import tetris.logic.Controller;
import tetris.logic.KeyInputHandler;
import tetris.ui.GameplayUI;
import tetris.ui.MainWindow;
import tetris.ui.UiManager;

import static tetris.util.TetrisConstants.FPS;

public class Tetris extends Application {
    private Timeline gameLoop;
    private MainWindow mainWindow;
    private Controller gameController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {

            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Tetris.fxml"));
            //Parent root = loader.load();
            //this.ui = loader.getController();
            //this.ui = new UiManager(primaryStage);
            this.mainWindow = new MainWindow(primaryStage);
            mainWindow.show();
            mainWindow.fillInnerParts();

            //Parent root = ui.getRoot();
            // gameController = new Controller(ui);
            gameController = mainWindow.getController();

            // ui.start();



            // Keyboard input handler
            Scene gameScene = mainWindow.getGameScene();
            new KeyInputHandler(gameScene, gameController);

            setUpGameLoop();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpFxmlLoaderAndComponents() {

    }

    private void setUpGameLoop() {
        // Create the game loop
        gameLoop = new Timeline(new KeyFrame(Duration.seconds(1.0 / FPS), e -> gameController.update()));
        gameLoop.setCycleCount(Timeline.INDEFINITE); // repeat forever
        gameLoop.play(); // start the loop
    }

}
