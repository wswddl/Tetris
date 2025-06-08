package tetris.logic;

import javafx.animation.*;
import javafx.scene.effect.GaussianBlur;
import javafx.util.Duration;
import tetris.ui.*;

import static tetris.util.TetrisConstants.FPS;

public class GameController {

    // model manager
    private GameplayManager gameplayManager;

    // ui
    private MainWindow mainWindow;
    private GameScreen gameScreen;
    private PauseMenuScreen pauseMenuScreen;
    private GameOverScreen gameOverScreen;
    private StartMenuScreen startMenuScreen;
    // game state
    private GameState gameState;
    private Timeline gameLoop;

    private boolean isIgnoreKeyInput;



    // game metrics


    // =================================================
    // Initialize the controller
    // =================================================

    /**
     * Is called by loader.load()
     */
    //public void initialize() {
    public GameController(GameScreen gameScreen, PauseMenuScreen pauseMenuScreen, GameOverScreen gameOverScreen,
                          StartMenuScreen startMenuScreen, MainWindow mainWindow) {
        //ui = new UiManager(playingField, nextMinoBox, holdMinoBox);
        this.gameScreen = gameScreen;
        this.pauseMenuScreen = pauseMenuScreen;
        this.gameOverScreen = gameOverScreen;
        this.startMenuScreen = startMenuScreen;

        this.mainWindow = mainWindow;

        this.gameState = new GameState();

        this.gameplayManager = new GameplayManager(gameState, gameScreen);
        setUpGameLoop();

        this.isIgnoreKeyInput = true;
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
        // dont start yet until play button!!!! gameLoop.play(); // start the loop
    }

    public void update() {
        if (gameState.isGameOver()) {
            gameLoop.pause();

            gameOverScreen.openGameOverScreenEffects(gameState, gameScreen);
            //gameScreen.getRoot().setEffect(new GaussianBlur(10));
            //gameOverScreen.getRoot().setVisible(true);
        } else {
            gameplayManager.update();
        }
    }


    public GameState getGameState() {
        return this.gameState;
    }


    public void pauseGame() {
        if (gameState.isTransitionEffectsOn) {
            return;
        }
        gameState.isTransitionEffectsOn = true;

        gameState.pauseTheGame();
        gameLoop.pause();

        pauseMenuScreen.openPauseMenuEffects(gameState, gameScreen);
        //gameScreen.getRoot().setEffect(new GaussianBlur(10));
        //pauseMenuScreen.getRoot().setVisible(true);
    }
    public void resumeGame() {
        if (gameState.isTransitionEffectsOn) {
            return;
        }
        gameState.isTransitionEffectsOn = true;

        // gameScreen.getRoot().setEffect(null); // remove blur
        ParallelTransition combined = pauseMenuScreen.closePauseMenuEffects(gameState, gameScreen);

        Animation gameScreenRemoveBlur = gameScreen.setRemoveEffects();
        combined.getChildren().add(gameScreenRemoveBlur);

        combined.setOnFinished(e -> {
            pauseMenuScreen.getRoot().setVisible(false);
            //gameScreen.setRemoveEffects();

            // only resume the game logic after the animation is finished
            gameState.resumeTheGame();
            gameLoop.play();

            gameState.isTransitionEffectsOn = false;
        });

        combined.play();
        //pauseMenuScreen.getRoot().setVisible(false);
    }

    public void restartGame() {
        if (gameState.isTransitionEffectsOn) {
            return;
        }
        gameState.isTransitionEffectsOn = true;

        // transition effects
        gameOverScreen.closeGameOverScreenEffects(gameState, gameScreen);
        //gameOverScreen.getRoot().setVisible(false);
        //gameScreen.getRoot().setEffect(null);

        // restart model
        gameLoop.play();
        gameplayManager.restartGame();
    }

