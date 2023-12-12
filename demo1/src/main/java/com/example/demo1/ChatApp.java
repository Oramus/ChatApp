package com.example.demo1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.util.Random;

public class ChatApp extends Application {

    private ListView<ChatMessage> chatArea; // the area where the chat messages are displayed
    private TextField inputField; // the field where the user types a message
    private Button sendButton; // the button to send a message
    private Label headerLabel; // the label to display the user name
    private Random random; // a random object to generate random responses

    @Override
    public void start(Stage primaryStage) throws Exception {
        // initialize the random object
        random = new Random();
        HBox topBox = new HBox();
        topBox.setSpacing(10);

        FileInputStream i1 = new FileInputStream("src/main/resources/images/cat-modified.png");
        Image image1 = new Image(i1);
        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitHeight(50);
        imageView1.setFitWidth(50);

        headerLabel = new Label("My cat \n" + "Online");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        topBox.getChildren().addAll(imageView1, headerLabel);

// Set the right margin of imageView1 to 100
        topBox.setMargin(headerLabel, new Insets(0, 150, 0, 0));

        FileInputStream i2 = new FileInputStream("src/main/resources/images/call.png");
        Image image2 = new Image(i2);
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitHeight(50);
        imageView2.setFitWidth(50);
        topBox.getChildren().addAll(imageView2);

        FileInputStream i3 = new FileInputStream("src/main/resources/images/setting.png");
        Image image3 = new Image(i3);
        ImageView imageView3 = new ImageView(image3);
        imageView3.setFitHeight(50);
        imageView3.setFitWidth(50);
        topBox.getChildren().addAll(imageView3);



        chatArea = new ListView<>();
        chatArea.setEditable(false); // the user cannot edit the chat area
        chatArea.setCellFactory(param -> new ChatCell()); // use custom cell factory to display chat messages

        inputField = new TextField();
        inputField.setPromptText("Type a message here..."); // a prompt text to guide the user
        inputField.setOnAction(e -> sendMessage()); // send the message when the user presses ENTER
        inputField.setPrefWidth(330); // set the preferred width of the input field
        inputField.setPrefHeight(30); // set the preferred height of the input field

        sendButton = new Button();
        FileInputStream input = new FileInputStream("src/main/resources/images/send-message-removebg-preview.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        sendButton.setGraphic(imageView);
        sendButton.setStyle("-fx-border-color: transparent; -fx-background-color: transparent; -fx-outline: none;");
        sendButton.setOnAction(e -> sendMessage()); // send the message when the user clicks the button

        HBox inputBox = new HBox(10);
        //inputBox.setStyle("-fx-background-color: linear-gradient(to bottom , white, lightblue);");
        inputBox.setStyle("-fx-background-color: lightblue;");
        inputBox.setPadding(new Insets(10));
        inputBox.getChildren().addAll(inputField, sendButton);

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(chatArea);
        root.setBottom(inputBox);

        // create a scene with the border pane as the root node
        Scene scene = new Scene(root, 400, 600);

        // set the scene to the stage and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("ChatApp");
        primaryStage.show();
    }

    // a method to send a message
    private void sendMessage() {
        // get the text from the input field
        String message = inputField.getText();

        // if the message is not empty
        if (!message.isEmpty()) {
            // create a chat message object with the message and the type USER
            ChatMessage userMessage = new ChatMessage(message, ChatMessage.Type.USER);

            // add the message to the chat area
            chatArea.getItems().add(userMessage);

            // clear the input field
            inputField.clear();

            // generate a random response from the assistant
            ChatMessage response = generateResponse();

            // add the response to the chat area
            chatArea.getItems().add(response);
        }
    }

    // a method to generate a random response from the assistant
    private ChatMessage generateResponse() {
        // an array of possible responses
        String[] responses = {
                "Hello, this is Bing.",
                "How are you today?",
                "That's interesting.",
                "I'm sorry, I don't understand.",
                "Can you tell me more?",
                "Thank you for chatting with me.",
                "Have a nice day."
        };

        // get a random index from the array
        int index = random.nextInt(responses.length);

        // create a chat message object with the response and the type ASSISTANT
        ChatMessage response = new ChatMessage(responses[index], ChatMessage.Type.ASSISTANT);

        // return the response
        return response;
    }

    // the main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}

// a class to represent a chat message
class ChatMessage {
    // an enum to represent the type of the message
    enum Type {
        USER, // the message is from the user
        ASSISTANT // the message is from the assistant
    }

    private String content; // the content of the message
    private Type type; // the type of the message

    // a constructor to create a chat message with content and type
    public ChatMessage(String content, Type type) {
        this.content = content;
        this.type = type;
    }

    // a getter method to get the content of the message
    public String getContent() {
        return content;
    }

    // a getter method to get the type of the message
    public Type getType() {
        return type;
    }
}

// a class to customize the display of chat messages in a list view
class ChatCell extends ListCell<ChatMessage> {
    @Override
    protected void updateItem(ChatMessage item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            // create a label to display the content of the message
            Label label = new Label(item.getContent());
            label.setWrapText(true); // the text will wrap to the next line if it exceeds the width

            // create a horizontal box to hold the label
            HBox hbox = new HBox(label);
            hbox.setPadding(new Insets(10));

            // set the style class of the label and the hbox according to the type of the message
            if (item.getType() == ChatMessage.Type.USER) {
                label.setStyle("-fx-background-color: purple; -fx-background-radius: 10; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-font: 16px \"Comic Sans MS\";");
                hbox.setAlignment(Pos.CENTER_RIGHT); // align the hbox to the right
            } else { // the message is from the assistant
                label.setStyle("-fx-background-color: blue; -fx-background-radius: 10; -fx-text-fill: white; -fx-padding: 5 10 5 10; -fx-font: 16px \"Comic Sans MS\";");
                hbox.setAlignment(Pos.CENTER_LEFT); // align the hbox to the left//
            }
                setGraphic(hbox);

        }
    }
}
