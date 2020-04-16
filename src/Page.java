import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Page<E> {
    TextFlow page;

    public Page(String file, String description, TextFlow previous, Pane pane) throws FileNotFoundException {
        page = new TextFlow();
        Text pageTitle = new Text(description + "\n");
        pageTitle.setFont(Font.font("Helvetica", 70));
        pageTitle.setStyle(
                "-fx-fill: white;\n" +
                "-fx-stroke: black;\n" +
                "-fx-stroke-width: 2px;\n" +
                "-fx-margin-top: 100px;");
        page.getChildren().add(pageTitle);
        page.setTextAlignment(TextAlignment.CENTER);
        page.setPadding(new Insets(50));

        Button backButton = new Button("Back");
        page.getChildren().add(backButton.getButton());

        backButton.getButton().setOnMouseClicked( e -> {
            pane.getChildren().remove(page);
            pane.getChildren().add(previous);
        });

        Scanner reader = new Scanner(new File(file));

        while (reader.hasNext()) {
            String[] lineArray = reader.nextLine().split("----");
            Text textContent = new Text(lineArray[1] + "\n");
            int fontSize = 0;

            if (lineArray[0].equals("Header")) {
                fontSize = 20;
            } else if (lineArray[0].equals("Paragraph")) {
                fontSize = 15;
            }
            textContent.setFont(Font.font("Helvetica", fontSize));
            textContent.setStyle(
                    "-fx-fill: white;\n" +
                    "-fx-stroke: black;\n" +
                    "-fx-stroke-width: 1px;\n" +
                    "-fx-margin-top: 100px;");
            page.getChildren().add(textContent);
        }



        //Loop through file. If starts with "Header", add a Header to the TextFlow. If starts with "Para", add Paragraph to TextFlow
    }

    private void addHeader(String header) {

    }

    private void addParagraph(String paragraph) {

    }

    public TextFlow getPage() {
        return this.page;
    }
}
