package tetris.logic;

public class GameMetrics {
    private final int BASIC_SCORE = 10;
    private int score;
    private int highScore;
    private int level;
    private int combo;

    public GameMetrics() {
        this.score = 0;
        this.level = 1;
        this.highScore = 0;
        this.combo = 0;
    }

    public void updateScore(int numLinesClear, boolean isTSpin) {
        if (numLinesClear <= 0) {
            combo = 0; // reset combo
        } else {
            if (isTSpin) {
                numLinesClear = 6;
            }
            // Non-linearity:
            // more lines cleared in one go -> more score awarded
            // higher combo -> more score awarded
            this.score += numLinesClear * (BASIC_SCORE + numLinesClear + combo*combo*combo);
            combo++;

        }
    }
    public int getCombo() {
        return combo;
    }
    public int getLevel() {
        return level;
    }
    public int getScore() {
        return score;
    }
    public void resetScore() {
        score = 0;
    }
    public int getHighScore() {
        return highScore;
    }
    public void setHighScore() {
        if (score > highScore) {
            highScore = score;
        }
    }
}
