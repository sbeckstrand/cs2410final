import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;

public class Board {
    ImageView bgImageView;
    GridPane mainBoard;
    GridPane piecesBoard;
    GridPane clickRectangles;
    GridPane capturedRed;
    GridPane capturedBlue;
    Piece[][] pieceArray;
    Rectangle[][] rectangleArray;
    ImageView[][] redPieces;
    ImageView[][] bluePieces;

    boolean hasWon = false;

    // MUSIC
    String musicPath = "assets/themes/general/music.mp3";
    Media media = new Media(new File(musicPath).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);


    // redTurn true at the start since red goes first
    boolean redTurn = true;
    boolean isRed = true;
    SimpleBooleanProperty redTurnProp = new SimpleBooleanProperty(true);

    //TODO: Add an "isRed" variable and add that to checks once we get the server stuff implemented
    //TODO:     so that we can set the client as the red player and server as blue (for example)


    // Coordinates to use for comparisons
    int fromX = 999;
    int toX = 999;
    int fromY = 999;
    int toY = 999;

    // Coordinates to print out
    int fromXCoord;
    int toXCoord;
    int fromYCoord;
    int toYCoord;

    // Properties to bind to, etc.
    // NO NEED TO USE THESE ANYWHERE ELSE AT THE MOMENT
    IntegerProperty fromXProp = new SimpleIntegerProperty(999);
    IntegerProperty toXProp = new SimpleIntegerProperty(999);
    IntegerProperty fromYProp = new SimpleIntegerProperty(999);
    IntegerProperty toYProp = new SimpleIntegerProperty(999);

