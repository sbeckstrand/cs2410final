import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  TABLE OF CONTENTS
 *
 *  class ChatGui
 *  checkIfOK() method
 *     class Menu (for setup)
 *     class ChatBotServer (the chatbox that pops up when you choose the radio button for server)
 *     Class ChatBotClient (the chatbox that pops up when you choose the radio button for client)
 *  Start() method
 */

public class ChatGui extends Application {
    boolean isServer;
    boolean isClient;
    boolean serverReady;
    int serverPort = -1;
    String ipAddress = "";
    private Object Menu;
    boolean startOk = false;

    // Method to check if the GUI chat is okay to start and all info is entered.
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

    /**
     * Menu class which brings up the options for someone to connect as a Server or a Client
     */
    class Menu {
        //Stage menuStage;
        Menu() {
            // Stage Setup
            Stage MenuStage = new Stage();
            MenuStage.setTitle("Menu");
            BorderPane borderPane = new BorderPane();
            VBox vBox = new VBox();

            ToggleGroup clientCheck = new ToggleGroup();

            // Client check button area
            HBox clientBox = new HBox();
            Text clientOption = new Text("Client - ");
            RadioButton amClient = new RadioButton();
            amClient.setOnAction(e -> {
                isClient = true;
                isServer = false;
            } );

            // Text Fields for all the data being entered (FOR CLIENT)
            TextField enterIP = new TextField();
            TextField enterPort = new TextField();
            Text clientText = new Text("Enter Ip / server port to connect to");
            Button sendIP = new Button();
            sendIP.setText("Send IP");
            sendIP.setOnAction(e -> ipAddress = enterIP.getText());
            Button sendPort = new Button();
            sendPort.setText("Send Port");
            sendPort.setOnAction(e -> serverPort = Integer.parseInt(enterPort.getText()));
            clientBox.getChildren().addAll(clientOption,amClient,clientText,enterIP,sendIP,enterPort,sendPort);

            // Server check button
            HBox serverBox = new HBox();
            Text serverOption = new Text("Server - ");
            RadioButton amServer = new RadioButton();
            amServer.setOnAction(e -> {
                isServer = true;
                isClient = false;
            });
            // Text Fields for all the data being entered (FOR SERVER)
            TextField enterServerPort = new TextField();
            Text serverText = new Text("Enter your desired server port");
            Button sendServerPort = new Button("Send Server Port");
            sendServerPort.setOnAction(e -> serverPort = Integer.parseInt(enterServerPort.getText()));
            serverBox.getChildren().addAll(serverOption,amServer,serverText,enterServerPort,sendServerPort);

            // Make the radio buttons have the same group
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

                    // Check to see if server or client is running the program
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
                // Must enter all info before clicking "start"
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

    /**
     * Class for the SERVER on the chatBot
     */
    class ChatBotServer {
        String toSend = "";
        ChatBotServer() throws IOException {
            Stage chatBotServer = new Stage();
            chatBotServer.setTitle("Chat Bot Server");
            VBox vB = new VBox();

            TextArea chatWindow = new TextArea();
            chatWindow.setPrefColumnCount(4);
            chatWindow.setPrefSize(300,500);
            //chatWindow.setEditable(false);

            // Chat Entry HBox
            HBox chatArea = new HBox();
            TextField chatEntry = new TextField();
            Button sendChat = new Button("Send");
            chatArea.getChildren().addAll(chatEntry, sendChat);

            // Total chat vBox
            vB.getChildren().addAll(chatWindow, chatArea);

            Scene s1 = new Scene(vB,300,500);
            chatBotServer.setScene(s1);
            chatBotServer.show();

            /** CHAT FUNCTIONALITY **/
            ServerSocket sv = new ServerSocket(serverPort);

            // Task which handles reading in chat messages
            Task taskRead = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    while (true) {
                        Socket sck = sv.accept();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(sck.getInputStream()));
                        String response = reader.readLine().trim();
                        chatWindow.appendText("Client) " + response + "\r\n");
                    }

                }
            };

