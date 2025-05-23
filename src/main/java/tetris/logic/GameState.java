package tetris.logic;

import static tetris.util.TetrisConstants.*;

public class GameState {
    private boolean allowSwapMino;
    private boolean isDeactivating;
    private boolean isEffectOn; // effect is on when there is line removal
    private boolean isTSpin;
    private boolean isGameOver;
    private boolean isPaused;
    private boolean isTimesUp;


    // game counter
    private int deactivateCounter;
    private int autoDropCounter;
    private int gameCounter;
    private int effectCounter;

    public boolean isTransitionEffectsOn;


    public GameState() {
        isDeactivating = false;
        isEffectOn = false;
        isTSpin = false;
        isGameOver = false;
        isPaused = false;
        isTimesUp = false;

        this.allowSwapMino = true;

        deactivateCounter = 0;
        autoDropCounter = 0;
        gameCounter = 0;
        effectCounter = 0;

    }

    public void resetGameState() {
        isDeactivating = false;
        isEffectOn = false;
        isTSpin = false;
        isGameOver = false;
        isPaused = false;
        isTimesUp = false;

        this.allowSwapMino = true;

        deactivateCounter = 0;
        autoDropCounter = 0;
        gameCounter = 0;
        effectCounter = 0;
    }


    // =================================================
    // Flags methods
    // =================================================

    public boolean isAllowSwapMino() {
        return allowSwapMino;
    }
    public void enableSwapMino() {
        allowSwapMino = true;
    }
    public void disableSwapMino() {
        allowSwapMino = false;
    }
    public void resetDeactivation() {
        this.deactivateCounter = 0;
        this.isDeactivating = false;
    }
    public void resetEffectFlags() {
        isEffectOn = false;
        isTSpin = false;
        effectCounter = 0;
    }
    public boolean isDeactivating() {
        return isDeactivating;
    }
    public void setDeactivating() {
        isDeactivating = true;
    }
    public boolean isEffectOn() {
        return isEffectOn;
    }
    public void turnOnEffect() {
        isEffectOn = true;
    }
    public boolean isTSpin() {
        return isTSpin;
    }
    public void setIsTSpin() {
        isTSpin = true;
    }
    public boolean isTimesUp() {
        return isTimesUp;
    }
    public void setTimesUp() {
        isTimesUp = true;
    }
    public boolean isGameOver() {
        return isGameOver;
    }
    public void setGameOver() {
        isGameOver = true;
    }
    public void restartGame() {
        isPaused = false;
        isGameOver = false;
        isTimesUp = false;

        allowSwapMino = true;
    }

    public boolean isGamePaused() {
        return isPaused;
    }
    public void pauseTheGame() {
        isPaused = true;
    }
    public void resumeTheGame() {
        isPaused = false;
    }


    // =================================================
    // Counters methods
    // =================================================

    public void incrementCounter() {
        gameCounter++;
    }
    public void incrementDeactivationCounter() {
        deactivateCounter++;
    }
    public boolean hasReachedDeactivateInterval() {
        return deactivateCounter == DEACTIVATE_INTERVAL;
    }
    public void incrementAutoDropCounter() {
        autoDropCounter++;
    }
    public boolean hasReachAutoDropInterval() {
        return autoDropCounter == AUTO_DROP_INTERVAL;
    }
    public void resetAutoDropCounter() {
        autoDropCounter = 0;
    }
    public void incrementEffectCounter() {
        effectCounter++;
    }
    public int getEffectCounter() {
        return effectCounter;
    }
    public boolean hasReachSpecialEffectDuration() {
        return effectCounter > SPECIAL_EFFECT_DURATION;
    }







}
