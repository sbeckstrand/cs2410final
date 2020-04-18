import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.css.Rect;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainLayout extends Application {
    boolean blueTurn = false; //TODO: SET TO TRUE
    /** SERVER IS RED, so blueTurn starts as true since client goes first **/

    @Override
    public void start(Stage stage) throws Exception {
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
            stage.close();
        });

        TextArea rssText = new TextArea();
        rssText.setPrefSize(200,200);
        rssText.setText("RSS will go here");

        rightVbox.getChildren().addAll(title,textArea,quitButton,rssText);

//        Rectangle bottomRec = new Rectangle(775,30);
//        bottomRec.setFill(Color.CADETBLUE);

        root.setLeft(leftVbox);
        root.setCenter(grids);
        root.setRight(rightVbox);
//        root.setBottom(bottomRec);
        //root.getBottom().resize(0,0);


        /***************** SERVER CONNECTION ******************/
        Piece[][] pieceArray = new Piece[10][10];
        // Initially set all to null
        for (int a=0;a<10;a++) {
            for (int b=0;b<10;b++) {
                pieceArray[b][a] = null;
            }
        }

//        // Place one piece
        Piece pieceToAdd = new Piece(10,"b");
        pieceArray[2][5] = pieceToAdd;
        Piece piece2 = new Piece(2,"r");
        pieceArray[4][6] = piece2;
//        FileInputStream imageFile = new FileInputStream("assets/themes/mario/pieces/b10.png");
//        //FileInputStream imageFile = new FileInputStream("../assets/themes/mario/pieces/" + "b" + pieceToAdd.getValue() + ".png");
//        Image piece = new Image(imageFile);
//        ImageView imageView = new ImageView(piece);
//        imageView.setFitHeight(50);
//        imageView.setFitWidth(50);
//        piecesBoard.add(imageView,2,5);

        drawGridPane(piecesBoard,pieceArray);
        int a=2;
        int b=5;

        int c=4;
        int d=6;

        // TEST MOVING PIECE TO UPDATE GAME BOARD
        Timeline movePieceAnimation = new Timeline();
        movePieceAnimation.setCycleCount(1);
        movePieceAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(5), e -> {
            pieceArray[a][b] = null;
            pieceArray[a+1][b+1] = pieceToAdd;
            try {
                drawGridPane(piecesBoard,pieceArray);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            pieceArray[c][d] = null;
            pieceArray[c][d-3] = piece2;
            try {
                drawGridPane(piecesBoard,pieceArray);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }));

        movePieceAnimation.play();

        if (!blueTurn) {

        }



        Scene s1 = new Scene(root);
        stage.setScene(s1);
        stage.setResizable(false);
        stage.setTitle("Stratego");
        stage.show();
    }

    public Node getNode(GridPane gridPane, int column, int row) {
        for (Node child : gridPane.getChildren()) {
            if ((GridPane.getRowIndex(child) == row) && (GridPane.getColumnIndex(child) == column));
                return child;
        }
        return null;
    }

    public void setGrid(GridPane oldPane, GridPane newPane) {
        oldPane = newPane;
    }

    public void drawGridPane(GridPane gridPane, Piece[][] pieceArray) throws FileNotFoundException {
        String imageName = "";
        // Clears the past board and redraws with the new one
        gridPane.getChildren().clear();

        // Go through and update game board based on move
        for (int a=0;a<10;a++) {
            for (int b=0;b<10;b++) {
                // Reset location each draw
                //gridPane.getChildren().remove(10*a+b);

                if (pieceArray[b][a] == null) {
                    //imageName = "-";
                    Rectangle blank = new Rectangle(50,50);
                    blank.setFill(Color.TRANSPARENT);
                    blank.setStroke(Color.TRANSPARENT);
                    blank.setStrokeWidth(1);
                    gridPane.add(blank,b,a);
                    //gridPane.getChildren().add((10*a + b),blank);
                    continue;
                }
                else {
                    imageName = pieceArray[b][a].getColor() + pieceArray[b][a].getValue();
                }
                //TODO: This can change based on theme obviously
                FileInputStream imageFile = new FileInputStream("assets/themes/mario/pieces/" + imageName + ".png");
                Image pieceGraphic = new Image(imageFile);
                ImageView imageView = new ImageView(pieceGraphic);
                imageView.setFitWidth(51);
                imageView.setFitHeight(51);
                gridPane.add(imageView,b,a);
            }
        }
    }
}
