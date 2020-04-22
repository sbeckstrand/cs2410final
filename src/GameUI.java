import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameUI {
    Board board;
    BorderPane layout;

    public GameUI() throws FileNotFoundException {
        layout = new BorderPane();


        // Left ---------------------------------------------------------
        VBox leftVbox = new VBox();
        GridPane topPane = new GridPane();
        topPane.setPrefSize(5,8);
        for (int a=0; a<8;a++) {
            for (int b=0;b<5;b++) {
                Rectangle r1 = new Rectangle(25,25);
                r1.setFill(Color.BLUE);
                r1.setStroke(Color.BLACK);
                r1.setStrokeWidth(1);
                topPane.add(r1,b,a);
            }
        }

        Rectangle bg = new Rectangle(75,94);
        bg.setFill(Color.WHITE);

        GridPane bottomPane = new GridPane();
        bottomPane.setPrefSize(5,8);
        for (int c=0; c<8;c++) {
            for (int d=0;d<5;d++) {
                Rectangle r1 = new Rectangle(25,25);
                r1.setFill(Color.RED);
                r1.setStroke(Color.BLACK);
                r1.setStrokeWidth(1);
                bottomPane.add(r1,d,c);
            }
        }

        leftVbox.getChildren().addAll(topPane,bg,bottomPane);

        // Center ---------------------------------------------------------
        this.board = new Board();

        StackPane grids = new StackPane();
        grids.getChildren().addAll(board.getBgImageView(),board.getMainBoard(),board.getPiecesBoard(),board.getClickRectangles());

        // Right ---------------------------------------------------------

        // Add custom font to Java
        FileInputStream fontInput = new FileInputStream("assets/Seagram tfb.ttf");
        Font strategoFont = Font.loadFont(fontInput,42);

        // Smaller version to test
        FileInputStream fontInput2 = new FileInputStream("assets/Seagram tfb.ttf");
        Font strategoFontSmall = Font.loadFont(fontInput2,16);

        VBox rightVbox = new VBox();

        StackPane title = new StackPane();
        Rectangle titleBox = new Rectangle(200,80);
        titleBox.setFill(Color.TAN);
        Text titleText = new Text("Stratego");
        titleText.setFill(Color.WHITE);
        titleText.setFont(strategoFont);
        DropShadow ds = new DropShadow();
        titleText.setEffect(ds);
        title.getChildren().addAll(titleBox,titleText);

        TextArea textArea = new TextArea();
        textArea.setPrefSize(200,150);

        textArea.appendText("Red Player Starts!" + "\n");

        // Listener for changing between turns
        board.getRedTurnProp().addListener( e -> {
            if (board.getRedTurnProp().get() == true) {
//                textArea.appendText("Red Player's Turn" + "\n");
                textArea.appendText("Blue moved: ");
                textArea.appendText("(" + board.getFromXCoord() + "," + board.getFromYCoord() + ") ");
                textArea.appendText("to (" + board.getToXCoord() + "," + board.getToYCoord() + ") \n");
            }
            else {
//                textArea.appendText("Blue Player's Turn" + "\n");
                textArea.appendText("Red moved: ");
                textArea.appendText("(" + board.getFromXCoord() + "," + board.getFromYCoord() + ") ");
                textArea.appendText("to (" + board.getToXCoord() + "," + board.getToYCoord() + ") \n");
            }
        });

        Button quitButton = new Button("Quit");
        quitButton.setPrefWidth(100);
        quitButton.setPrefHeight(20);
        quitButton.setOnAction(e -> {
            Stage stage = (Stage) quitButton.getScene().getWindow();
            stage.close();
        });

        Button resetMoves = new Button("Reset Move");
        resetMoves.setPrefWidth(100);
        resetMoves.setPrefHeight(20);
        resetMoves.setOnAction(e -> {
            board.resetCoordinates();
        });
        resetMoves.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                board.resetCoordinates();
            }
        });

        HBox buttonHBox = new HBox();
        buttonHBox.getChildren().addAll(resetMoves,quitButton);

        TextArea rssText = new TextArea();
        rssText.setPrefSize(200,253);
        rssText.setFont(Font.font(null,10));

        /** ---------------------------------- RSS FEED ---------------------------------------**/
        Task rssTask = new Task() {
            @Override
            protected Object call() throws Exception {
                ReadRSS rssReader = new ReadRSS("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom",rssText);

                // Update RSS feed every minute
                Timeline timelineRSS = new Timeline();
                timelineRSS.setCycleCount(Timeline.INDEFINITE);
                timelineRSS.setDelay(Duration.seconds(0));
                timelineRSS.getKeyFrames().add(new KeyFrame(Duration.seconds(20), e -> {
                    rssReader.run();
                }));
                timelineRSS.play();
                return null;
            }
        };

        Thread readRSSThread = new Thread(rssTask);
        readRSSThread.start();
        /** -----------------------------------------------------------------------------------**/

        rightVbox.getChildren().addAll(title,textArea,buttonHBox,rssText);

        layout.setLeft(leftVbox);
        layout.setCenter(grids);
        layout.setRight(rightVbox);
    }

    public Board getBoard() {
        return this.board;
    }

    public BorderPane getLayout() {
        return this.layout;
    }
}
