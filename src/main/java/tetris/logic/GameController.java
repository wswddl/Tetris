package tetris.logic;

import javafx.scene.effect.GaussianBlur;
import tetris.ui.GameOverScreen;
import tetris.ui.GameScreen;
import tetris.ui.PauseMenuScreen;

public class GameController {

    // model manager
    private GameplayManager gameplayManager;

    // ui
    private GameScreen gameScreen;
    private PauseMenuScreen pauseMenuScreen;
    private GameOverScreen gameOverScreen;
    // game state
    private GameState gameState;


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

        this.gameplayManager = new GameplayManager(gameState, gameScreen, gameOverScreen);
    }
    public GameState getGameState() {
        return this.gameState;
    }


    public void pauseGame() {
        gameState.pauseTheGame();
        gameplayManager.pauseGameLoop();

        gameScreen.getRoot().setEffect(new GaussianBlur(10));
        pauseMenuScreen.getRoot().setVisible(true);
    }
    public void resumeGame() {
        gameState.resumeTheGame();
        gameplayManager.resumeGameLoop();

        gameScreen.getRoot().setEffect(null); // remove blur
        pauseMenuScreen.getRoot().setVisible(false);
    }

    public void restartGame() {
        gameplayManager.restartGame();
    }




    // =================================================
    // Update the game
    // =================================================

    public void update() {
        if (gameState.isGameOver()) {
            //gameLoop.pause();
            /*
            if (KeyInputHandler.restartPress) {
                // reset the game over flag
                gameState.restartGame();
                // handle restart game logic
            }*/

        }
/*
        else if (gameState.isGamePaused()) { // game has been paused
            if (KeyInputHandler.escapePress || KeyInputHandler.resumePress|| pauseMenuUI.isResumeButtonClicked()) {
                // resume the game
                gameState.resumeTheGame();
                // handle resume
                pauseController.handleResumePress();
            }
        }
*//*
        else if (!gameState.isGamePaused()) { // game isn't paused
            if (KeyInputController.escapePress) {
                // pause the game
                gameState.pauseTheGame();
                // handle pause
                pauseController.handlePausePress();

                //gameLoop.pause();

            } else {
                gameplayManager.update();
            }
        }
        KeyInputController.resetPressFlags();
        KeyInputController.minoAction = null;

/*

        if (!KeyInputHandler.pausePress && !KeyInputHandler.resumePress && !isGameOver) {
            gameplayController.update();
        } else if (KeyInputHandler.pausePress && !isGameOver) {
            KeyInputHandler.ignoreMovement();
            pauseController.handlePausePress();
            //TetrisPanel.OST.pause(); handled in pausecontroller
        } else if (KeyInputHandler.resumePress && !isGameOver) {
            //TetrisPanel.OST.resume(); handled in pausecontroller
            // KeyInputHandler.resumePress = false;
            pauseController.handleResumePress();
        } else if (isGameOver && KeyInputHandler.restartPress) {
            // restart();
        /*} else if (TetrisFunctions.isTimesUp && KeyInputHandler.restartPressForTimesUp) {
            tf.restart();*//*
        } else { // gameOver and NO restart
            KeyInputHandler.ignoreMovement();
            KeyInputHandler.pausePress = false;
        }*/

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    public GameplayManager getGameplayManager() {
        return gameplayManager;
    }





}
