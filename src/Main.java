import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //Create Main Menu object and add menu items.
        Menu mainMenu = new Menu("Stratego", "themes/general/background.png", null);
        mainMenu.addButton("Play", "game", "test");
        mainMenu.addButton("Rules", "page", "assets/pages/rules.txt");
        mainMenu.addButton("About", "page", "assets/pages/about.txt");
        mainMenu.addButton("Quit", "quit", stage);


        stage.setScene(new Scene(mainMenu.getMenu()));
        stage.setTitle("Stratego");
        stage.show();
    }
}
