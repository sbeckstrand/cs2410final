import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Button {
    Text button;
    public Button(String description){
        button = new Text(description + '\n');

        button.setFont(Font.font("Helvetica", 40));
        button.setStyle(
                "-fx-fill: white;\n" +
                        "-fx-stroke: black;\n" +
                        "-fx-stroke-width: 1px");

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-fill: black");
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-fill: white;\n" +
                            "-fx-stroke: black;");
        });
    }

    public Text getButton() {
        return button;
    }
}
