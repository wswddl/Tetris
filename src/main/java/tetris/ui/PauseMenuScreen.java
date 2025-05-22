package tetris.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import tetris.logic.GameController;
import tetris.util.ButtonHandler;

public class PauseMenuScreen extends UiPart<StackPane> {
    private static final String FXML = "PauseMenu.fxml";
    //private GameController gameController;
    ButtonHandler resumeButtonHandler;
    @FXML
    private StackPane myStackPane;
    @FXML
    private Button resumeButton;

    public PauseMenuScreen() {
        super(FXML);
    }

    public void setButtonHandler(ButtonHandler buttonHandler) {
        this.resumeButtonHandler = buttonHandler;
    }

    @FXML
    public void handleResumeButton() {
        resumeButtonHandler.handle();
    }


}
