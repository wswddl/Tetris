package tetris.ui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tetris.logic.Controller;

public class MainWindow extends UiPart<StackPane> {
    private static final String FXML = "MainWindow.fxml";
    private Stage primaryStage;
    private Scene mainScene;
    private Controller controller;
    private GameplayUI gameplayScreen;
    //private GameOverUI gameOverScreen;
    private PauseMenuUI pauseScreen;
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

        this.gameplayScreen = new GameplayUI();
        this.gameRoot.getChildren().add(gameplayScreen.getRoot());


        this.pauseScreen = new PauseMenuUI();
        //this.gameRoot.getChildren().add(pauseScreen.getRoot());
        //gameplayScreen.getRoot().setEffect(blurEffect);
        this.gameRoot.getChildren().add(pauseScreen.getRoot());
        pauseScreen.getRoot().setVisible(false); // initially, pause screen isn't visible




        //double.minW
        //double minWidth = gameplayScreen.getRoot().prefWidth(-1); // -1 = use preferred width
        //double minHeight = gameplayScreen.getRoot().prefHeight(-1);
        // double minWidth = gameRoot.prefWidth(-1);
        //double minHeight= gameRoot.prefHeight(-1);
        //primaryStage.setMinWidth((int) minWidth);
        //primaryStage.setMinHeight((int) minHeight);

        gameplayScreen.getRoot().prefWidthProperty().bind(getRoot().widthProperty());
        gameplayScreen.getRoot().prefHeightProperty().bind(getRoot().heightProperty());
        //System.out.println("main stack pane width and height " + gameRoot.prefWidth(-1) + " " + gameRoot.prefHeight(-1));
        pauseScreen.getRoot().prefWidthProperty().bind(getRoot().widthProperty());
        pauseScreen.getRoot().prefHeightProperty().bind(getRoot().heightProperty());




        this.controller = new Controller(gameplayScreen, pauseScreen);
    }
    public Scene getGameScene() {
        return mainScene;
    }
    public Controller getController() {
        return controller;
    }
}
