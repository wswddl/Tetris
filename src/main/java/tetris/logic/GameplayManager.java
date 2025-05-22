package tetris.logic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.GaussianBlur;
import javafx.util.Duration;
import tetris.block.Mino;
import tetris.block.MinoBlock;
import tetris.ui.GameOverScreen;
import tetris.ui.GameScreen;

import static tetris.util.TetrisConstants.*;

public class GameplayManager {

    private GameState gameState;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;

    private MinoBlock[][] inactiveBlocksArray;
    private MinoManager minoManager;

    private ActiveStateManager activeStateManager;
    private InactiveStateManager inactiveStateManager;

    private GameMetrics gameMetrics;
    private Timeline gameLoop;


    public GameplayManager(GameState gameState, GameScreen gameScreen, GameOverScreen gameOverScreen) {
        this.gameState = gameState;
        this.gameScreen = gameScreen;
        this.gameOverScreen = gameOverScreen;

        this.inactiveBlocksArray = new MinoBlock[NUM_OF_ROW][NUM_OF_COL];
        this.minoManager = new MinoManager(gameScreen, gameState, inactiveBlocksArray);

        gameMetrics = new GameMetrics();
        activeStateManager = new ActiveStateManager(gameState, gameScreen, minoManager, inactiveBlocksArray);
        inactiveStateManager = new InactiveStateManager(gameState, gameScreen, minoManager, inactiveBlocksArray, gameMetrics);
        setUpGameLoop();
    }

    private void setUpGameLoop() {
        // Create the game loop
        gameLoop = new Timeline(
            new KeyFrame(Duration.seconds(1.0 / FPS),
                e -> {
                    this.update();
                /*
                    if (!gameState.isGameOver()) {
                        this.update();
                    }*/
                }
            )
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE); // repeat forever
        gameLoop.play(); // start the loop
    }

    public void update() {
        if (gameState.isGameOver()) {
            System.out.println("gameover update");
            gameLoop.pause();
            gameScreen.getRoot().setEffect(new GaussianBlur(10));
            gameOverScreen.getRoot().setVisible(true);
        }

        Mino currentMino = minoManager.getCurrentMino();

        if (currentMino.isActive()){
            if (!gameState.isEffectOn()) {
                //***updateWhenMinoIsActiveOnly();
                activeStateManager.update();
            } else {
                // current mino is active (next round) but the effect is still going on
                // don't proceed the game (by not calling updateWhenMinoIsActiveOnly()) until the effect is over
                //System.out.println("HANDLING SPECIAL EFFECT");
                handleClearLineSpecialEffect();
            }
        } else {
            // ****updateWhenMinoIsInactiveOnly();
            inactiveStateManager.update();
        }


        gameState.incrementCounter();
        //gameState.gameCounter = gameState.gameCounter + 1;
    }

    public void handleClearLineSpecialEffect() {
        gameScreen.handleClearLineSpecialEffect(gameState.getEffectCounter());

        if (gameState.isTSpin()) {
            gameScreen.handleTSpinSpecialEffect(gameState.getEffectCounter());
        }

        gameState.incrementEffectCounter();

        if (gameState.hasReachSpecialEffectDuration()) {
            // clear cached block in ui
            gameScreen.clearEffect();

            // add the shadow when the animation is finished
            Mino currentMino = minoManager.getCurrentMino();
            gameScreen.addMinoShadowInPlayingField(currentMino);

            // reset all the flags
            gameState.resetEffectFlags();
        }
    }

    public ActiveStateManager getActiveStateManager() {
        return activeStateManager;
    }
    public void pauseGameLoop() {
        gameLoop.pause();
    }
    public void resumeGameLoop() {
        gameLoop.play();
    }
    public void restartGame() {
        gameState.restartGame();
        gameLoop.play();
        gameOverScreen.getRoot().setVisible(false);
        gameScreen.getRoot().setEffect(null);


        // clear inactive blocks array
        for (int row = 0; row < NUM_OF_ROW; row++) {
            for (int col = 0; col < NUM_OF_COL; col++) {
                inactiveBlocksArray[row][col] = null;
            }
        }


        gameScreen.restartGame();

        minoManager.initializeMinos();


        gameMetrics.setHighScore(); // set high score first
        gameMetrics.resetScore(); // then reset current score
        gameScreen.updateScore(gameMetrics.getScore());
        gameScreen.updateHighScore(gameMetrics.getHighScore());
    }
}
