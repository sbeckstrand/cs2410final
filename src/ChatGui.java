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

            //while (true) {

            chatWindow.appendText("Waiting for Client Connection" + System.getProperty("line.separator") + "\n");

            Thread th = new Thread(taskRead);
            //th.setDaemon(true);
            th.start();

            sendChat.setOnAction(e -> {
                setMessage(chatEntry.getText());
                Thread th2 = new Thread(taskWrite);
                th2.start();
                chatEntry.setText("");
            });
            //reader = new BufferedReader(new InputStreamReader(sck.getInputStream()));
//                sck = sv.accept();
//                writer = new BufferedWriter(new OutputStreamWriter(sck.getOutputStream()));
//                while (true) {
//
////                    if (!reader.readLine().equals("")) {
////                        response = reader.readLine().trim();
////                        chatWindow.setText(chatWindow.getText() + "Client) " + response + "\r\n");
////                    }
//
//                    // Set chat send button functionality
//                    sendChat.setOnAction(e -> {
//                        try {
//                            writer.write(send + "\r\n");
//                            writer.flush();
//                        } catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
//                    });
//                }
        }
//    }
        public void setMessage(String message) {
            toSend = message;
        }


        public void sendMessage(BufferedWriter writer, String message) throws IOException {
            writer.write(message);
            writer.flush();
        }
        public void handleInput(BufferedReader nis, TextField tf) {
            final Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    String output;
                    while ((output = nis.readLine()) != null) {
                        final String value = output;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                tf.setText(tf.getText() + value + "\n");
                            }
                        });
                    }
                    return null;
                }
            };
            new Thread(task).start();
        }

    }

    class ChatBotClient {
        String toSend = "";
        ChatBotClient() throws IOException {
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
//            Socket socketClient = null;
//            try {
//                socketClient = new Socket(ipAddress,serverPort);
//                chatWindow.setText(chatWindow.getText() + "\nConnecting to client\n");
//            }
//            catch (Exception ex) {
//                chatWindow.setText(chatWindow.getText() + "\nclient can't be connected to\n");
//            }
//
//            Socket connectionSocket = socketClient;
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
//
//            // Set button action to send message and display it on chat box
//            sendChat.setOnAction(e -> {
//                try {
//                    sendMessage(writer,chatEntry.getText());
//                    chatEntry.setText("");
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            });
//
//            //Display chat messages
//            chatWindow.setText(chatWindow.getText() + "Client) " + reader.readLine().trim() + "\n");


//            BufferedWriter writer;
//            BufferedReader reader;
//            Socket sck;
//            String response;
//            String send = chatEntry.getText();
//
//            chatWindow.setText(chatWindow.getText() + "Waiting for Client Connection");
//            sck = new Socket(ipAddress,serverPort);
//            reader = new BufferedReader(new InputStreamReader(sck.getInputStream()));
//            writer = new BufferedWriter(new OutputStreamWriter(sck.getOutputStream()));
//
//            while (true) {
//                // Set chat send button functionality
//                sendChat.setOnAction(e -> {
//                    try {
//                        writer.write(send + "\r\n");
//                        writer.flush();
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                });
//
//                // Get server response
//                if (!reader.readLine().equals("")) {
//                    response = reader.readLine().trim();
//                    chatWindow.setText(chatWindow.getText() + "Server) " + response + "\r\n");
//                }
//
//            }

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

            Thread th = new Thread(taskRead);
            //th.setDaemon(true);
            th.start();

            sendChat.setOnAction(e -> {
                setMessage(chatEntry.getText());
                Thread th2 = new Thread(taskWrite);
                th2.start();
                chatEntry.setText("");
            });

        }
        public void setMessage(String message) {
            toSend = message;
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
