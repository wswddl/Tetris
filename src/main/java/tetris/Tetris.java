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

import java.util.Arrays;

import static tetris.util.TetrisConstants.FPS;

public class Tetris extends Application {

    public static final int BLOCK_SIZE = 30;
    public static final int MOVEMENT_SIZE = 30;
    public static final int MAX_WIDTH = 30 * 10;
    public static final int MAX_HEIGHT = 30 * 20;
    public static int[][] grid = new int[MAX_WIDTH / BLOCK_SIZE][MAX_HEIGHT / BLOCK_SIZE];
    private Timeline gameLoop;
    private Controller gameController;

    @Override
    public void start(Stage stage) throws Exception {
        for (int[] row : grid) {
            Arrays.fill(row, 0);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Tetris.fxml"));
            Parent root = loader.load();
            this.gameController = loader.getController();

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            // Keyboard input handler
            new KeyInputHandler(scene, gameController);

            // Create the game loop
            gameLoop = new Timeline(new KeyFrame(Duration.seconds(1.0 / FPS), e -> gameController.update()));
            gameLoop.setCycleCount(Timeline.INDEFINITE); // repeat forever
            gameLoop.play(); // start the loop

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
