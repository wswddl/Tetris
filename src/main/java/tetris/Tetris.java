package tetris;

import javafx.application.Application;
import javafx.stage.Stage;
import tetris.ui.MainWindow;

public class Tetris extends Application {
    private MainWindow mainWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {

            this.mainWindow = new MainWindow(primaryStage);
            mainWindow.show();
            mainWindow.fillInnerParts();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
