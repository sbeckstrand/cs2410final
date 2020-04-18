import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.TimerTask;

public class ReadRSS {
    String webURL;
    TextArea textArea;

    public ReadRSS(String url, TextArea t1) {
        this.webURL = url;
        textArea = t1;
    }

    public void readRSS(String urlAddress, TextArea t1) throws Exception {
        URL rssURL = new URL(urlAddress);
        BufferedReader reader = new BufferedReader(new InputStreamReader(rssURL.openStream()));
        String line;
        int lineCount = 0;

        // Reset text so that only the current updated text shows up, not the old text in addition to the new text
        t1.setText("");

        // Read through
        while ((line = reader.readLine()) != null) {

            //REPLACE SPECIAL CHARACTERS
            if (line.contains("&#x22;")) {
                line = line.replace("&#x22;", "");
            }
            else if (line.contains("&#x27;")) {
                line = line.replace("&#x27;", "");
            }
            else if (line.contains("&#x2019;")) {
                line = line.replace("&#x2019;", "\'");
            }

            //TODO ---- ADD CODE TO READ THROUGH THE <updated> </updated> lines which contain the times of the quakes

            // Read through "Title" lines
            if (line.contains("title")) {
                String titleLine = line.substring(line.indexOf("<title>"),line.indexOf("</title>"));
                t1.appendText(titleLine.replace("<title>","").replace("</title>","").strip());
                t1.appendText("\n");
                lineCount++;
            }

            if (line.contains("updated")) {
                String timeLine = line.substring(line.indexOf("<updated>"),line.indexOf("</updated>"));
                t1.appendText(timeLine.replace("<updated>","").replace("</updated>","").strip());
                t1.appendText("\n\n");
            }

            // Only show 10 most recent updates
            if (lineCount > 5) {
                break;
            }
        }

        // Add text to RSS window
        t1.appendText("\n");
        reader.close();
    }

    public void run() {
        try {
            readRSS(webURL, textArea);
        } catch (Exception e) {
            System.out.println("LINE 126 IS WHERE IT'S HAPPENING");
            e.printStackTrace();

        }
    }
}