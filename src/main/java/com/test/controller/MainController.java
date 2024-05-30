package com.test.controller;

import com.test.entity.Person;
import com.test.service.PersonService;
import com.test.service.PersonServiceImpl;
import com.test.util.RandomPersonGeneratorImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.test.data.Constants.EXTERNAL_DB_FOLDER;
import static com.test.data.Constants.INTERNAL_DB_FOLDER;

public class MainController {

    private final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    Stage stage = new Stage();
    private String activeDatabase = "";
    private String findBy = "First name";
    private long activePersonId = 0;
    List<Person> items = new ArrayList<>();


    public void runDbStage(String db) {
        activeDatabase = db;
        stage.setScene(getMainScene());
        stage.getIcons().add(new Image("images" + File.separator + "cat.png"));
        //        stage.setMaximized(true);
        stage.setWidth(1024);
        stage.setHeight(640);
        stage.setMinWidth(1024);
        stage.setMinHeight(640);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
        stage.setResizable(true);
        stage.show();
        LOGGER.info("Run DbStage");
    }


    private Scene getMainScene() {
        BorderPane pane = new BorderPane();
        pane.setTop(addTopVbox());
        pane.setCenter(addCenterTableView());
        pane.setBottom(addBottomHBox());
        return new Scene(pane);
    }

    private TableView<Person> addCenterTableView() {
//        RandomPersonGenerator rpg = new RandomPersonGeneratorImpl();
//        List<Person> items = rpg.generateRandomPersons((int) QUANTITY_TEST_PERSONS);

        PersonService personService = new PersonServiceImpl();
        items = personService.getAllPersonFromDB(activeDatabase);

        TableView<Person> table = new TableView<>();

        TableColumn<Person, Integer> idCol = new TableColumn<>("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<>("personId"));
        idCol.setMinWidth(50);
        idCol.setMaxWidth(50);
        idCol.setStyle("-fx-alignment: center ;");
        TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TableColumn<Person, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Person, String> mobileCol = new TableColumn<>("Phone");
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mobileCol.setMinWidth(120);
        mobileCol.setMaxWidth(120);
        mobileCol.setStyle("-fx-alignment: center ;");
        TableColumn<Person, Long> peselCol = new TableColumn<>("Pesel");
        peselCol.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        peselCol.setMinWidth(110);
        peselCol.setMaxWidth(110);
        peselCol.setStyle("-fx-alignment: center ;");

        table.getItems().addAll(items);
        table.getColumns().addAll(idCol, firstNameCol, lastNameCol, emailCol, mobileCol, peselCol);
        table.getSortOrder().add(idCol);
        table.sort();
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setMaxWidth(1200);
        table.setOnMouseClicked(event -> {
            showPersonInDb(table.getSelectionModel().getSelectedItem());
        });
        table.setOnKeyReleased(event -> {
            showPersonInDb(table.getSelectionModel().getSelectedItem());
        });
        activePersonId = 0;
        return table;
    }

    private void showPersonInDb(Person person) {
        if (person != null) {
            Label label = (Label) stage.getScene().lookup("#labelPerson");
            label.setText(person.toStringToShowInDb());
            activePersonId = person.getPersonId();
        }
    }


    private VBox addTopVbox() {
        Menu menu = new Menu("Database");
        MenuItem menuItem = new MenuItem("todo Clear external database");
        MenuItem menuItem2 = new MenuItem("todo Clear internal database");
        MenuItem menuItem3 = new MenuItem("todo Change the directory for the database");
        menu.getItems().addAll(menuItem, menuItem2, menuItem3);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        final VBox vbox = new VBox();
        vbox.setFillWidth(true);
        vbox.getChildren().addAll(addTopHBox());
        return vbox;
    }


    private HBox addTopHBox() {
        stage.setTitle(activeDatabase);
        Label label = new Label(activeDatabase);
        label.setId("label");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

        Button buttonExternal = new Button(EXTERNAL_DB_FOLDER);
        buttonExternal.setPrefSize(100, 20);
        buttonExternal.setId("buttonExternal");
        buttonExternal.setOnAction(event -> {
            showActiveDb(EXTERNAL_DB_FOLDER);
        });

        Button buttonInternal = new Button(INTERNAL_DB_FOLDER);
        buttonInternal.setId("buttonInternal");
        buttonInternal.setPrefSize(100, 20);
        buttonInternal.setOnAction(event -> {
            showActiveDb(INTERNAL_DB_FOLDER);
        });

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(4, 12, 4, 12));
        hbox.setSpacing(12);
        hbox.setStyle("-fx-background-color: #ced1d4; -fx-alignment: center ;");
        hbox.getChildren().addAll(buttonExternal, buttonInternal, region, label);
        return hbox;
    }

