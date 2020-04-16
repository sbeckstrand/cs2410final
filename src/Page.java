import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Page<E> {
    TextFlow page;
    ScrollPane pageScroll;

    // Constructor which sets a title for the page, a back button, and then reads through a page text file and adds the content to the page.
    public Page(String file, String description, TextFlow previous, Pane pane) throws FileNotFoundException {
        page = new TextFlow();
        pageScroll = new ScrollPane();
        Text pageTitle = new Text(description + "\n");
        pageTitle.setFont(Font.font("Helvetica", 70));
        pageTitle.setStyle(
                "-fx-fill: white;\n" +
                "-fx-stroke: black;\n" +
                "-fx-stroke-width: 2px;\n");
        page.getChildren().add(pageTitle);
//        page.setTextAlignment(TextAlignment.CENTER);
        page.setPadding(new Insets(50));

        Button backButton = new Button("Back");
        page.getChildren().add(backButton.getButton());

        backButton.getButton().setOnMouseClicked( e -> {
            pane.getChildren().remove(pageScroll);
            pane.getChildren().add(previous);
        });

        Scanner reader = new Scanner(new File(file));

        while (reader.hasNext()) {
            String[] lineArray = reader.nextLine().split("----");
            Text textContent = null;
            int fontSize = 0;
            int marginTop = 0;
            if (lineArray[0].equals("Header")) {
                fontSize = 25;
                textContent = new Text("\n" + lineArray[1] + "\n");
                textContent.setUnderline(true);
            } else if (lineArray[0].equals("Paragraph")) {
                fontSize = 20;
                textContent = new Text("\n" + lineArray[1] + "\n");
            }
            textContent.setFont(Font.font("Helvetica", fontSize));
            textContent.setStyle(
                    "-fx-fill: black;\n" +
                    "-fx-stroke: black;\n" +
                    "-fx-stroke-width: 1px;");


            page.getChildren().add(textContent);
        }
        page.setMaxWidth(900);
        page.setMaxWidth(880);
        pageScroll.setMinHeight(500);
        pageScroll.setMaxWidth(900);
        pageScroll.setContent(page);
        pageScroll.setStyle(
                "-fx-background: rgb(137,202,127);\n" +
                "-fx-background-color: rgb(137,202,127);");

    }

    // Getter method to return our Page content in a ScrollPane.
    public ScrollPane getPage() {
        return this.pageScroll;
    }
}
