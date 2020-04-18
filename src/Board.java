import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Board {
    GridPane mainBoard;
    GridPane piecesBoard;
    Piece[][] pieceArray;

    public Board() {
        // Main GridPane used to display board. Visual part of the board.
        mainBoard = new GridPane();
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

        piecesBoard = new GridPane();
        mainBoard.setPrefSize(10,10);
        for (int i=0; i<10; i++) {
            for (int j=0;j<10;j++) {
                Rectangle r1 = new Rectangle(50,50);
                r1.setFill(Color.TRANSPARENT);
                r1.setStroke(Color.BLACK);
                r1.setStrokeWidth(1);
                mainBoard.add(r1,j,i);
            }
        }

        // Logical array to track pieces
        pieceArray = new Piece[10][10];
        // Initially set all to null
        for (int a=0;a<10;a++) {
            for (int b=0;b<10;b++) {
                pieceArray[b][a] = null;
            }
        }


    }

    public GridPane getMainBoard() {
        return this.mainBoard;
    }

    public GridPane getPiecesBoard() {
        return this.piecesBoard;
    }

    public Piece[][] getPieceArray() {
        return this.getPieceArray();
    }

    public void setPiece(int x, int y, Piece piece) throws FileNotFoundException {
        pieceArray[x][y] = piece;
        updateBoard();
    }


    public void setPiece(int currentX, int currentY, int newX, int newY, Piece piece) throws FileNotFoundException {

        updateBoard();
    }

    private void updateBoard() throws FileNotFoundException {
        String imageName = "";
        // Clears the past board and redraws with the new one
        piecesBoard.getChildren().clear();

        // Go through and update game board based on move
        for (int a=0;a<10;a++) {
            for (int b=0;b<10;b++) {

                if (pieceArray[b][a] == null) {
                    Rectangle blank = new Rectangle(50,50);
                    blank.setFill(Color.TRANSPARENT);
                    blank.setStroke(Color.TRANSPARENT);
                    blank.setStrokeWidth(1);
                    piecesBoard.add(blank,b,a);
                    continue;
                }
                else {
                    int value = pieceArray[b][a].getValue();
                    if (value != 99 && value != 100) {
                        imageName = pieceArray[b][a].getColor() + value;
                    } else if (value == 99) {
                        imageName = pieceArray[b][a].getColor() + "b";
                    } else if (value == 100) {
                        imageName = pieceArray[b][a].getColor() + "f";
                    }
                }

                FileInputStream imageFile = new FileInputStream("assets/themes/mario/pieces/" + imageName + ".png");
                Image pieceGraphic = new Image(imageFile);
                ImageView imageView = new ImageView(pieceGraphic);
                imageView.setFitWidth(51);
                imageView.setFitHeight(51);
                piecesBoard.add(imageView,b,a);
            }
        }
    }
}