            // Task which handles writing new chat messages
            Task taskWrite = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    //while (true) {
                        Socket sck = sv.accept();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sck.getOutputStream()));
                        writer.write(toSend + "\r\n");
                        writer.flush();
                        chatWindow.appendText("Server) " + toSend + "\r\n");
                    ///}
                    return null;

                }
            };

            chatWindow.appendText("Waiting for Client Connection" + System.getProperty("line.separator") + "\n");

            // Start the threads
            Thread th = new Thread(taskRead);
            th.start();

            // Start the "Send chat" thread when button is pressed to send a message
            sendChat.setOnAction(e -> {
                setMessage(chatEntry.getText());
                Thread th2 = new Thread(taskWrite);
                th2.start();
                chatEntry.setText("");
            });

        }

        // Set the message to send
        public void setMessage(String message) {
            toSend = message;
        }

// UNUSED TEST METHODS

//        public void sendMessage(BufferedWriter writer, String message) throws IOException {
//            writer.write(message);
//            writer.flush();
//        }
//        public void handleInput(BufferedReader nis, TextField tf) {
//            final Task<Void> task = new Task<Void>() {
//                @Override
//                protected Void call() throws Exception {
//                    String output;
//                    while ((output = nis.readLine()) != null) {
//                        final String value = output;
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                tf.setText(tf.getText() + value + "\n");
//                            }
//                        });
//                    }
//                    return null;
//                }
//            };
//            new Thread(task).start();
//        }

    }

    /**
     * Class for the CLIENT side of the chatBot
     */
    class ChatBotClient {
        String toSend = "";
        ChatBotClient() throws IOException {
            // Setup for Stage
            Stage chatBotClient = new Stage();
            chatBotClient.setTitle("Chat Bot Client");
            VBox vB = new VBox();

            TextArea chatWindow = new TextArea();
            chatWindow.setPrefColumnCount(4);
            chatWindow.setPrefSize(300,500);
            //chatWindow.setEditable(false);

            // Chat Entry HBox
            HBox chatArea = new HBox();
            TextField chatEntry = new TextField();
            Button sendChat = new Button("Send");
            chatArea.getChildren().addAll(chatEntry,sendChat);

            // Total chat vBox
            vB.getChildren().addAll(chatWindow,chatArea);

            Scene s1 = new Scene(vB,300,500);
            chatBotClient.setScene(s1);
            chatBotClient.show();

            /** CHAT FUNCTIONALITY **/
            // Task to read in messages
            Task taskRead = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    while (true) {
                        Socket sck = new Socket(ipAddress, serverPort);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(sck.getInputStream()));
                        String response = reader.readLine().trim();
                        chatWindow.appendText("Server) " + response + "\r\n");
                    }
                    //return null;

                }
            };

            // Task to send messages
            Task taskWrite = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    //while (true) {
                        Socket sck = new Socket(ipAddress, serverPort);
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sck.getOutputStream()));
                        writer.write(toSend + "\r\n");
                        writer.flush();
                        chatWindow.appendText("Client) " + toSend + "\r\n");
                    //}
                    return null;

                    }
            };

            chatWindow.appendText("Waiting for Client Connection" + System.getProperty("line.separator") + "\n");

            // Start threads
            Thread th = new Thread(taskRead);
            th.start();

            // Start of "send messages" thread when button clicked to send chats
            sendChat.setOnAction(e -> {
                setMessage(chatEntry.getText());
                Thread th2 = new Thread(taskWrite);
                th2.start();
                chatEntry.setText("");
            });

        }

        // Sets the message to send
        public void setMessage(String message) {
            toSend = message;
        }

        // UNUSED METHODS
//        public void sendMessage(BufferedWriter writer, String message) throws IOException {
//            writer.write(message);
//            writer.flush();
//        }
    }


    @Override
    public void start(Stage stage) {
        // Check if it's okay to start, and then create the Menu
        if (!startOk) {
            Menu menu = new Menu();
        }
    }
}
