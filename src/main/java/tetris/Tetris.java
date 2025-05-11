package tetris;

import javafx.application.Application;
import javafx.stage.Stage;

public class Tetris extends Application {

    public static final int BLOCK_SIZE = 30;
    public static final int MOVEMENT_SIZE = 30;
    public static final int MAX_WIDTH = 30 * 10;
    public static final int MAX_HEIGHT = 30 * 20;
    public static int[][] grid = new int[MAX_WIDTH / BLOCK_SIZE][MAX_HEIGHT / BLOCK_SIZE];

    @Override
    public void start(Stage stage) throws Exception {

    }
}
