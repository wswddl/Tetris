package tetris.ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tetris.logic.TetrisManager;
import tetris.logic.GameState;
import tetris.logic.KeyInputController;

public class MainWindow extends UiPart<StackPane> {
    private static final String FXML = "MainWindow.fxml";
    private Stage primaryStage;
    private Scene mainScene;
    private TetrisManager tetrisManager;
    private GameScreen gameplayScreen;
    //private GameOverUI gameOverScreen;
    private PauseMenu pauseMenu;
    private final GaussianBlur blurEffect = new GaussianBlur(10);
    @FXML
    private StackPane gameRoot; // stack different layers (gameplay, pause menu, game over screen)

    public MainWindow(Stage primaryStage) {
        super(FXML);

        config(primaryStage);
        setIcon(primaryStage);
    }

    private void config(Stage primaryStage) {
        try {
            /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML));
            loader.setController(this);
            Parent root = loader.load();*/
            Parent root = getRoot();

            this.primaryStage = primaryStage;
            this.mainScene = new Scene(root);
            mainScene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(mainScene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setIcon(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/tetris_icon.png")));
    }
    public void show() {
        primaryStage.show();

        double minWidth = primaryStage.getWidth();
        double minHeight = primaryStage.getHeight();

        primaryStage.setMinWidth((int) minWidth);
        primaryStage.setMinHeight((int) minHeight);

        System.out.println(minWidth + "                 " + minHeight);
        //primaryStage.setMaximized(true);
    }
    public void fillInnerParts() {

        this.gameplayScreen = new GameScreen();
        this.gameRoot.getChildren().add(gameplayScreen.getRoot());


        this.pauseMenu = new PauseMenu();
        //this.gameRoot.getChildren().add(pauseMenu.getRoot());
        //gameplayScreen.getRoot().setEffect(blurEffect);
        this.gameRoot.getChildren().add(pauseMenu.getRoot());
        pauseMenu.getRoot().setVisible(false); // initially, pause screen isn't visible

        gameplayScreen.getRoot().prefWidthProperty().bind(getRoot().widthProperty());
        gameplayScreen.getRoot().prefHeightProperty().bind(getRoot().heightProperty());
        //System.out.println("main stack pane width and height " + gameRoot.prefWidth(-1) + " " + gameRoot.prefHeight(-1));
        pauseMenu.getRoot().prefWidthProperty().bind(getRoot().widthProperty());
        pauseMenu.getRoot().prefHeightProperty().bind(getRoot().heightProperty());


        //
        this.tetrisManager = new TetrisManager(gameplayScreen, pauseMenu);

        // Keyboard input handler
        GameState gameState = tetrisManager.getGameState();
        new KeyInputController(mainScene, gameState);
    }
}
