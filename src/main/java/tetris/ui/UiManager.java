package tetris.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import tetris.block.Block;
import tetris.block.Mino;

import static tetris.util.TetrisConstants.*;

public class UiManager {
    private Pane playingField;
    private Pane nextBox;
    private Pane holdBox;

    private Canvas playingFieldCanvas;
    private GraphicsContext playingFieldGC;
    private Canvas nextBoxCanvas;
    private GraphicsContext nextBoxGC;
    private Canvas holdBoxCanvas;
    private GraphicsContext holdBoxGC;
    private Canvas blockCanvas;
    private GraphicsContext blockGC;
    private Canvas shadowCanvas;
    private GraphicsContext shadowGC;

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

    }

    public void addMinoToPlayingField(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawAdd(playingFieldGC);
        }
    }

    public void removeMinoFromPlayingField(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawRemovePrevPosition(playingFieldGC);;
        }
    }

    public void addMinoToNextBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawAdd(nextBoxGC);
        }
    }

    public void removeMinoFromNextBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawRemovePrevPosition(nextBoxGC);;
        }
    }

    public void addMinoToHoldBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawAdd(holdBoxGC);
        }
    }

    public void removeMinoFromHoldBox(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawRemovePrevPosition(holdBoxGC);;
        }
    }

    public void moveMinoInPlayingField(Mino mino) {
        for (Block block : mino.blocks) {
            block.drawMove(playingFieldGC);;
        }
    }

    public void
}
