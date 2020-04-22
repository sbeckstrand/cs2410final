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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import java.util.Random;


public class Game {
    boolean blueTurn;
    GameUI UI;
    Piece[] pieces;

    public Game(String team) throws FileNotFoundException {
        boolean blueTurn = true;

        UI = new GameUI();
//        Board gameBoard = UI.getBoard();
        pieces = buildPieces();

        randomizePlacement();


    }

    public GameUI getUI() {
        return this.UI;
    }

    private void randomizePlacement() throws FileNotFoundException {
        int currentBlue = 0;
        int currentRed = 60;

        ArrayList<String> unplacedPieces = new ArrayList<>();

        for (int i = 0; i < 80; i++) {
            unplacedPieces.add(String.valueOf(i));
        }

        while (unplacedPieces.size() > 0) {
            Random rand = new Random();
            int randomNumber = Integer.parseInt(unplacedPieces.get(rand.nextInt(unplacedPieces.size())));

            if (pieces[randomNumber].getColor() == "b") {
                UI.getBoard().setPiece( currentBlue % 10, currentBlue / 10, pieces[randomNumber]);
                currentBlue +=1;
            } else {
                UI.getBoard().setPiece( currentRed % 10, (currentRed / 10), pieces[randomNumber]);
                currentRed +=1;
            }
            unplacedPieces.remove(String.valueOf(randomNumber));

        }

    }


    private Piece[] buildPieces() {
        Piece[] pieces = new Piece[80];
        String color = "";
        int currentIndex = 0;

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                color = "b";
            } else {
                color = "r";
            }

            for (int j = 0; j < 12; j++) {
                int currentValue = j + 1;
                int currentCount = 1;

                if (currentValue == 2) {
                    currentCount = 8;
                }
                else if (currentValue == 3) {
                    currentCount = 5;
                }
                else if (currentValue == 4 || currentValue == 5 || currentValue == 6) {
                    currentCount = 4;
                }
                else if (currentValue == 7) {
                    currentCount = 3;
                }
                else if (currentValue == 8) {
                    currentCount = 2;
                }
                else if (currentValue == 11) {
                    currentValue = 99;
                    currentCount = 6;
                }
                else if (currentValue == 12) {
                    currentValue = 100;
                }

                for (int k = 0; k < currentCount; k++) {
                    pieces[currentIndex] = new Piece(currentValue, color);
                    currentIndex++;
                }

            }

        }

        return pieces;
    }


    public boolean playerWin(Piece A, Piece B){ // when this returns true, the game will end, (needs to be written into the game logic probably.)
        // Check if either of the revealed pieces is a flag
        if(A.getValue() == 100 || B.getValue() == 100){
            if(A.getValue()== 100) { // B is the winner
                DisplayWinner(B.getColor());
                return true;
            } else { // A is the winner
                DisplayWinner(A.getColor());
                return true;
            }
        }
        return false;
    }

    public void DisplayWinner(String winner){
        // Make a window to display the winner
        // So I'm thinking for this to have a separate window pop up with who the winner is, but I'm not entirely sure how to do this.
    }

}
