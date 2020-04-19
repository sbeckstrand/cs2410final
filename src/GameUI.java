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

public class GameUI {
    Board board;
    BorderPane layout;

    public GameUI() {
        layout = new BorderPane();


        // Left
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

        // Center
        this.board = new Board();


        StackPane grids = new StackPane();
        grids.getChildren().addAll(board.getMainBoard(),board.getPiecesBoard());

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
