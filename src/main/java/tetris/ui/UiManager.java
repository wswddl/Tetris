package tetris.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import tetris.block.Block;
import tetris.block.Mino;
import tetris.block.MinoBlock;

import java.util.ArrayList;

import static tetris.util.TetrisConstants.*;

public class UiManager {
    private Pane playingField;
    private Pane nextBox;
    private Pane holdBox;

    private Canvas playingFieldCanvas; // Draw the grid background in the playing field
    private GraphicsContext playingFieldGC;
    private Canvas nextBoxCanvas; // Draw the minos in the next box
    private GraphicsContext nextBoxGC;
    private Canvas holdBoxCanvas; // Draw the minos in the hold box
    private GraphicsContext holdBoxGC;
    private Canvas blockCanvas; // Draw the minos and blocks in the playing field
    private GraphicsContext blockGC;
    private Canvas shadowCanvas; // Draw the minos' shadow in the playing field
    private GraphicsContext shadowGC;

    // Fading blocks
    public ArrayList<MinoBlock> fadingBlocks;
    public ArrayList<MinoBlock> fallingBlocks;
    private ArrayList<Integer> numLinesFallList;

    public UiManager(Pane playingField, Pane nextBox, Pane holdBox) {
        this.playingField = playingField;
        this.nextBox = nextBox;
        this.holdBox = holdBox;

        playingFieldCanvas = new Canvas(PLAYING_FIELD_WIDTH, PLAYING_FIELD_HEIGHT);
        playingFieldGC = playingFieldCanvas.getGraphicsContext2D();

        nextBoxCanvas = new Canvas(NEXT_BOX_HEIGHT_WIDTH, NEXT_BOX_HEIGHT_WIDTH);
        nextBoxGC = nextBoxCanvas.getGraphicsContext2D();

        holdBoxCanvas = new Canvas(HOLD_BOX_HEIGHT_WIDTH, HOLD_BOX_HEIGHT_WIDTH);
        holdBoxGC = holdBoxCanvas.getGraphicsContext2D();

        blockCanvas = new Canvas(PLAYING_FIELD_WIDTH, PLAYING_FIELD_HEIGHT);
        blockGC = blockCanvas.getGraphicsContext2D();

        shadowCanvas = new Canvas(PLAYING_FIELD_WIDTH, PLAYING_FIELD_HEIGHT);
        shadowGC = shadowCanvas.getGraphicsContext2D();

        // follow the hierarchical order of drawing: playing field grid -> shadow -> mino
        playingField.getChildren().add(playingFieldCanvas);
        playingField.getChildren().add(shadowCanvas);
        playingField.getChildren().add(blockCanvas);

        nextBox.getChildren().add(nextBoxCanvas);
        holdBox.getChildren().add(holdBoxCanvas);

        fadingBlocks = new ArrayList<>();
        fallingBlocks = new ArrayList<>();
        numLinesFallList = new ArrayList<>();
    }


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
    public void removeMinoFromPlayingField(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawRemove(blockGC);
        }
    }
    public void addMinoShadowInPlayingField(Mino mino) {
        for (Block block : mino.shadowBlocks) {
            block.drawAdd(shadowGC);
        }
    }
    public void removeMinoShadowFromPlayingField(Mino mino) {
        for (Block block : mino.shadowBlocks) {
            block.drawRemove(shadowGC);
        }
    }

    public void addMinoInNextBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawAdd(nextBoxGC);
        }
    }
    public void removeMinoFromNextBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawRemove(nextBoxGC);
        }
    }

    public void addMinoInHoldBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawAdd(holdBoxGC);
        }
    }
    public void removeMinoFromHoldBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawRemove(holdBoxGC);
        }
    }
    public void fadeBlock(Block block) {
        block.drawRemove(blockGC);
    }
    public void removeBlock(Block block) {
        block.drawRemove(blockGC);
    }
    public void addBlock(Block block) {
        block.drawAdd(blockGC);
    }

    // =================================================
    // Effects
    // =================================================

    public void handleClearLineSpecialEffect(int effectCounter, int EFFECT_DURATION) {

        if (effectCounter >= 0 && effectCounter < BLOCK_FADING_DURATION) {
            int workingCounter = effectCounter;
            for (MinoBlock fadingBlock : fadingBlocks) {
                fadingBlock.drawFading(blockGC, workingCounter);
                //fadingBlock.drawRemove(blockGC);
            }
            //System.out.println("fading block counter " + workingCounter);
            //fadingBlock.drawFading(blockGC, workingCounter);
        }


        if (effectCounter >= BLOCK_FADING_DURATION && effectCounter < BLOCK_FALLING_DURATION + BLOCK_FADING_DURATION) {

            int workingCounter = effectCounter - BLOCK_FADING_DURATION;

            for (int i = 0; i < fallingBlocks.size(); i++) {
                MinoBlock fallingBlock = fallingBlocks.get(i);
                int numLinesFall = numLinesFallList.get(i);

                fallingBlock.drawFalling(blockGC, workingCounter, numLinesFall);
            }
            //System.out.println("fallingnggggggggg block counter " + workingCounter);
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
    }
}
