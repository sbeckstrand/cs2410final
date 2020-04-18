import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game {

    public Game(String team) {
        boolean blueTurn = true;

        BorderPane root = new BorderPane();

        // Left
        VBox leftVbox = new VBox();
        GridPane topPane = new GridPane();
        topPane.setPrefSize(6,5);
        for (int a=0; a<5;a++) {
            for (int b=0;b<6;b++) {
                Rectangle r1 = new Rectangle(25,25);
                r1.setFill(Color.BLUE);
                r1.setStroke(Color.BLACK);
                r1.setStrokeWidth(1);
                topPane.add(r1,b,a);
            }
        }

        Rectangle bg = new Rectangle(75,250);
        bg.setFill(Color.WHITE);

        GridPane bottomPane = new GridPane();
        bottomPane.setPrefSize(6,5);
        for (int c=0; c<5;c++) {
            for (int d=0;d<6;d++) {
                Rectangle r1 = new Rectangle(25,25);
                r1.setFill(Color.RED);
                r1.setStroke(Color.BLACK);
                r1.setStrokeWidth(1);
                bottomPane.add(r1,d,c);
            }
        }

        leftVbox.getChildren().addAll(topPane,bg,bottomPane);

        // Center
        GridPane mainBoard = new GridPane();
        mainBoard.setPrefSize(10,10);
        for (int i=0; i<10; i++) {
            for (int j=0;j<10;j++) {
                Rectangle r1 = new Rectangle(50,50);
                r1.setFill(Color.GRAY);
                r1.setStroke(Color.BLACK);
                r1.setStrokeWidth(1);
                mainBoard.add(r1,j,i);
            }
        }

        GridPane piecesBoard = new GridPane();
        mainBoard.setPrefSize(10,10);
        for (int i=0; i<10; i++) {
            for (int j=0;j<10;j++) {
                Rectangle r1 = new Rectangle(50,50);
                r1.setFill(Color.TRANSPARENT);
                r1.setStroke(Color.BLACK);
//                r1.setStrokeWidth(1);
                mainBoard.add(r1,j,i);
            }
        }

        StackPane grids = new StackPane();
        grids.getChildren().addAll(mainBoard,piecesBoard);

        // Right

        VBox rightVbox = new VBox();

        StackPane title = new StackPane();
        Rectangle titleBox = new Rectangle(200,80);
        titleBox.setFill(Color.TAN);
        Text titleText = new Text("STRATEGO");
        titleText.setFill(Color.WHITE);
        titleText.setFont(Font.font(null, FontWeight.BLACK,32));
        DropShadow ds = new DropShadow();
        titleText.setEffect(ds);
        title.getChildren().addAll(titleBox,titleText);

        TextArea textArea = new TextArea();
        textArea.setPrefSize(200,200);
        textArea.setText("Game updates will go here");

        Button quitButton = new Button("Quit");
        quitButton.setPrefWidth(200);
        quitButton.setPrefHeight(20);
        quitButton.setOnAction(e -> {
            Stage stage = (Stage) quitButton.getScene().getWindow();
            stage.close();
        });

        TextArea rssText = new TextArea();
        rssText.setPrefSize(200,200);
        rssText.setText("RSS will go here");

        rightVbox.getChildren().addAll(title,textArea,quitButton,rssText);

        root.setLeft(leftVbox);
        root.setCenter(grids);
        root.setRight(rightVbox);
    }
}