    public void restartGameInPauseMenu() {
        if (gameState.isTransitionEffectsOn) {
            return;
        }
        gameState.isTransitionEffectsOn = true;

        // transition effects
        ParallelTransition combined = pauseMenuScreen.closePauseMenuEffects(gameState, gameScreen);

        Animation gameScreenRemoveBlur = gameScreen.setRemoveEffects();

        combined.getChildren().add(gameScreenRemoveBlur);

        combined.setOnFinished(e -> {
            pauseMenuScreen.getRoot().setVisible(false);
            //gameScreen.setRemoveEffects();

            gameState.isTransitionEffectsOn = false;

            // restart model
            // only start the game after animation is finished
            gameLoop.play();
            gameplayManager.restartGame();
        });

        combined.play();
        //pauseMenuScreen.getRoot().setVisible(false);
        //gameScreen.getRoot().setEffect(null);


    }
    public void exitButtonInGameOver() {
        // synchronization flags
        if (gameState.isTransitionEffectsOn) {
            return;
        }
        gameState.isTransitionEffectsOn = true;
        this.isIgnoreKeyInput = false;

        // transition effects
        mainWindow.addNodesToRoot(startMenuScreen.getRoot());

        ParallelTransition combined = gameOverScreen.exitGameOverScreenEffects(gameState);

        FadeTransition fadeOutGameScreen = new FadeTransition(Duration.seconds(1.0), gameScreen.getRoot());
        fadeOutGameScreen.setToValue(0.0);
        //combined.getChildren().add(fadeOutGameScreen);

        FadeTransition fadeOutGameOverScreen = new FadeTransition(Duration.seconds(1.0), gameOverScreen.getRoot());
        fadeOutGameOverScreen.setToValue(0.0);
        //combined.getChildren().add(fadeOutGameOverScreen);

        FadeTransition fadeInStartMenuScreen = new FadeTransition(Duration.seconds(1.0), startMenuScreen.getRoot());
        fadeInStartMenuScreen.setFromValue(0.0);
        fadeInStartMenuScreen.setToValue(1.0);
        combined.getChildren().add(fadeInStartMenuScreen);

        Animation gameScreenRemoveBlur = gameScreen.setRemoveEffects();
        combined.getChildren().add(gameScreenRemoveBlur);

        combined.setOnFinished(e -> {

            // handle nodes
            mainWindow.removeNodesFromRoot(pauseMenuScreen.getRoot(), gameScreen.getRoot(), gameOverScreen.getRoot());

            gameState.isTransitionEffectsOn = false;
        });

        combined.play();

    }
    public void exitButtonInPauseMenu() {
        // synchronization flags
        if (gameState.isTransitionEffectsOn) {
            return;
        }
        gameState.isTransitionEffectsOn = true;
        this.isIgnoreKeyInput = true;

        // transition effects
        mainWindow.addNodesToRoot(startMenuScreen.getRoot());

        ParallelTransition combined = pauseMenuScreen.exitPauseMenuEffects(gameState);

        FadeTransition fadeOutGameScreen = new FadeTransition(Duration.seconds(1.0), gameScreen.getRoot());
        fadeOutGameScreen.setToValue(0.0);
        //combined.getChildren().add(fadeOutGameScreen);

        FadeTransition fadeOutGameOverScreen = new FadeTransition(Duration.seconds(1.0), pauseMenuScreen.getRoot());
        fadeOutGameOverScreen.setToValue(0.0);
        //combined.getChildren().add(fadeOutGameOverScreen);

        FadeTransition fadeInStartMenuScreen = new FadeTransition(Duration.seconds(1.0), startMenuScreen.getRoot());
        fadeInStartMenuScreen.setFromValue(0.0);
        fadeInStartMenuScreen.setToValue(1.0);
        combined.getChildren().add(fadeInStartMenuScreen);

        Animation gameScreenRemoveBlur = gameScreen.setRemoveEffects();
        combined.getChildren().add(gameScreenRemoveBlur);

        combined.setOnFinished(e -> {
            //gameScreen.setRemoveEffects();

            // handle nodes
            mainWindow.removeNodesFromRoot(pauseMenuScreen.getRoot(), gameScreen.getRoot(), gameOverScreen.getRoot());

            gameState.isTransitionEffectsOn = false;
        });

        combined.play();
    }
    public void playButtonInStartMenu() {
        if (gameState.isTransitionEffectsOn) {
            return;
        }
        gameState.isTransitionEffectsOn = true;
        this.isIgnoreKeyInput = false;

        // set new background for gameScreen
        gameScreen.setRandomBackGroundImage();

        // transition effects
        mainWindow.addNodesToRoot(gameScreen.getRoot(), pauseMenuScreen.getRoot(), gameOverScreen.getRoot());
        gameScreen.getRoot().setEffect(null); // clear the blur effects just in case.
        pauseMenuScreen.getRoot().setVisible(false);
        gameOverScreen.getRoot().setVisible(false);

        ParallelTransition combined = startMenuScreen.closeStartMenuEffects(gameState);

        FadeTransition fadeOutStartMenu = new FadeTransition(Duration.seconds(1.0), startMenuScreen.getRoot());
        fadeOutStartMenu.setToValue(0.0);
        //combined.getChildren().add(fadeOutStartMenu);

        FadeTransition fadeInGameScreen = new FadeTransition(Duration.seconds(1.0), gameScreen.getRoot());
        fadeInGameScreen.setFromValue(0.0);
        fadeInGameScreen.setToValue(1.0);
        combined.getChildren().add(fadeInGameScreen);

        pauseMenuScreen.getRoot().setOpacity(1.0);
        gameOverScreen.getRoot().setOpacity(1.0);

        combined.setOnFinished(e -> {
            //startMenuScreen.getRoot().setOpacity(1.0); // reset opacity after fading effect
            // handle nodes
            mainWindow.removeNodesFromRoot(startMenuScreen.getRoot());

            gameState.isTransitionEffectsOn = false;

            // start model only after the animation is finished
            gameLoop.play();
        });
        // setup the model before animation finish !!!
        gameplayManager.restartGame();
        combined.play();




    }






    public GameplayManager getGameplayManager() {
        return gameplayManager;
    }

    public boolean isIgnoreKeyInput() {
        return isIgnoreKeyInput;
    }





}
