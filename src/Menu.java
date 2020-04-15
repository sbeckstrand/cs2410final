import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class Menu {
    private VBox root;

    public Menu(String title, String background) {
        root = new VBox();
        root.setPrefSize(1600, 1000);
        Image backgroundImage = new Image(background, 1600, 1600, false, false);
        Background bg = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
        root.setBackground(bg);

        Text menuTitle = new Text(title);
        TextFlow titleWrapper = new TextFlow(menuTitle);


        menuTitle.setFont(Font.font("Helvetica", 70));
        menuTitle.setStyle("-fx-fill: white;\n" +
                "-fx-stroke: black;\n" +
                "-fx-stroke-width: 2px");
        titleWrapper.setTextAlignment(TextAlignment.CENTER);




        root.getChildren().add(titleWrapper);
    }

    public Pane getMenu() {
        return this.root;
    }

    public void addButton(String description, String type) {

    }


}
