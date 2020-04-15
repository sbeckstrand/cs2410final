import javafx.scene.text.TextFlow;

public class Page {
    TextFlow page;

    public Page(String file) {
        page = new TextFlow();

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
