package tetris.logic;

import tetris.block.Mino;
import tetris.block.MinoBlock;
import tetris.ui.GameScreen;

import static tetris.util.TetrisConstants.*;

public class GameplayManager {

    private GameState gameState;
    private GameScreen gameplayUI;

    private MinoBlock[][] inactiveBlocksArray;
    private MinoManager minoManager;

    private ActiveStateManager activeStateManager;
    private InactiveStateManager inactiveStateManager;

    private GameMetrics gameMetrics;


    public GameplayManager(GameState gameState, GameScreen gameplayUI) {
        this.gameState = gameState;
        this.gameplayUI = gameplayUI;

        this.inactiveBlocksArray = new MinoBlock[NUM_OF_ROW][NUM_OF_COL];
        this.minoManager = new MinoManager(gameplayUI, gameState, inactiveBlocksArray);

        gameMetrics = new GameMetrics();
        activeStateManager = new ActiveStateManager(gameState, gameplayUI, minoManager, inactiveBlocksArray);
        inactiveStateManager = new InactiveStateManager(gameState, gameplayUI, minoManager, inactiveBlocksArray, gameMetrics);

    }

    public void update() {
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
        gameplayUI.handleClearLineSpecialEffect(gameState.getEffectCounter());

        if (gameState.isTSpin()) {
            gameplayUI.handleTSpinSpecialEffect(gameState.getEffectCounter());
        }

        gameState.incrementEffectCounter();

        if (gameState.hasReachSpecialEffectDuration()) {
            // clear cached block in ui
            gameplayUI.clearEffect();

            // add the shadow when the animation is finished
            Mino currentMino = minoManager.getCurrentMino();
            gameplayUI.addMinoShadowInPlayingField(currentMino);

            // reset all the flags
            gameState.resetEffectFlags();
        }
    }
}