    public Board() throws FileNotFoundException {
        // Start music
        playBGMusic();

        // Image behind the grids
        FileInputStream bgImageFile = new FileInputStream("assets/themes/general/background.png");
        Image bgImage = new Image(bgImageFile);
        bgImageView = new ImageView(bgImage);
        bgImageView.setFitWidth(510);
        bgImageView.setFitHeight(510);


        // Main GridPane used to display board. Visual part of the board.
        mainBoard = new GridPane();
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

        piecesBoard = new GridPane();
        piecesBoard.setPrefSize(10,10);
        for (int i=0; i<10; i++) {
            for (int j=0;j<10;j++) {
                Rectangle r1 = new Rectangle(50,50);
                r1.setFill(Color.TRANSPARENT);
                r1.setStroke(Color.BLACK);
                r1.setStrokeWidth(1);
                piecesBoard.add(r1,j,i);
            }
        }

        capturedRed = new GridPane();
        capturedRed.setPrefSize(5,8);

        capturedBlue = new GridPane();
        capturedBlue.setPrefSize(5,8);


        clickRectangles = new GridPane();
        rectangleArray = new Rectangle[10][10];
        clickRectangles.setPrefSize(10,10);
        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                int y = i;
                int x = j;
                Rectangle clickRectangle = new Rectangle(50, 50);
                clickRectangle.setFill(Color.BLUE);
                clickRectangle.setStroke(Color.BLACK);
                clickRectangle.setStrokeWidth(1);
                clickRectangle.opacityProperty().set(0);

                // Move logic based on which rectangle is clicked
                clickRectangle.setOnMouseClicked(e -> {
                    // Checks to make sure it's your turn and you select one of your pieces
                    if ((fromX == 999 && fromY == 999) && (pieceArray[x][y] != null) && (redTurn && pieceArray[x][y].getColor().compareTo("r") == 0)
                            || ((fromX == 999 && fromY == 999) && (pieceArray[x][y] != null) && ((!redTurn) && pieceArray[x][y].getColor().compareTo("b") == 0))) {
                        if ((fromX == 999 && fromY == 999) && (pieceArray[x][y] != null)) {
                            if (pieceArray[x][y].getMoveDistance() > 0) {
                                clearRectangleTransparency();
                                showMoveRectangles(x, y);
                                fromX = x;
                                fromXCoord = x;
                                fromY = y;
                                fromYCoord = y;
                                fromXProp.setValue(x);
                                fromYProp.setValue(y);
                            }
                        }
                    }
                    // If you have selected your piece, check if the "to" coordinates are a valid green rectangle
                    else if ((fromX != 999 && fromY != 999) && (rectangleArray[x][y].getOpacity() == 0.5)) {
                        toX = x;
                        toXCoord = x;
                        toY = y;
                        toYCoord = y;
                        toXProp.setValue(x);
                        toYProp.setValue(y);
                        clearRectangleTransparency();
                        if (pieceArray[toX][toY] == null) {
                            pieceArray[toX][toY] = pieceArray[fromX][fromY];
                            pieceArray[fromX][fromY] = null;
                        } else if ((pieceArray[toX][toY].getColor().equals("r") && !redTurn)
                                || (pieceArray[toX][toY].getColor().equals("b") && redTurn)) {
                            int attacker = pieceArray[fromX][fromY].getValue();
                            int defender = pieceArray[toX][toY].getValue();
                            if (defender == 100) {
                                //case for win
                                try {
                                    hasWon = true;
                                    stopBGMusic();
                                    playWinMusic();
                                    removePiece(pieceArray[toX][toY]);
                                    Winner winner = new Winner(pieceArray[fromX][fromY].getColor());
                                } catch (FileNotFoundException ex) {
                                    ex.printStackTrace();
                                }
                                pieceArray[toX][toY] = pieceArray[fromX][fromY];
                                pieceArray[fromX][fromY] = null;
                                // create win window, and end game.
                            } else if (defender == 99) { // case for attacks a bomb
                                if (attacker == 3) {
                                    //attacker wins
                                    try {
                                        removePiece(pieceArray[toX][toY]);
                                    } catch (FileNotFoundException ex) {
                                        ex.printStackTrace();
                                    }
                                    pieceArray[toX][toY] = pieceArray[fromX][fromY];
                                    pieceArray[fromX][fromY] = null;
                                } else {
                                    //attacker loses
                                    try {
                                        removePiece(pieceArray[fromX][fromY]);
                                    } catch (FileNotFoundException ex) {
                                        ex.printStackTrace();
                                    }
                                    pieceArray[fromX][fromY] = null;
                                }
                            } else if (defender == 10 && attacker == 1) {
                                // attacker wins
                                try {
                                    removePiece(pieceArray[toX][toY]);
                                } catch (FileNotFoundException ex) {
                                    ex.printStackTrace();
                                }
                                pieceArray[toX][toY] = pieceArray[fromX][fromY];
                                pieceArray[fromX][fromY] = null;
                            } else { // general case for comparing pieces.
                                if (attacker > defender) {
                                    // attacker wins
                                    try {
                                        removePiece(pieceArray[toX][toY]);
                                    } catch (FileNotFoundException ex) {
                                        ex.printStackTrace();
                                    }
                                    pieceArray[toX][toY] = pieceArray[fromX][fromY];
                                    pieceArray[fromX][fromY] = null;
                                } else if (attacker < defender) {
                                    // defender wins
                                    try {
                                        removePiece(pieceArray[fromX][fromY]);
                                    } catch (FileNotFoundException ex) {
                                        ex.printStackTrace();
                                    }
                                    pieceArray[fromX][fromY] = null;
                                } else {
                                    //its a tie, and they both lose.
                                    try {
                                        removePiece(pieceArray[toX][toY]);
                                        removePiece(pieceArray[fromX][fromY]);
                                    } catch (FileNotFoundException ex) {
                                        ex.printStackTrace();
                                    }
                                    pieceArray[fromX][fromY] = null;
                                    pieceArray[toX][toY] = null;
                                }
                            }
                        }
                        try {
                            if (!hasWon) {
                                playWaitAnimation();
                            }
                            // Switch turns
                            redTurn = !redTurn;
                            redTurnProp.set(redTurn);
                            isRed = !isRed; // Swap turns for graphics TEST. REMOVE LATER
                            // Print out updated board and RESET EVERYTHING
                            updateBoard();
                            fromX = 999;
                            fromY = 999;
                            toX = 999;
                            toY = 999;
                            fromXProp.setValue(999);
                            fromYProp.setValue(999);
                            toXProp.setValue(999);
                            toYProp.setValue(999);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }

                });

                clickRectangles.add(clickRectangle, j, i);
                rectangleArray[j][i] = clickRectangle;
            }
        }

