package com.example.denno;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class logincontroller{


    @FXML private ImageView logosignin;
    @FXML private AnchorPane panelogin;
    @FXML private Label labelsignin;
    @FXML private Button signinbutton;
    @FXML private TextField usernamefield;
    @FXML private TextField passwordfield;
    @FXML private Label usernamelabel;
    @FXML private Label passwordlabel;
    @FXML private Button signupbutton;
    private Stage stage;
    private Scene scene;
    private Parent root;
    database db = new database();
    Connection con = db.getConnection();

    ResultSet rs;
    public void Login(ActionEvent event){
        try {
            PreparedStatement pst = con.prepareStatement("select * from account where Username=? and Password=?");
            String uname = usernamefield.getText();
            String pass = passwordfield.getText();
            pst.setString(1,uname);
            pst.setString(2,pass);

            rs = pst.executeQuery();

            if(rs.next()){
                System.out.println("Login successfully");
                root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else {
                usernamefield.setText("");
                passwordfield.setText("");
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

//    public void actionsignin(){
//        Window owner = usernamefield.getScene().getWindow();
//        System.out.println(usernamefield.getText());
//        System.out.println(passwordfield.getText());
//
//        if(usernamefield.getText().isEmpty()){
//            showAlert(Alert.AlertType.ERROR,owner,"Please enter a valid username","Error");
//
//        }
//
//    }
//    public static void infobox(String informessage,String header,String title){
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setContentText(informessage);
//        alert.setTitle(title);
//        alert.setHeaderText(header);
//        alert.showAndWait();
//
//    }
//
//    public static void showAlert(Alert.AlertType alertType, Window owner,String message,String title){
//        Alert alert = new Alert(alertType);
//        alert.setContentText(message);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.initOwner(owner);
//        alert.show();
//    }
}
