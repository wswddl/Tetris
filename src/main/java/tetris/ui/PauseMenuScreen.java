package tetris.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import tetris.logic.GameController;
import tetris.util.ButtonHandler;

public class PauseMenuScreen extends UiPart<StackPane> {
    private static final String FXML = "PauseMenu.fxml";
    //private GameController gameController;
    ButtonHandler resumeButtonHandler;
    ButtonHandler restartButtonHandler;
    @FXML
    private StackPane myStackPane;
    @FXML
    private Button resumeButton;
    @FXML
    private Button restartButton;
    @FXML
    private Pane testPane;

    public PauseMenuScreen() {
        super(FXML);
    }

    public void setResumeRestartButtonHandler(ButtonHandler resumeButtonHandler, ButtonHandler restartButtonHandler) {
        this.resumeButtonHandler = resumeButtonHandler;
        this.restartButtonHandler = restartButtonHandler;
    }

    @FXML
    public void handleResumeButton() {
        TranslateTransition slide = new TranslateTransition(Duration.seconds(1.5), testPane);
        slide.setByX(200); // Move 200px right
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), testPane);
        fade.setToValue(0.0);

        ParallelTransition combined = new ParallelTransition(slide, fade);

        // Hide root node AFTER animation completes
        combined.setOnFinished(e -> {
            resumeButtonHandler.handle();
            testPane.setTranslateX(0); // Reset position for future use
            testPane.setOpacity(1.0); // Reset opacity
        });

        combined.play();
        //resumeButtonHandler.handle();

    }
    @FXML
    public void handleRestartButton() {
        restartButtonHandler.handle();
    }


}
