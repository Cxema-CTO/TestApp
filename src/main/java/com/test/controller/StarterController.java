package com.test.controller;

import com.test.repository.FileManager;
import com.test.repository.FileManagerImpl;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ResourceBundle;

import static com.test.data.Constants.*;

public class StarterController extends Application {

    private final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    ResourceBundle bundle = ResourceBundle.getBundle("app");
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        LOGGER.info("Start application");

        FileManager fileManager = new FileManagerImpl();
        fileManager.checkBeforeStart();

        Button externalButton = new Button(EXTERNAL_DB_FOLDER);
        externalButton.setFocusTraversable(false);
        externalButton.setOnAction(event -> {
            showDbStage(EXTERNAL_DB_FOLDER);
        });

        Button internalButton = new Button(INTERNAL_DB_FOLDER);
        internalButton.setFocusTraversable(false);
        internalButton.setOnAction(event -> {
           showDbStage(INTERNAL_DB_FOLDER);
        });

        ImageView imageView = new ImageView();
        Image image = new Image("logo.png");
        imageView.preserveRatioProperty();
        imageView.setFitHeight(100);
        imageView.setFitWidth(840);
        imageView.setImage(image);


        // todo edit this ugly Label :)
        Label labelCenter = new Label("The task involves creating a codebase to manage an employee database stored in XML files.\n" +
                "Each XML file contains data for an individual employee.\n" +
                "Employees are categorized into two directories: External (external employees) and Internal (internal employees), indicating their type.\n" +
                "\n" +
                "Each employee has the following attributes: Identifier, First Name, Last Name, Phone Number, Email,\n" +
                "PESEL (Polish National Identification Number).\n" +
                "\n" +
                "\n" +
                "Data type definition:\n" +
                "class Person {\n" +
                "    String personId, firstName, lastName, mobile, email, pesel;\n" +
                "}\n" +
                "\n" +
                "The operations to be supported include:\n" +
                "1. Find an employee by any attribute: identifier, type, last name, first name, phone number,\n" +
                "PESEL (or any combination of these attributes).\n" +
                "2. Add a new employee.\n" +
                "3. Remove an employee.\n" +
                "4. Modify employee data.\n" +
                "\n" +
                "Example functions to be implemented:\n" +
                "Person find(Type type, String firstName, String lastName, String mobile);\n" +
                "void create(Person person);\n" +
                "boolean remove(String personId);\n" +
                "void modify(Person person);");
        labelCenter.setFont(new Font("Arial", 14));
        labelCenter.setWrapText(true);
        labelCenter.setPadding(new Insets(4, 12, 4, 12));
        labelCenter.setTextAlignment(TextAlignment.JUSTIFY);

        Label labelBottom = new Label("If this is the first time running the program on this device, we will now" +
                " generate two databases with entities 'Person', " + QUANTITY_TEST_PERSONS + "(you can change quantity in Constants.java)" +
                " in each folders 'External' and 'Internal' within the program's directory.");
        labelBottom.setFont(new Font("Arial", 12));
        labelBottom.setWrapText(true);
        labelBottom.setPadding(new Insets(4, 12, 4, 12));
        labelBottom.setTextAlignment(TextAlignment.JUSTIFY);
        labelBottom.setStyle("-fx-font-weight: bold;");


        externalButton.setMinWidth(200);
        internalButton.setMinWidth(200);
        HBox hbox  = new HBox();
        hbox.setSpacing(10);
        hbox.setStyle(" -fx-alignment: center;");
        hbox.getChildren().addAll(externalButton,internalButton);

        VBox box = new VBox();
        box.setPadding(new Insets(4, 12, 4, 12));
        box.setSpacing(10);
        box.setStyle(" -fx-alignment: center;");
        box.getChildren().addAll(imageView, labelCenter, labelBottom, hbox);

        this.stage = stage;
        stage.setTitle(bundle.getString("app.title"));
        stage.getIcons().add(new Image("images" + File.separator + "cat.png"));
        stage.setScene(new Scene(box, 880, 680));
        stage.setResizable(false);

        stage.show();
    }

    void showDbStage(String db){
        stage.close();
        MainController mainController = new MainController();
        mainController.runDbStage(db);
    }

}// end of class
