import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class RSSFeed extends Application {
    String rss;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();

        // Text area to display RSS feed
        TextArea textArea = new TextArea();
        textArea.setScaleX(300);
        textArea.setScaleY(300);
        textArea.setEditable(false);

        //TODO: Implement this correctly to print to a GUI maybe when someone clicks a "GET RSS" button by the Rss Feed Box
        Platform.runLater(() -> {
            Timer t1 = new Timer();
            t1.schedule(new ReadRSS("https://twitrss.me/twitter_user_to_rss/?user=cnn"),0,60000);
            textArea.setText(rss);
        });

        root.getChildren().addAll(textArea);

        Scene s1 = new Scene(root,300,300);
        stage.setScene(s1);
        stage.setTitle("RSS Feed");
        stage.show();
    }

    public static String readRSS(String urlAddress) throws Exception {
        URL rssURL = new URL(urlAddress);
        BufferedReader reader = new BufferedReader(new InputStreamReader(rssURL.openStream()));
        String sourceCode = "";
        String line;
        int lineCount = 0;
        while ((line = reader.readLine()) != null) {
            line = line.replace("&#x22;",""); //REPLACE SPECIAL CHARACTERS
            line = line.replace("&#x27;","");
            if (line.contains("title")) {
                System.out.println("Title: " + line.replace("<title>","").replace("</title>","").strip());
                sourceCode += line + "\n";
                lineCount++;
            }
            if (line.contains("pubDate")) {
                System.out.println("Date: " + line.replace("<pubDate>","").replace("</pubDate>","").strip());
                sourceCode += line + "\n\n";
                lineCount++;
            }
            if (lineCount > 6) {
                break;
            }
        }
        System.out.println("\n");
        reader.close();
        return sourceCode;

    }

    public class ReadRSS extends TimerTask {
        String webURL;

        public ReadRSS(String url) {
            this.webURL = url;
        }

        @Override
        public void run() {
            try {
                rss = readRSS(webURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}