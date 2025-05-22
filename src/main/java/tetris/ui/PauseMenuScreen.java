package tetris.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class PauseMenu extends UiPart<StackPane> {
    private static final String FXML = "PauseMenu.fxml";
    private boolean isResumeButtonClicked;
    private GameScreen gameScreen;
    @FXML
    private StackPane myStackPane;
    @FXML
    private Button resumeButton;

    public PauseMenu() {
        super(FXML);
        isResumeButtonClicked = false;
    }

    /**
     * Checks if the resume button has been clicked and reset the flag.
     */
    public boolean isResumeButtonClicked() {
        boolean isClicked = isResumeButtonClicked;
        isResumeButtonClicked = false;
        return isClicked;
    }

    @FXML
    public void handleResumeButton() {

    }
}
