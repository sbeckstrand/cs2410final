import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class RSSFeed extends Application {

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();

        // Text area to display RSS feed
        TextArea textArea = new TextArea();
        textArea.setScaleX(300);
        textArea.setScaleY(300);
        textArea.setWrapText(true);

        for (int i=0; i<20; i++) {
            textArea.appendText("HELLO \n");
        }

        root.getChildren().addAll(textArea);

        Scene s1 = new Scene(root,300,300);
        stage.setScene(s1);
        stage.setTitle("RSS Feed");
        stage.show();
    }
}