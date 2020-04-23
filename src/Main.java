import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //Create Main Menu object and add menu items.
        Menu mainMenu = new Menu("Stratego", "themes/general/background.png", null);
        mainMenu.addButton("Play", "game", "test");
        mainMenu.addButton("Rules", "page", "assets/pages/rules.txt");
        mainMenu.addButton("About", "page", "assets/pages/about.txt");
        mainMenu.addButton("Quit", "quit", stage);


        /** -----------------------------------------------------------------------------------**/

        stage.setScene(new Scene(mainMenu.getMenu()));
        stage.setResizable(false);
        stage.setTitle("Stratego");
        stage.show();
    }
}
