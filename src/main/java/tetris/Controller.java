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

        addMinoToPlayingField(currentMino);
    }

    public void addMinoToPlayingField(Mino mino) {
        for (Block block : mino.blocks) {
            playingField.getChildren().add(block.getRectangle());
        }
    }

    /**
     * Gets invoke by {@Code Tetris} gameLoop every time interval.
     * Updates the Minos and other UI components every time interval.
     */
    public void update() {

        if (currentMino.isActive()) {
            updateWhenMinoIsActiveOnly();
        }
        if (gameCounter % 120 == 0) {
            System.out.println("leftcollision = " + leftCollision);
            System.out.println("rightcollision = " + rightCollision);
            System.out.println("bottomcollision = " + bottomCollision);


        }


        gameCounter++;
    }

    public void updateWhenMinoIsActiveOnly() {
        if (isDeactivating) {
            this.startDeactivatingCurrentMino();
        }

        this.checkCurrentMinoMovementCollision();




        // auto drop
        this.autoDropMechanism();
    }

    public void startDeactivatingCurrentMino() {
        deactivateCounter++;
        if (deactivateCounter == FPS) {
            deactivateCounter = 0;

            // check if the mino is still hitting the bottom after 1 second
            // if still hitting the bottom, deactivate it
            // else reset the deactivate counter
            this.checkCurrentMinoMovementCollision();
            if (bottomCollision) {
                currentMino.deactivate();
            } else {
                deactivateCounter = 0;
            }

        }
    }

    public void autoDropMechanism() {
        if (bottomCollision) {
            this.isDeactivating = true;
        } else {
            this.autoDropCounter++;
            if (autoDropCounter == AUTO_DROP_INTERVAL) {
                currentMino.moveDown();
                autoDropCounter = 0;
            }
        }
    }


    /**
     * Check if the mino collides with the border or any inactive blocks.
     * <p>A collision means that the mino is "in contact" with the border/inactive blocks. </>
     */
    public void checkCurrentMinoMovementCollision() {
        // reset signal
        leftCollision = rightCollision = bottomCollision = false;

        for (int i = 0; i < NUM_OF_BLOCKS_PER_MINO; i++) {
            Block block = currentMino.blocks[i];
            // left collision
            if (block.getCol() <= LEFTMOST_X) {
                this.leftCollision = true;
            }
            // right collision
            if (block.getCol() >= RIGHTMOST_X) {
                this.rightCollision = true;
            }
            // bottom collision
            if (block.getRow() >= BOTTOMMOST_Y) {
                this.bottomCollision = true;
            }

        }
    }



}
