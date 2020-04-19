import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
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

        // Play music test
        /** --------------------------------- MUSIC PLAYING IN BACKGROUND ----------------------**/
        String musicPath = "assets/themes/general/music.mp3";
        Media media = new Media(new File(musicPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

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
        //t1.start();
        /** -----------------------------------------------------------------------------------**/

        stage.setScene(new Scene(mainMenu.getMenu()));
        stage.setResizable(false);
        stage.setTitle("Stratego");
        stage.show();
    }
}
