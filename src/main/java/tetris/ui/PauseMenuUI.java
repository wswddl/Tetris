package tetris.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import tetris.logic.KeyInputHandler;

public class PauseMenuUI extends UiPart<StackPane> {
    private static final String FXML = "PauseMenu.fxml";
    @FXML
    private StackPane myStackPane;
    @FXML
    private Button resumeButton;

    public PauseMenuUI() {
        super(FXML);
    }

    @FXML
    public void handleResumeButton() {
        KeyInputHandler.pausePress = false;
        KeyInputHandler.resumePress = true;
    }
}
