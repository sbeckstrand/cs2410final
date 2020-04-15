import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Menu mainMenu = new Menu("Stratego", "themes/general/background.png");
        mainMenu.addButton("Play", "game");

        Page rules = new Page("pages/rules.txt");
        mainMenu.addButton("Rules", "page");

        Page about = new Page("pages/about.txt");
        mainMenu.addButton("About", "page");

        mainMenu.addButton("Quit", "quit");


        stage.setScene(new Scene(mainMenu.getMenu()));
        stage.setTitle("Stratego");
        stage.show();
    }
}
