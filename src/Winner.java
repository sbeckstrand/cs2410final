import javafx.application.Application;
import javafx.application.Platform;
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
        Stage stage = new Stage();
        Scene scene = new Scene(pane);

        if (color.equals("r")){
            Image bgImage = new Image("themes/general/redWin.png", 300,200,false, false);
            Background bg = new Background(new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,BackgroundSize.DEFAULT));
            pane.setBackground(bg);
        } else if (color.equals("b")){
            Image bgImage = new Image("themes/general/blueWin.png", 300,200, false, false);
            Background bg = new Background(new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,BackgroundSize.DEFAULT));
            pane.setBackground(bg);
        }

        Button quit = new Button("quit");
        quit.getButton().xProperty().bind(pane.widthProperty().divide(8).multiply(3));
        quit.getButton().yProperty().bind(pane.heightProperty().divide(5).multiply(4));
        pane.getChildren().add(quit.getButton());

        quit.getButton().setOnMouseClicked(e -> {
            Platform.exit();
        });

        stage.setScene(scene);
        stage.show();

    }

    public Pane getPane(){ return this.pane;}
}
