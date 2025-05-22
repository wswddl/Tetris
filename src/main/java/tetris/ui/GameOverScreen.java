package tetris.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import tetris.util.ButtonHandler;

public class GameOverScreen  extends UiPart<StackPane> {
    private static final String FXML = "GameOverScreen.fxml";
    private ButtonHandler restartButtonHandler;
    @FXML
    Button restartButton;
    @FXML
    Button exitButton;


    public GameOverScreen() {
        super(FXML);
    }
    public void setRestartButtonHandler(ButtonHandler restartButtonHandler) {
        this.restartButtonHandler = restartButtonHandler;
    }

    @FXML
    public void handleRestartButton() {
        System.out.println("restart button");
        restartButtonHandler.handle();
    }

    @FXML
    public void handleExitButton() {

    }
}
