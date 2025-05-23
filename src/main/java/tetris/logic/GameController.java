package tetris.logic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.GaussianBlur;
import javafx.util.Duration;
import tetris.ui.GameOverScreen;
import tetris.ui.GameScreen;
import tetris.ui.PauseMenuScreen;

import static tetris.util.TetrisConstants.FPS;

public class GameController {

    // model manager
    private GameplayManager gameplayManager;

    // ui
    private GameScreen gameScreen;
    private PauseMenuScreen pauseMenuScreen;
    private GameOverScreen gameOverScreen;
    // game state
    private GameState gameState;
    private Timeline gameLoop;


    // game metrics


    // =================================================
    // Initialize the controller
    // =================================================

    /**
     * Is called by loader.load()
     */
    //public void initialize() {
    public GameController(GameScreen gameScreen, PauseMenuScreen pauseMenuScreen, GameOverScreen gameOverScreen) {
        //ui = new UiManager(playingField, nextMinoBox, holdMinoBox);
        this.gameScreen = gameScreen;
        this.pauseMenuScreen = pauseMenuScreen;
        this.gameOverScreen = gameOverScreen;

        this.gameState = new GameState();

        this.gameplayManager = new GameplayManager(gameState, gameScreen);
        setUpGameLoop();
    }
    private void setUpGameLoop() {
        // Create the game loop
        gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(1.0 / FPS),
                        e -> {
                            this.update();
                        }
                )
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE); // repeat forever
        gameLoop.play(); // start the loop
    }

    public void update() {
        if (gameState.isGameOver()) {
            gameLoop.pause();
            gameScreen.getRoot().setEffect(new GaussianBlur(10));
            gameOverScreen.getRoot().setVisible(true);
        } else {
            gameplayManager.update();
        }
    }


    public GameState getGameState() {
        return this.gameState;
    }


    public void pauseGame() {
        gameState.pauseTheGame();
        gameLoop.pause();

        gameScreen.getRoot().setEffect(new GaussianBlur(10));
        pauseMenuScreen.getRoot().setVisible(true);
    }
    public void resumeGame() {
        gameState.resumeTheGame();
        gameLoop.play();

        gameScreen.getRoot().setEffect(null); // remove blur
        pauseMenuScreen.getRoot().setVisible(false);
    }

    public void restartGame() {
        gameOverScreen.getRoot().setVisible(false);
        gameScreen.getRoot().setEffect(null);

        gameLoop.play();
        gameplayManager.restartGame();
    }

    public void restartGameInPauseMenu() {
        pauseMenuScreen.getRoot().setVisible(false);
        gameScreen.getRoot().setEffect(null);

        gameLoop.play();
        gameplayManager.restartGame();
    }




    // =================================================
    // Update the game
    // =================================================





    public GameplayManager getGameplayManager() {
        return gameplayManager;
    }





}
