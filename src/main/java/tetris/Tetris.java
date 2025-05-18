package tetris;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import tetris.ui.UiManager;

import java.util.Arrays;

import static tetris.util.TetrisConstants.FPS;

public class Tetris extends Application {
    private Timeline gameLoop;
    private UiManager ui;
    private Controller gameController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {

            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Tetris.fxml"));
            //Parent root = loader.load();
            //this.ui = loader.getController();
            this.ui = new UiManager(primaryStage);
            ui.show();
            ui.fillInnerParts();

            //Parent root = ui.getRoot();
            gameController = new Controller(ui);

            // ui.start();



            // Keyboard input handler
            Scene gameScene = ui.getGameScene();
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
