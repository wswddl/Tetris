package tetris.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import tetris.logic.GameState;
import tetris.util.ButtonHandler;

public class GameOverScreen  extends UiPart<VBox> {
    private static final String FXML = "GameOverScreen.fxml";
    private ButtonHandler restartButtonHandler;
    private ButtonHandler exitButtonHandler;
    @FXML
    Label gameOverLabel;
    @FXML
    Button restartButton;
    @FXML
    Button exitButton;

    public GameOverScreen() {
        super(FXML);
    }
    public void setRestartExitButtonHandler(ButtonHandler restartButtonHandler, ButtonHandler exitButtonHandler) {
        this.restartButtonHandler = restartButtonHandler;
        this.exitButtonHandler = exitButtonHandler;
    }

    @FXML
    public void handleRestartButton() {
        restartButtonHandler.handle();
    }

    @FXML
    public void handleExitButton() {
        exitButtonHandler.handle();
    }

    public void openGameOverScreenEffects(GameState gameState, GameScreen gameScreen) {
        gameScreen.setPauseEffects();

        int fromX = 500;

        this.getRoot().setVisible(true);

        TranslateTransition slide2 = new TranslateTransition(Duration.seconds(0.1), restartButton);
        slide2.setFromX(fromX);
        slide2.setByX(-fromX);

        TranslateTransition slide3 = new TranslateTransition(Duration.seconds(0.1), exitButton);
        slide3.setFromX(fromX);
        slide3.setByX(-fromX);

        FadeTransition fade2 = new FadeTransition(Duration.seconds(0.1), restartButton);
        fade2.setFromValue(0.0);
        fade2.setToValue(1.0);

        FadeTransition fade3 = new FadeTransition(Duration.seconds(0.1), exitButton);
        fade3.setFromValue(0.0);
        fade3.setToValue(1.0);

        FadeTransition gameOverLabelFadeIn = new FadeTransition(Duration.seconds(0.1), gameOverLabel);
        gameOverLabelFadeIn.setFromValue(0.0);
        gameOverLabelFadeIn.setToValue(1.0);

        ParallelTransition combined = new ParallelTransition(slide2, fade2, slide3, fade3, gameOverLabelFadeIn);

        combined.setOnFinished(e -> {
            gameState.isTransitionEffectsOn = false;
        });

        combined.play();
    }

    public void closeGameOverScreenEffects(GameState gameState, GameScreen gameScreen) {
        int fromX = 500;

        TranslateTransition slide2 = new TranslateTransition(Duration.seconds(0.1), restartButton);
        slide2.setFromX(0);
        slide2.setByX(fromX); // Move 200px

        TranslateTransition slide3 = new TranslateTransition(Duration.seconds(0.1), exitButton);
        slide3.setFromX(0);
        slide3.setByX(fromX); // Move 200px

        FadeTransition fade2 = new FadeTransition(Duration.seconds(0.1), restartButton);
        fade2.setFromValue(1.0);
        fade2.setToValue(0.2);

        FadeTransition fade3 = new FadeTransition(Duration.seconds(0.1), exitButton);
        fade3.setFromValue(1.0);
        fade3.setToValue(0.2);

        FadeTransition gameOverLabelFadeOut = new FadeTransition(Duration.seconds(0.1), gameOverLabel);
        gameOverLabelFadeOut.setFromValue(1.0);
        gameOverLabelFadeOut.setToValue(0.2);

        ParallelTransition combined = new ParallelTransition(slide2, fade2, slide3, fade3, gameOverLabelFadeOut);
        // Hide root node AFTER animation completes

        combined.setOnFinished(e -> {
            this.getRoot().setVisible(false);
            gameScreen.setRemoveEffects();

            gameState.isTransitionEffectsOn = false;
        });

        combined.play();
    }

    public ParallelTransition exitGameOverScreenEffects(GameState gameState) {
        int fromX = 500;

        TranslateTransition slide2 = new TranslateTransition(Duration.seconds(1.0), restartButton);
        slide2.setFromX(0);
        slide2.setByX(fromX); // Move 200px

        TranslateTransition slide3 = new TranslateTransition(Duration.seconds(1.0), exitButton);
        slide3.setFromX(0);
        slide3.setByX(fromX); // Move 200px

        FadeTransition fade2 = new FadeTransition(Duration.seconds(1.0), restartButton);
        fade2.setFromValue(1.0);
        fade2.setToValue(0.2);

        FadeTransition fade3 = new FadeTransition(Duration.seconds(1.0), exitButton);
        fade3.setFromValue(1.0);
        fade3.setToValue(0.2);

        FadeTransition gameOverLabelFadeOut = new FadeTransition(Duration.seconds(1.0), gameOverLabel);
        gameOverLabelFadeOut.setFromValue(1.0);
        gameOverLabelFadeOut.setToValue(0.2);

        ParallelTransition combined = new ParallelTransition(slide2, fade2, slide3, fade3, gameOverLabelFadeOut);
        // Hide root node AFTER animation completes

        return combined;
    }
}
