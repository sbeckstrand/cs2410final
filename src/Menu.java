import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class Menu  {
    private VBox root;
    private TextFlow menuWrapper;
    private String bgImage;
    private TextFlow previous;

    // Construct Menu Object. In the constructor. We create a VBox as our pane, set a background, set the size of our pane, create a tile, and see if our menu stores a reference to a previous menu. If we do store a reference, add a back button that links to the previous menu.
    public <E> Menu(String title, String background, E previous) throws FileNotFoundException {
        root = new VBox();
        bgImage = background;
        root.setPrefSize(900, 500);
        Image backgroundImage = new Image(background, 1600, 1600, false, false);
        Background bg = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
        root.setBackground(bg);

        Text menuTitle = new Text(title + "\n\n");
        menuWrapper = new TextFlow(menuTitle);


        menuTitle.setFont(Font.font("Helvetica", 70));
        menuTitle.setStyle(
                "-fx-fill: white;\n" +
                "-fx-stroke: black;\n" +
                "-fx-stroke-width: 2px;\n" +
                "-fx-margin-top: 100px;");
        menuWrapper.setTextAlignment(TextAlignment.CENTER);
        menuWrapper.setPadding(new Insets(50));


        if (previous != null) {

            Button backButton = new Button("Back");
            menuWrapper.getChildren().add(backButton.getButton());
            addButton("Host Game", "host", null);
            addButton("Join Game", "host", null);
            Menu previousMenu = (Menu) previous;
            previousMenu.setPrevious((TextFlow) previousMenu.getTextFlow());
            backButton.getButton().setOnMouseClicked( e -> {
                root.getChildren().remove(menuWrapper);
                root.getChildren().add(previousMenu.getTextFlow());
            });
        }
        root.getChildren().add(menuWrapper);
    }

    // Method to get return our Pane that stores the menu.
    public Pane getMenu() {
        return this.root;
    }

    //Method to get the Textflow that stores our title and buttons.
    public TextFlow getTextFlow() {
        return this.menuWrapper;
    }

    // Method to add a button. This checks the button type and will generate sub pages/menues based on type.
    public <E> void addButton(String description, String type, E object) {
        Button button = new Button(description);
        menuWrapper.getChildren().add(button.getButton());

        button.getButton().setOnMouseClicked(e -> {
            if (type.equals("game")) {
                try {
                    Menu clientMenu  = new Menu("Host/Client Settings", bgImage, this);
                    System.out.println(this.previous);
                    if (previous != null) {
                        System.out.println("test");
                        root.getChildren().clear();
                    } else {
                        root.getChildren().remove(menuWrapper);
                    }
                    root.getChildren().add(clientMenu.getMenu());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

            if (type.equals("quit")) {
                Stage currentStage = (Stage) object;
                currentStage.close();
            }

            if (type.equals("page")) {
                root.getChildren().clear();
                String pagePath = (String) object;
                Page page = null;
                try {
                    page = new Page(pagePath, description, menuWrapper, root);
                    root.getChildren().add(page.getPage());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    // Method to update our refernece to a previous menu/page.
    public void setPrevious(TextFlow previous) {
        this.previous = previous;
    }


}