        // arrays to keep track of captured pieces, initially empty.
        bluePieces = new ImageView[8][5];
        for (ImageView[] i: bluePieces){
            for (ImageView j: i){
                j = null;
            }
        }

        redPieces = new ImageView[8][5];
        for (ImageView[] i: redPieces){
            for (ImageView j: i){
                j = null;
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

    // Add listener to this property
    public SimpleBooleanProperty getRedTurnProp() {
        return redTurnProp;
    }

    // Getters for positions
    public int getFromXCoord() { return fromXCoord; }
    public int getFromYCoord() { return fromYCoord; }
    public int getToXCoord() { return toXCoord; }
    public int getToYCoord() { return toYCoord; }

    public ImageView getBgImageView() {
        return bgImageView;
    }

    public GridPane getMainBoard() {
        return this.mainBoard;
    }

    public GridPane getPiecesBoard() {
        return this.piecesBoard;
    }

    public GridPane getClickRectangles() {
        return clickRectangles;
    }

    public GridPane getCapturedRed() {
        return capturedRed;
    }

    public GridPane getCapturedBlue() {
        return capturedBlue;
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

    public void resetCoordinates() {
        fromX = 999;
        fromY = 999;
        toX = 999;
        toY = 999;
        fromXProp.setValue(999);
        fromYProp.setValue(999);
        toXProp.setValue(999);
        toYProp.setValue(999);

        clearRectangleTransparency();

        System.out.println("FromX: " + fromX);
        System.out.println("FromY: " + fromY);
        System.out.println("ToX: " + toX);
        System.out.println("ToY: " + toY);
        System.out.println();
    }

    /**
     * Method to show (as green transparent rectangles) which moves on the gridpane are valid.
     * Including a piece that isn't your color. Won't show green rectangles on your own colored pieces, since
     * you can't move onto yourself
     * @param xPos x Coordinate
     * @param yPos y Coordinate
     */
    public void showMoveRectangles(int xPos, int yPos) {
        if (pieceArray[xPos][yPos] == null) {
            return;
        }

//        //Debug print piece array
//        for (int i=0; i<10;i++) {
//            for (int j=0; j<10; j++) {
//                System.out.print(pieceArray[j][i]);
//            }
//            System.out.println();
//        }

        int moves = pieceArray[xPos][yPos].getMoveDistance();

        // Check up
        int upX = xPos;
        int upY = yPos;
        int upMoves = moves;

        while (upY > 0 && upMoves > 0) {
            upY--;
            if (isInLake(upX,upY)) { break; }

            if ((pieceArray[upX][upY] != null) &&
            (pieceArray[upX][upY].getColor().compareTo(pieceArray[xPos][yPos].getColor()) != 0)) {
                rectangleArray[upX][upY].setFill(Color.GREEN);
                rectangleArray[upX][upY].opacityProperty().set(0.5);
                break;
            }
            else if ((pieceArray[upX][upY] != null) &&
                    (pieceArray[upX][upY].getColor().compareTo(pieceArray[xPos][yPos].getColor()) == 0)) {
                break;
            }
            rectangleArray[upX][upY].setFill(Color.GREEN);
            rectangleArray[upX][upY].opacityProperty().set(0.5);
            upMoves--;
        }

        // Check down
        int downX = xPos;
        int downY = yPos;
        int downMoves = moves;

        while (downY < 9 && downMoves > 0) {
            downY++;
            if (isInLake(downX,downY)) { break; }

            if ((pieceArray[downX][downY] != null) &&
                    (pieceArray[downX][downY].getColor().compareTo(pieceArray[xPos][yPos].getColor()) != 0)) {
                rectangleArray[downX][downY].setFill(Color.GREEN);
                rectangleArray[downX][downY].opacityProperty().set(0.5);
                break;
            }
            else if ((pieceArray[downX][downY] != null) &&
                    (pieceArray[downX][downY].getColor().compareTo(pieceArray[xPos][yPos].getColor()) == 0)) {
                break;
            }
            rectangleArray[downX][downY].setFill(Color.GREEN);
            rectangleArray[downX][downY].opacityProperty().set(0.5);
            downMoves--;
        }

        // Check left
        int leftX = xPos;
        int leftY = yPos;
        int leftMoves = moves;

        while (leftX > 0 && leftMoves > 0) {
            leftX--;
            if (isInLake(leftX,leftY)) { break; }

            if ((pieceArray[leftX][leftY] != null) &&
                    (pieceArray[leftX][leftY].getColor().compareTo(pieceArray[xPos][yPos].getColor()) != 0)) {
                rectangleArray[leftX][leftY].setFill(Color.GREEN);
                rectangleArray[leftX][leftY].opacityProperty().set(0.5);
                break;
            }
            else if ((pieceArray[leftX][leftY] != null) &&
                    (pieceArray[leftX][leftY].getColor().compareTo(pieceArray[xPos][yPos].getColor()) == 0)) {
                break;
            }
            rectangleArray[leftX][leftY].setFill(Color.GREEN);
            rectangleArray[leftX][leftY].opacityProperty().set(0.5);
            leftMoves--;
        }

        // Check right
        int rightX = xPos;
        int rightY = yPos;
        int rightMoves = moves;

        while (rightX < 9 && rightMoves > 0) {
            rightX++;
            if (isInLake(rightX,rightY)) { break; }

            if ((pieceArray[rightX][rightY] != null) &&
                    (pieceArray[rightX][rightY].getColor().compareTo(pieceArray[xPos][yPos].getColor()) != 0)) {
                rectangleArray[rightX][rightY].setFill(Color.GREEN);
                rectangleArray[rightX][rightY].opacityProperty().set(0.5);
                break;
            }
            else if ((pieceArray[rightX][rightY] != null) &&
                    (pieceArray[rightX][rightY].getColor().compareTo(pieceArray[xPos][yPos].getColor()) == 0)) {
                break;
            }
            rectangleArray[rightX][rightY].setFill(Color.GREEN);
            rectangleArray[rightX][rightY].opacityProperty().set(0.5);
            rightMoves--;
        }

    }

    public boolean isInLake(int xPos, int yPos) {
        if ((xPos == 2 && yPos == 4)
            || (xPos == 2 && yPos == 5)
                || (xPos == 3 && yPos == 4)
                || (xPos == 3 && yPos == 5)) {
            return true;
        }
        else if ((xPos == 6 && yPos == 4)
                || (xPos == 6 && yPos == 5)
                || (xPos == 7 && yPos == 4)
                || (xPos == 7 && yPos == 5)) {
            return true;
        }
        else {
            return false;
        }

    }

    public void clearRectangleTransparency() {
        for (int x = 0; x < 10; x++) {
            for (int y=0; y < 10; y++) {
                rectangleArray[y][x].opacityProperty().set(0);
            }
        }
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
                    if (isRed && !pieceArray[b][a].getColor().equals("r")) {
                        imageName = pieceArray[b][a].getColor() + "DEFAULT";
                    }
                    else if (!isRed && !pieceArray[b][a].getColor().equals("b")) {
                        imageName = pieceArray[b][a].getColor() + "DEFAULT";
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
    int blueX = 0; int blueY = 0;
    int redX = 0; int redY = 0;


    private void removePiece(Piece piece) throws FileNotFoundException {
        String imageName = "";
        int value = piece.getValue();
        if (value != 99 && value != 100) {
            imageName = piece.getColor() + value;
        } else if (value == 99) {
            imageName = piece.getColor() + "b";
        } else if (value == 100) {
            imageName = piece.getColor() + "f";
        }

        FileInputStream imageFile = new FileInputStream("assets/themes/mario/pieces/" + imageName + ".png");
        Image pieceGraphic = new Image(imageFile);
        ImageView imageView = new ImageView(pieceGraphic);
        imageView.setFitWidth(26);
        imageView.setFitHeight(26);

        if (piece.getColor().equals("b")){
            //capturedBlue.add(imageView,blueX,blueY);
            capturedRed.add(imageView,blueX,blueY);
            blueX++;
            if (blueX > 4){
                blueX = 0;
                blueY++;
            }
        } else {
//            capturedRed.add(imageView,redX,redY);
            capturedBlue.add(imageView,redX,redY);
            redX++;
            if (redX > 4){
                redX = 0;
                redY++;
            }
        }
    }

    public void playBGMusic() {
        Task playMusic = new Task() {
            @Override
            protected Object call() throws Exception {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                while (true) {
                    mediaPlayer.play();
                }
            }
        };

        Thread t1 = new Thread(playMusic);
        t1.start();
    }

    public void stopBGMusic() {
        mediaPlayer.setVolume(0);
    }

    public void resumeBGMusic() {
        mediaPlayer.play();
        mediaPlayer.setVolume(1);
    }

    private void playWinMusic() {
        // play victory theme
        String VictoryPath = "assets/themes/general/Victory1.m4a";
        Media VictoryMedia = new Media(new File(VictoryPath).toURI().toString());
        MediaPlayer VictoryPlayer = new MediaPlayer(VictoryMedia);

        Task playVictoryTheme = new Task() {
            @Override
            protected Object call() throws Exception {
                VictoryPlayer.setCycleCount(1);
                while (true) {
                    VictoryPlayer.play();
                }
            }
        };

        Thread t2 = new Thread(playVictoryTheme);
        t2.start();
    }

    private void playWaitAnimation() throws FileNotFoundException {
        Stage waitStage = new Stage();
        Pane p1 = new Pane();
        VBox textBox = new VBox();
        Text switchPlayerText = new Text("Switch Players!");
        switchPlayerText.xProperty().bind(p1.widthProperty().divide(2).subtract(330));
        switchPlayerText.yProperty().bind(p1.heightProperty().divide(2).subtract(50));

        // Font
        FileInputStream fontInput2 = new FileInputStream("assets/Seagram tfb.ttf");
        Font strategoFontSmall = Font.loadFont(fontInput2,100);
        switchPlayerText.setFont(strategoFontSmall);

        Text countDown = new Text("3");
        countDown.setFill(Color.WHITE);
        countDown.setFont(strategoFontSmall);
        textBox.getChildren().addAll(switchPlayerText,countDown);

        countDown.xProperty().bind(p1.widthProperty().divide(2).subtract(40));
        countDown.yProperty().bind(p1.heightProperty().divide(2).add(80));

        FileInputStream waitBGImage = new FileInputStream("assets/themes/general/WaitBGWood.png");
        Image bgImageW = new Image(waitBGImage);
        ImageView waitBG = new ImageView(bgImageW);
        waitBG.setFitWidth(900);
        waitBG.setFitHeight(520);

//        p1.getChildren().add(textBox);
        p1.getChildren().addAll(switchPlayerText,countDown);
        p1.setPrefWidth(900);
        p1.setPrefHeight(520);

        StackPane stack = new StackPane();
        stack.getChildren().addAll(waitBG,p1);

        Scene s1 = new Scene(stack);
        waitStage.setScene(s1);
        waitStage.show();

        Timeline toClose = new Timeline();
        toClose.setCycleCount(5);
        AtomicReference<Integer> count = new AtomicReference<>(Integer.parseInt(countDown.getText()));
        AtomicReference<Integer> zero = new AtomicReference<>(0);
        toClose.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            count.getAndSet(count.get() - 1);
            countDown.setText(count.toString());
            if (count.get().equals(0)) {
                waitStage.close();
            }
        }));
        toClose.play();

    }
}


