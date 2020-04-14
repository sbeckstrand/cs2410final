import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class RSSFeed extends Application {
    String rss;
    TextField textAreaOverall;

    @Override
    public void start(Stage stage) {
        VBox vBox = new VBox();

        // Text area to display RSS feed
        TextArea textArea = new TextArea();
        textArea.setPrefSize(300,400);
        textArea.setWrapText(true);
        textArea.setOnMouseClicked(null);
        textArea.setFont(Font.font("Times New Roman", FontWeight.NORMAL,10));
        textArea.setEditable(false);

        //TODO: Save this code to show what error pops up (if I need the RSS feed to be automatically updating)
//        Task<Void> t1 = new Task<>() {
//            @Override
//            protected Void call() {
//                // Make an instance of ReadRSS class, and have it repeat every 10 seconds
//                ReadRSS r1 = new ReadRSS("https://twitrss.me/twitter_user_to_rss/?user=cnn", textArea);
//                    Timer t1 = new Timer();
//                    t1.schedule(r1, 0, 6000);
//                return null;
//
//            }
//        };
//
//        // Start a thread with this task
//        Thread thread = new Thread(t1);
//        thread.start();

        /** --------------------------------------- WORKING CODE ----------------------------------------**/
        Button getRSS = new Button("Get RSS");
        getRSS.setPrefWidth(300);
        Task t1 = new Task() {
            @Override
            protected Object call() throws Exception {
                ReadRSS r1 = new ReadRSS("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom",textArea);
                // Set the button's action in the thread to not stop the GUI each time the button is clicked
                getRSS.setOnAction(e ->  {
                    r1.run();
                });
                return null;
            }
        };

        Thread thread = new Thread(t1);
        thread.start();
        /** ----------------------------------------------------------------------------------------------**/


//        Task t1 = new Task() {
//            @Override
//            protected Object call() throws Exception {
//                readRSS("https://twitrss.me/twitter_user_to_rss/?user=cnn",textArea);
//                return null;
//            }
//        };





        // Add things to scene
        vBox.getChildren().addAll(textArea,getRSS);
        Scene s1 = new Scene(vBox,300,600);
        stage.setScene(s1);
        stage.setResizable(false);
        stage.setTitle("RSS Feed");
        stage.show();


    }

    /**
     * Read RSS method which takes a URL and a text area and updates the textArea with current RSS
     * @param urlAddress
     * @param t1
     * @return
     * @throws Exception
     */
    public static String readRSS(String urlAddress, TextArea t1) throws Exception {
        URL rssURL = new URL(urlAddress);
        BufferedReader reader = new BufferedReader(new InputStreamReader(rssURL.openStream()));
        String sourceCode = "";
        String line;
        int lineCount = 0;
        // Reset text so that only the current updated text shows up, not the old text in addition to the new text
        t1.setText("");

        // Read through
        while ((line = reader.readLine()) != null) {
            if ((line = reader.readLine()) == null) {
                System.out.println("LINE 80 IS WHERE IT'S HAPPENING");
            }
            String line2 = reader.readLine();
            //REPLACE SPECIAL CHARACTERS
            line = line.replace("&#x22;","");
            line = line.replace("&#x27;","");
            line = line.replace("&#x2019;","\'");

            //TODO ---- ADD CODE TO READ THROUGH THE <updated> </updated> lines which contain the times of the quakes

            // Read through "Title" lines
            if (line.contains("title")) {
                line = line.substring(line.indexOf("<title>"),line.indexOf("</title>"));
                System.out.println("Title: " + line.replace("<title>","").replace("</title>","").strip());
                t1.appendText("Title: " + line.replace("<title>","").replace("</title>","").strip());
                t1.appendText("\n\n");
//                sourceCode += line + "\n";
                lineCount++;
            }
            // Read through "pubDate" Lines
//            if (line.contains("pubDate")) {
//                System.out.println("Date: " + line.replace("<pubDate>","").replace("</pubDate>","").strip());
////                t1.appendText("Date: " + line.replace("<pubDate>","").replace("</pubDate>","").strip());
////                sourceCode += line + "\n\n";
//                lineCount++;
//            }

            // Stop reading after 6 lines
            if (lineCount > 3) {
                break;
            }
        }
        System.out.println("\n");
        t1.appendText("\n");
        reader.close();
        return sourceCode;

    }

    public class ReadRSS extends TimerTask {
        String webURL;
        TextArea textArea;

        public ReadRSS(String url, TextArea t1) {
            this.webURL = url;
            textArea = t1;
        }

        @Override
        public void run() {
            try {
                readRSS(webURL, textArea);
            } catch (Exception e) {
                System.out.println("LINE 126 IS WHERE IT'S HAPPENING");
                e.printStackTrace();

            }
        }
    }
}