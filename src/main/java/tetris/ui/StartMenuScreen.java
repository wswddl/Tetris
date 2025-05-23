package tetris.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import tetris.logic.GameState;
import tetris.util.ButtonHandler;

public class StartMenuScreen extends UiPart<AnchorPane> {
    private static final String FXML = "StartMenu.fxml";
    private ButtonHandler playButtonHandler;
    @FXML
    private Button playButton;

    public StartMenuScreen() {
        super(FXML);
    }

    public void setPlayButtonHandler(ButtonHandler playButtonHandler) {
        this.playButtonHandler = playButtonHandler;
    }
    @FXML
    public void handlePlayButton() {
        playButtonHandler.handle();
    }

    public ParallelTransition closeStartMenuEffects(GameState gameState) {
        return new ParallelTransition();
    }
}
