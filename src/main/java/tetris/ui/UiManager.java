package tetris.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tetris.block.Block;
import tetris.block.Mino;
import tetris.block.MinoBlock;

import java.util.ArrayList;

import static tetris.util.TetrisConstants.*;

public class UiManager {
    private static final String FXML = "/view/Tetris.fxml";
    private Stage primaryStage;
    private Scene gameScene;
    @FXML
    private Pane playingField;
    @FXML
    private Pane nextMinoBox;
    @FXML
    private Pane holdMinoBox;

    // Graphic contexts
    private GraphicsContext playingFieldGC; // Draw the grid background in the playing field
    private GraphicsContext nextBoxGC; // Draw the minos in the next box
    private GraphicsContext holdBoxGC; // Draw the minos in the hold box
    private GraphicsContext blockGC; // Draw the minos and blocks in the playing field
    private GraphicsContext shadowGC; // Draw the minos' shadow in the playing field

    // Keeps data for animation
    private ArrayList<MinoBlock> fadingBlocks;
    private ArrayList<MinoBlock> fallingBlocks;
    private ArrayList<Integer> numLinesFallList;

    public Parent root;

    // =================================================
    // Set up UI
    // =================================================

    public UiManager(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML));
            loader.setController(this);
            Parent root = loader.load();

            this.primaryStage = primaryStage;
            this.gameScene = new Scene(root);
            gameScene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(gameScene);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void show() {
        primaryStage.show();
    }
    public void fillInnerParts() {
        // Draw the grid background in the playing field
        Canvas playingFieldCanvas = new Canvas(PLAYING_FIELD_WIDTH, PLAYING_FIELD_HEIGHT);
        playingFieldGC = playingFieldCanvas.getGraphicsContext2D();

        // Draw the minos in the next box
        Canvas nextBoxCanvas = new Canvas(NEXT_BOX_HEIGHT_WIDTH, NEXT_BOX_HEIGHT_WIDTH);
        nextBoxGC = nextBoxCanvas.getGraphicsContext2D();

        // Draw the minos in the hold box
        Canvas holdBoxCanvas = new Canvas(HOLD_BOX_HEIGHT_WIDTH, HOLD_BOX_HEIGHT_WIDTH);
        holdBoxGC = holdBoxCanvas.getGraphicsContext2D();

        // Draw the minos and blocks in the playing field
        Canvas blockCanvas = new Canvas(PLAYING_FIELD_WIDTH, PLAYING_FIELD_HEIGHT);
        blockGC = blockCanvas.getGraphicsContext2D();
        blockCanvas.getGraphicsContext2D().setImageSmoothing(false); // disable anti-aliasing

        // Draw the minos' shadow in the playing field
        Canvas shadowCanvas = new Canvas(PLAYING_FIELD_WIDTH, PLAYING_FIELD_HEIGHT);
        shadowGC = shadowCanvas.getGraphicsContext2D();
        shadowCanvas.getGraphicsContext2D().setImageSmoothing(false); // disable anti-aliasing

        // follow the hierarchical order of drawing: playing field grid -> shadow -> mino
        playingField.getChildren().add(playingFieldCanvas);
        playingField.getChildren().add(shadowCanvas);
        playingField.getChildren().add(blockCanvas);

        nextMinoBox.getChildren().add(nextBoxCanvas);
        holdMinoBox.getChildren().add(holdBoxCanvas);

        // keeps the blocks for animation (fading and falling)
        fadingBlocks = new ArrayList<>();
        fallingBlocks = new ArrayList<>();
        numLinesFallList = new ArrayList<>();
    }
    public Scene getGameScene() {
        return gameScene;
    }


    // =================================================
    // Drawing
    // =================================================

    public void drawPlayingFieldGrid() {
        int margin = 2;
        int trueSize = BLOCK_SIZE - 2 * margin;
        int gridCount = 0;

        for (int pixelY = TOPMOST_Y; pixelY < PLAYING_FIELD_HEIGHT; pixelY += BLOCK_SIZE) {
            gridCount++;  // trick the counter (odd even switch)

            for (int pixelX = LEFTMOST_PIXEL; pixelX < PLAYING_FIELD_WIDTH; pixelX += BLOCK_SIZE) {
                if (gridCount % 2 == 1) {
                    playingFieldGC.setFill(PLAYING_FIELD_GRID_LIGHT_GREY);
                } else {
                    playingFieldGC.setFill(PLAYING_FIELD_GRID_GREY);
                }
                playingFieldGC.fillRect(pixelX + margin, pixelY + margin, trueSize, trueSize);
                gridCount++;
            }
        }
    }

    public void addMinoInPlayingField(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawAdd(blockGC);
        }
    }
    public void removeMinoInPlayingField(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawRemove(blockGC);
        }
    }
    public void addMinoShadowInPlayingField(Mino mino) {
        for (Block block : mino.shadowBlocks) {
            block.drawAdd(shadowGC);
        }
    }
    public void removeMinoShadowInPlayingField(Mino mino) {
        for (Block block : mino.shadowBlocks) {
            block.drawRemove(shadowGC);
        }
    }

    /**
     * Invoked when mino is deactivated to prevent bug (shadow not being removed in UI).
     */
    public void clearAllShadowInPlayingField() {
        shadowGC.clearRect(LEFTMOST_PIXEL, TOPMOST_PIXEL, PLAYING_FIELD_WIDTH, PLAYING_FIELD_HEIGHT);
    }

    public void addMinoInNextBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawAdd(nextBoxGC);
        }
    }
    public void removeMinoInNextBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawRemove(nextBoxGC);
        }
    }

    public void addMinoInHoldBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawAdd(holdBoxGC);
        }
    }
    public void removeMinoInHoldBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawRemove(holdBoxGC);
        }
    }

    // =================================================
    // Effects
    // =================================================

    public void handleClearLineSpecialEffect(int effectCounter, int EFFECT_DURATION) {

        if (effectCounter >= 0 && effectCounter < BLOCK_FADING_DURATION) {
            int workingCounter = effectCounter;
            for (MinoBlock fadingBlock : fadingBlocks) {
                fadingBlock.drawFading(blockGC, workingCounter);
            }
        }


        if (effectCounter >= BLOCK_FADING_DURATION && effectCounter < BLOCK_FALLING_DURATION + BLOCK_FADING_DURATION) {

            int workingCounter = effectCounter - BLOCK_FADING_DURATION;

            for (int i = 0; i < fallingBlocks.size(); i++) {
                MinoBlock fallingBlock = fallingBlocks.get(i);
                int numLinesFall = numLinesFallList.get(i);

                fallingBlock.drawFalling(blockGC, workingCounter, numLinesFall);
            }
        }
    }

    public void addFadingBlock(MinoBlock minoBlock) {
        fadingBlocks.add(minoBlock);
    }
    public void addFallingBlock(MinoBlock minoBlock, int numLinesFall) {
        fallingBlocks.add(minoBlock);
        numLinesFallList.add(numLinesFall);
    }
    public void clear() {
        fadingBlocks.clear();
        fallingBlocks.clear();
        numLinesFallList.clear();
    }
}
