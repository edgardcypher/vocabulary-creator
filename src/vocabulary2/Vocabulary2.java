/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocabulary2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author abcd
 */
public class Vocabulary2 extends Application {

    public Integer j = 0;
    public String myNewWord, myNewDef;
    public String wordsFile = "myVocabulary.dat";
    public ObjectOutputStream out = null;
    public ObjectInputStream input = null;
    MyWords myWords[] = new MyWords[3];
    List<MyWords> allWords;
    public ListView<MyWords> lv;

    @Override
    public void start(Stage primaryStage) {
        Integer j = null;
        allWords = new ArrayList<>();
        
        Image add = new Image("add.png");
        Image edit = new Image("pencil.png");
        Image delete = new Image("close.png");
        ImageView view = new ImageView(add);
        ImageView view2 = new ImageView(edit);
        ImageView view3 = new ImageView(delete);
        view.setFitHeight(20);
        view.setFitWidth(20);
        view2.setFitHeight(20);
        view2.setFitWidth(20);
        view3.setFitHeight(20);
        view3.setFitWidth(20);

        MenuBar myBar = new MenuBar();
        Menu menu = new Menu("File");
        MenuItem load = new MenuItem("Load File");
        MenuItem save = new MenuItem("Save File");
        MenuItem exit = new MenuItem("Exit");
        menu.getItems().addAll(load, save, exit);
        myBar.getMenus().add(menu);
        Button addButton = new Button();
        addButton.setGraphic(view);
        Button editButton = new Button();
        editButton.setGraphic(view2);
        Button deleteButton = new Button();
        deleteButton.setGraphic(view3);
        Button btnOK = new Button();
        btnOK.setText("OK");
        btnOK.setVisible(false);
        Button btnCancel = new Button();
        btnCancel.setText("Cancel");
        btnCancel.setVisible(false);
        Button allbut[] = {addButton, editButton, deleteButton};
        ListView<Button> bt = new ListView<>(FXCollections.observableArrayList(allbut));
        bt.setPrefSize(70, 70);
        bt.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //end my button pane
        TextArea myDefinition = new TextArea();//text area
        myDefinition.setEditable(false);
        TextField myText = new TextField();
        myText.setEditable(false);
        myText.setAlignment(Pos.CENTER);
        BorderPane myBorder = new BorderPane();
        VBox myV = new VBox();
        HBox myH = new HBox();
        HBox myBt = new HBox();
        myBt.setAlignment(Pos.BOTTOM_CENTER);
        myBt.getChildren().addAll(btnOK, btnCancel);
        myV.getChildren().addAll(myText, myDefinition, myBt);
        myBorder.setTop(myBar);
        myBorder.setCenter(myH);
        load.setOnAction((ActionEvent event) -> {
            load.setVisible(false);
            try {
                try (ObjectInputStream inputt = new ObjectInputStream(new FileInputStream(wordsFile))) {
                    while (true) {
                        List<MyWords> newNumbers = (List<MyWords>) (inputt.readObject());
                        for (int i = 0; i < newNumbers.size(); i++) {
                            System.out.println(newNumbers.get(i).getWord() + " " + newNumbers.get(i).getDefn());
                            myWords[i] = new MyWords();
                            myWords[i].setWordAndDefn(newNumbers.get(i).getWord(), newNumbers.get(i).getDefn());
                            allWords.add(myWords[i]);
                        }
                    }
                } catch (StreamCorruptedException e) {
                    System.out.printf("End of file");
                }
                lv = new ListView<>(FXCollections.observableArrayList(allWords));
                lv.setPrefSize(100, 100);
                lv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                lv.setEditable(true);

                lv.getSelectionModel().selectedItemProperty().addListener(o -> {
                    for (Integer i : lv.getSelectionModel().getSelectedIndices()) {
                        myDefinition.setText(allWords.get(i).getDefn());
                        myText.setText(allWords.get(i).getWord());
                        this.j = i;
                        myDefinition.setEditable(false);
                        myText.setEditable(false);
                    }
                });

                myH.getChildren().addAll(lv, myV, bt);
                //input.close();

            } catch (IOException ex) {
                Logger.getLogger(Vocabulary2.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Vocabulary2.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        addButton.setOnAction((ActionEvent event) -> {
            myDefinition.clear();
            myText.clear();
            myDefinition.setEditable(true);
            myText.setEditable(true);
            btnOK.setVisible(true);
            btnCancel.setVisible(true);
            btnOK.setOnAction((ActionEvent e) -> {
                String wrd = myText.getText();
                String df = myDefinition.getText();
                if (wrd.length() != 0 && df.length() != 0) {
                    int k = 0;
                    for (MyWords tmp : allWords) {
                        if (wrd.toLowerCase().equals(tmp.getWord().toLowerCase())) {
                            k++;
                            break;
                        }
                    }
                    if (k == 0) {
                        List<MyWords> allWrd = new ArrayList<>();
                        MyWords nWords = new MyWords();
                        nWords.setWordAndDefn(wrd, df);
                        allWords.add(nWords);
                        allWrd.add(nWords);
                        lv.setItems(FXCollections.observableArrayList(allWords));
                        save.setOnAction((ActionEvent eve) -> {
                            try {
                                out = new ObjectOutputStream(new FileOutputStream(wordsFile, true));
                                out.writeObject(allWords);
                            } catch (IOException ex) {
                                Logger.getLogger(Vocabulary2.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        });

                    } else {
                        myText.clear();
                        myDefinition.clear();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("This is already in the list!");
                        alert.showAndWait();
                    }
                    myDefinition.clear();
                    myText.clear();
                    btnOK.setVisible(false);
                    btnCancel.setVisible(false);
                } else {
                    myText.clear();
                    myDefinition.clear();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Word or/and definition is(are) missing!!!!.");

                    alert.showAndWait();

                }
            });
        });
        editButton.setOnAction((ActionEvent event) -> {
            myDefinition.setEditable(true);
            myText.setEditable(true);
            btnOK.setVisible(true);
            btnCancel.setVisible(true);
            Integer i = lv.getSelectionModel().getSelectedIndex();
            btnOK.setOnAction((ActionEvent e) -> {
                String wrd = myText.getText();
                String df = myDefinition.getText();
                if (wrd.length() != 0 && df.length() != 0) {
                    MyWords nWords = new MyWords();
                    nWords.setWordAndDefn(wrd, df);
                    allWords.add(i, nWords);
                    allWords.remove(i + 1);
                    ObservableList<MyWords> collList = FXCollections.observableArrayList(allWords);
                    collList.set(i, nWords);
                    lv.setItems(collList);
                    myDefinition.clear();
                    myText.clear();
                    btnOK.setVisible(false);
                    btnCancel.setVisible(false);
                } else {
                    myText.clear();
                    myDefinition.clear();
                    //            tfWord.setText("You must have a definition as well as a word.");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Word or/and definition is(are) missing!!!!.");

                    alert.showAndWait();

                }

            });
            btnCancel.setOnAction((ActionEvent e) -> {
                myDefinition.clear();
                myText.clear();
            });

        });
        deleteButton.setOnAction((ActionEvent event) -> {
            myDefinition.setEditable(true);
            myText.setEditable(true);
            btnOK.setVisible(true);
            btnCancel.setVisible(true);
            Integer i = lv.getSelectionModel().getSelectedIndex();
            btnOK.setOnAction((ActionEvent e) -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Deleting word " + myText.getText());
                alert.setContentText("Are you sure?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    allWords.remove(allWords.get(i));
                    //myDef.remove(myDef.get(i));
                    ObservableList<MyWords> collList = FXCollections.observableArrayList(allWords);
                    lv.setItems(collList);
                    myDefinition.clear();
                    myText.clear();
                    btnOK.setVisible(false);
                    btnCancel.setVisible(false);
                }
                myDefinition.setEditable(false);
                myText.setEditable(false);
                btnOK.setVisible(false);
                btnCancel.setVisible(false);

            });

        });

        exit.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
        //create scene
        Scene scene = new Scene(myBorder, 700, 250);
        primaryStage.setTitle("My dictionary"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
