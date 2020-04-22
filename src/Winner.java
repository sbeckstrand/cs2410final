import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Winner {
    private Pane pane;

    public Winner(String color){
        pane = new Pane();
        pane.setPrefSize(300,200);

        if (color.equals("red")){
            Image bgImage = new Image("themes/general/redWin.png", 300,200,false, false);
            Background bg = new Background(new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,BackgroundSize.DEFAULT));
            pane.setBackground(bg);
        } else if (color.equals("blue")){
            Image bgImage = new Image("themes/general/blueWin.png", 300,200, false, false);
            Background bg = new Background(new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,BackgroundSize.DEFAULT));
            pane.setBackground(bg);
        }

    }

    public Pane getPane(){ return this.pane;}
}
