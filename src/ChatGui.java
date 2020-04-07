import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatGui extends Application {
    boolean isServer;
    boolean isClient;
    boolean serverReady;
    int serverPort = -1;
    String ipAddress = "";
    private Object Menu;
    boolean startOk = false;

    public boolean checkIfOk() {
        if (isServer && (serverPort >= 0)) {
            startOk = true;
            return true;
        }
        else if (isClient && (serverPort >= 0) && !(ipAddress.compareTo("") == 0)) {
            startOk = true;
           return true;
        }
        else {
            startOk = false;
            return false;
        }
    }

    class Menu {
        //Stage menuStage;
        Menu() {
            Stage MenuStage = new Stage();
            MenuStage.setTitle("Menu");
            BorderPane borderPane = new BorderPane();
            VBox vBox = new VBox();

            ToggleGroup clientCheck = new ToggleGroup();

            // Client check button area
            HBox clientBox = new HBox();
            RadioButton amClient = new RadioButton();
            amClient.setOnAction(e -> {
                isClient = true;
                isServer = false;
            } );
            TextField enterIP = new TextField();
            TextField enterPort = new TextField();
            Text clientText = new Text("Enter Ip / server port to connect to");
            Button sendIP = new Button();
            sendIP.setText("Send IP");
            sendIP.setOnAction(e -> ipAddress = enterIP.getText());
            Button sendPort = new Button();
            sendPort.setText("Send Port");
            sendPort.setOnAction(e -> serverPort = Integer.parseInt(enterPort.getText()));
            clientBox.getChildren().addAll(amClient,clientText,enterIP,sendIP,enterPort,sendPort);

            // Server check button
            HBox serverBox = new HBox();
            RadioButton amServer = new RadioButton();
            amServer.setOnAction(e -> {
                isServer = true;
                isClient = false;
            });
            TextField enterServerPort = new TextField();
            Text serverText = new Text("Enter your desired server port");
            Button sendServerPort = new Button("Send Server Port");
            sendServerPort.setOnAction(e -> serverPort = Integer.parseInt(enterServerPort.getText()));
            serverBox.getChildren().addAll(amServer,serverText,enterServerPort,sendServerPort);


            amClient.setToggleGroup(clientCheck);
            amServer.setToggleGroup(clientCheck);



            // Text field to allow the chatbox to start or not based on info added
            TextField errorReport = new TextField();

            // Start button
            Button startButton = new Button("Start");
            startButton.setOnAction(e -> {
                checkIfOk();
                if (startOk) {
                    MenuStage.close();

                    if (isServer) {
                        try {
                            ChatBotServer cbServer = new ChatBotServer();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    else {
                        try {
                            ChatBotClient cbClient = new ChatBotClient();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else {
                    Text notReady = new Text("Please enter the needed info before starting");
                    notReady.setFill(Color.RED);
                    errorReport.setText(notReady.getText());
                }
            });


            // Add things to menu
            vBox.getChildren().addAll(serverBox,clientBox);
            borderPane.setLeft(vBox);
            borderPane.setBottom(errorReport);
            borderPane.setTop(startButton);

            Scene s1 = new Scene(borderPane,1000,300);
            MenuStage.setScene(s1);
            MenuStage.show();
            //menuStage = MenuStage;
        }

        public void showMenu(Menu m) {
            m = new Menu();
        }
    }

    class ChatBotServer {
        ChatBotServer() throws IOException {
            Stage chatBotServer = new Stage();
            chatBotServer.setTitle("Chat Bot Server");
            VBox vB = new VBox();

            TextField chatWindow = new TextField();
            chatWindow.setEditable(false);

            // Chat Entry HBox
            HBox chatArea = new HBox();
            TextField chatEntry = new TextField();
            Button sendChat = new Button("Send");
            chatArea.getChildren().addAll(chatEntry,sendChat);

            // Total chat vBox
            vB.getChildren().addAll(chatWindow,chatArea);

            /** CHAT FUNCTIONALITY **/
            ServerSocket mySocket = new ServerSocket(serverPort);

            Scene s1 = new Scene(vB);
            chatBotServer.setScene(s1);
            chatBotServer.show();

            // Wait for connection //QUESTION: I think this is where my stallng is coming from.
            while(true) {
                Socket connectionSocket = mySocket.accept();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));

                // Set button action to send message and display it on chat box
                sendChat.setOnAction(e -> {
                    try {
                        sendMessage(writer,chatEntry.getText());
                        chatEntry.setText("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                // Display chat messages
                chatWindow.setText(chatWindow.getText() + "Server) " + reader.readLine().trim() + "\n");
            }

        }

        public void sendMessage(BufferedWriter writer, String message) throws IOException {
            writer.write(message);
            writer.flush();
        }
    }

    class ChatBotClient {
        ChatBotClient() throws IOException {
            Stage chatBotClient = new Stage();
            chatBotClient.setTitle("Chat Bot Client");
            VBox vB = new VBox();

            TextField chatWindow = new TextField();
            chatWindow.setEditable(false);

            // Chat Entry HBox
            HBox chatArea = new HBox();
            TextField chatEntry = new TextField();
            Button sendChat = new Button("Send");
            chatArea.getChildren().addAll(chatEntry,sendChat);

            // Total chat vBox
            vB.getChildren().addAll(chatWindow,chatArea);

            /** CHAT FUNCTIONALITY **/
            Socket socketClient = null;
            try {
                socketClient = new Socket(ipAddress,serverPort);
                chatWindow.setText(chatWindow.getText() + "\nConnecting to client\n");
            }
            catch (Exception ex) {
                chatWindow.setText(chatWindow.getText() + "\nclient can't be connected to\n");
            }

            Socket connectionSocket = socketClient;

            BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));

            // Set button action to send message and display it on chat box
            sendChat.setOnAction(e -> {
                try {
                    sendMessage(writer,chatEntry.getText());
                    chatEntry.setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            //Display chat messages
            chatWindow.setText(chatWindow.getText() + "Client) " + reader.readLine().trim() + "\n");
        }

        public void sendMessage(BufferedWriter writer, String message) throws IOException {
            writer.write(message);
            writer.flush();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        if (!startOk) {
            Menu menu = new Menu();
        }

    }
}