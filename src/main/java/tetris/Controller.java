package tetris;

import tetris.block.Block;
import tetris.block.Mino;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import tetris.block.MinoL;

import java.util.ArrayList;

import static tetris.util.TetrisConstants.*;

public class Controller {
    @FXML
    private Pane playingField;
    private ArrayList<Block> inactiveBlocks;
    private Block[][] inactiveBlocksArray; // only keep track of inactive blocks, not including active blocks
    public Mino currentMino;
    private Mino nextMino;
    private Mino holdMino;



    // Signals for the game logic
    private boolean leftCollision, rightCollision, bottomCollision;
    private boolean allowSwapMino;
    private boolean isDeactivating;
    private int deactivateCounter;
    public boolean isGameOver;
    public boolean isTimesUp;

    // game counter
    private int autoDropCounter;
    private int gameCounter;
    private final int GAME_DURATION = 2 * 60 * FPS; // 2 minutes gameplay

    /**
     * Is called by loader.load()
     */
    public void initialize() {
        this.inactiveBlocks = new ArrayList<>();
        this.inactiveBlocksArray = new Block[NUM_OF_ROW][NUM_OF_COL];

        this.currentMino = new MinoL();

        this.autoDropCounter = 0;
        this.gameCounter = 0;



        System.out.println("initializing");
        addMinoToPlayingField(currentMino);
    }

    /**
     * Gets invoke by {@Code Tetris} gameLoop every time interval.
     * Updates the Minos and other UI components every time interval.
     */
    public void update() {
        System.out.println("update :)");

        if (currentMino.isActive()) {
            if (isDeactivating) {
                startDeactivatingCurrentMino();
            }
            // auto drop
            if (false) {
                this.isDeactivating = true;
                //this.isActive = false;
            } else {
                this.autoDropCounter++;
                if (autoDropCounter == AUTO_DROP_INTERVAL) {
                    currentMino.moveDown();
                    autoDropCounter = 0;
                }
            }
        }



        gameCounter++;
    }

    public void addMinoToPlayingField(Mino mino) {
        for (Block block : mino.blocks) {
            playingField.getChildren().add(block.getRectangle());
        }
    }

    public void startDeactivatingCurrentMino() {
        deactivateCounter++;
        if (deactivateCounter == FPS) {
            deactivateCounter = 0;

            // check if the mino is still hitting the bottom after 1 second
            // if still hitting the bottom, deactivate it
            // else reset the deactivate counter
            /////////////////////////////////////////checkCurrentMinoMovementCollision();
            if (bottomCollision) {
                currentMino.deactivate();
            } else {
                deactivateCounter = 0;
            }

        }
    }



}