    private void showActiveDb(String dbName) {
        if (!activeDatabase.equals(dbName)) {
            activeDatabase = dbName;
            stage.setScene(getMainScene());
            stage.show();
        }
    }

    private void reloadActiveDb(String dbName) {
        activeDatabase = dbName;
        stage.setScene(getMainScene());
        stage.show();
    }

    private HBox addBottomHBox() {
        double btnWidth = 64;
        Button btnFind = new Button("Find");
        btnFind.setMinWidth(btnWidth);
        btnFind.setOnAction(event -> {
            showModalForFindPerson();
        });
        Button btnEdit = new Button("Edit");
        btnEdit.setMinWidth(btnWidth);
        btnEdit.setOnAction(event -> {
            showEditDialog(false);
        });
        Button btnAdd = new Button("Add");
        btnAdd.setMinWidth(btnWidth);
        btnAdd.setOnAction(event -> {
            showEditDialog(true);
        });
        Button btnDelete = new Button("Delete");
        btnDelete.setMinWidth(btnWidth);
        btnDelete.setOnAction(event -> {
            showDeleteDialog();
        });

        Label label = new Label();
        label.setId("labelPerson");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14");

        Region region = new Region();
        Region region2 = new Region();


        HBox hbox = new HBox();
        hbox.setPadding(new Insets(8, 12, 8, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #ced1d4;-fx-alignment: center ;");
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox.setHgrow(region2, Priority.ALWAYS);
        hbox.getChildren().addAll(btnFind, region, label, region2, btnEdit, btnAdd, btnDelete);
        return hbox;
    }

    private void showDeleteDialog() {
        if (activePersonId > 0) {
            showModalForDeleteUser();
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Delete");
            a.setHeaderText("");
            a.setResizable(false);
            a.setContentText("Nothing selected for delete!");
            a.showAndWait();
        }
    }

    private void showEditDialog(boolean newPerson) {
        if (!newPerson) {
            if (activePersonId > 0) {
                showModalForEditPerson(false);
            } else {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Edit");
                a.setHeaderText("");
                a.setResizable(false);
                a.setContentText("Nothing selected for edit!");
                a.showAndWait();
            }
        } else {
            showModalForEditPerson(true);
        }
    }

    private void showModalForFindPerson() {
        double modalWidth = 480;
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setAlwaysOnTop(true);
        Label label = new Label("Find Person");
        label.prefWidth(modalWidth - 20);
        label.setWrapText(true);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14");

        TextField tfSearchValue = new TextField();

        // todo add Constants
        ObservableList<String> attributes = FXCollections.observableArrayList("First name", "Last name", "Email", "Phone", "Pesel");
        ComboBox<String> attrComboBox = new ComboBox<>(attributes);
        attrComboBox.setValue(findBy);
        Label lblComboBox = new Label();
        attrComboBox.setOnAction(event -> {
            findBy = attrComboBox.getValue();
            lblComboBox.setText(findBy);
        });

        Button btnFind = new Button("Find");
        btnFind.setMinWidth(64);
        btnFind.setOnAction(event -> {
            Person result = findPersonInDb(findBy, tfSearchValue.getText());
            if (Objects.equals(result, null)) {
                label.setText("no results...");
            } else {
                label.setText(result.toString());
            }
        });

        Button btnClose = new Button("Close");
        btnClose.setMinWidth(64);
        btnClose.setOnAction(event -> {
            modal.close();
        });

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(16, 0, 0, 12));
        bottomBox.setSpacing(10);
        bottomBox.getChildren().addAll(btnFind, region, btnClose);

        VBox box = new VBox();
        box.setPadding(new Insets(4, 12, 4, 12));
        box.setSpacing(10);
        box.setStyle(" -fx-alignment: center;");
        box.getChildren().addAll(label, tfSearchValue, attrComboBox, bottomBox);

        modal.setTitle("Find person");
        modal.getIcons().add(new Image("images" + File.separator + "cat.png"));
        modal.setScene(new Scene(box, modalWidth, 240));
        modal.setResizable(false);
        modal.showAndWait();
    }

    private Person findPersonInDb(String attribute, String value) {
        String attrName = switch (attribute) {// todo add Constants
            case "First name" -> "firstName";
            case "Last name" -> "lastName";
            case "Email" -> "email";
            case "Phone" -> "mobile";
            case "Pesel" -> "pesel";
            default -> "firstName";
        };
        return findPersonByAttribute(items, attrName, value);
    }

    private <T> T findPersonByAttribute(List<T> list, String attributeName, String attributeValue) {
        for (T obj : list) {// ohn my god :)
            try {
                var field = obj.getClass().getDeclaredField(attributeName);
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value.equals(attributeValue)) {
                    LOGGER.info("Find Person - " + obj);
                    return obj;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private void showModalForDeleteUser() {
        Person person = new PersonServiceImpl().getPersonById(activeDatabase, activePersonId);

        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setAlwaysOnTop(true);
        Label label = new Label();
        label.setPadding(new Insets(16, 16, 4, 16));
        label.setText("Delete " + person.getFirstName() + " " + person.getLastName() + "?");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

        Button btnDelete = new Button("Delete");
        btnDelete.setMinWidth(64);
        btnDelete.setOnAction(event -> {
            PersonService personService = new PersonServiceImpl();
            personService.deletePerson(activeDatabase, person);
            reloadActiveDb(activeDatabase);
            modal.close();
        });

        Button btnClose = new Button("Close");
        btnClose.setMinWidth(64);
        btnClose.setOnAction(event -> {
            modal.close();
        });

        ImageView imageView = new ImageView();
        Image image = new Image("images" + File.separator + "smile.gif");
        imageView.preserveRatioProperty();

        imageView.setFitHeight(90);
        imageView.setFitWidth(95);
        imageView.setImage(image);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(16, 0, 0, 12));
        bottomBox.setSpacing(10);
        bottomBox.getChildren().addAll(region, btnDelete, btnClose);

        VBox box = new VBox();
        box.setPadding(new Insets(4, 12, 4, 12));
        box.setSpacing(10);
        box.setStyle(" -fx-alignment: center;");
        box.getChildren().addAll(imageView, label, bottomBox);

        modal.setTitle("Delete person");
        modal.getIcons().add(new Image("images" + File.separator + "cat.png"));
        modal.setScene(new Scene(box, 480, 220));
        modal.setResizable(false);
        modal.showAndWait();

    }

    private void showModalForEditPerson(boolean newPerson) {
        Person person = new Person();
        if (newPerson) person = new RandomPersonGeneratorImpl().generateRandomPerson();
        if (!newPerson) person = new PersonServiceImpl().getPersonById(activeDatabase, activePersonId);
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setAlwaysOnTop(true);
        Label label = new Label("Edit Person with id." + person.getPersonId());
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

        Label lbFirstName = new Label("first name");
        TextField tfFirstName = new TextField(person.getFirstName());
        Label lbLastName = new Label("last name");
        TextField tfLastName = new TextField(person.getLastName());
        Label lbEmail = new Label("email");
        TextField tfEmail = new TextField(person.getEmail());
        Label lbMobile = new Label("phone");
        TextField tfMobile = new TextField(person.getStringMobile());
        Label lbPesel = new Label("pesel");
        TextField tfPesel = new TextField(person.getStringPesel());

        Button btnSave = new Button("Save");
        btnSave.setMinWidth(64);
        Person editedPerson = person;
        btnSave.setOnAction(event -> {
            //todo check data
            editedPerson.setFirstName(tfFirstName.getText());
            editedPerson.setLastName(tfLastName.getText());
            editedPerson.setEmail(tfEmail.getText());
            editedPerson.setMobile(tfMobile.getText());
            editedPerson.setPesel(tfPesel.getText());
            PersonService personService = new PersonServiceImpl();
            personService.savePerson(activeDatabase, editedPerson);
            modal.close();
            reloadActiveDb(activeDatabase);
        });
        Button btnClose = new Button("Close");
        btnClose.setMinWidth(64);
        btnClose.setOnAction(event -> {
            modal.close();
        });

        ImageView imageView = new ImageView();
        Image image = new Image("images" + File.separator + "fly.gif");
        imageView.preserveRatioProperty();
        imageView.setFitHeight(48);
        imageView.setFitWidth(36);
        imageView.setImage(image);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(16, 0, 0, 12));
        bottomBox.setSpacing(10);
        bottomBox.getChildren().addAll(imageView, region, btnSave, btnClose);


        VBox box = new VBox();
        box.setPadding(new Insets(4, 12, 4, 12));
        box.setSpacing(10);
        box.setStyle(" -fx-alignment: center;");
        box.getChildren().addAll(label, lbFirstName, tfFirstName, lbLastName, tfLastName,
                lbEmail, tfEmail, lbMobile, tfMobile, lbPesel, tfPesel, bottomBox);

        modal.setTitle("Edit person");
        modal.getIcons().add(new Image("images" + File.separator + "cat.png"));
        modal.setScene(new Scene(box, 360, 430));
        modal.setResizable(false);
        modal.showAndWait();
    }


}// end of class
