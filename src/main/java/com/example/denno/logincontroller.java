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

    private database db = new database();
    private Connection con = db.getConnection();
    private Alert alert;
    ResultSet rs;

    public void Login(ActionEvent event){

        if(usernamefield.getText().isEmpty() || passwordfield.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Username or Password");
            alert.show();
        } else{
            try {
                PreparedStatement pst = con.prepareStatement("select * from account where Username=? and Password=?");
                String uname = usernamefield.getText();
                String pass = passwordfield.getText();
                pst.setString(1,uname);
                pst.setString(2,pass);

                rs = pst.executeQuery();

                if(rs.next()){
                    //TO GET THE USERNAME THAT USER USED
                    data.username = usernamefield.getText();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successfully");
                    alert.showAndWait();
                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setTitle("Welcome to myComestic");
                    stage.setScene(scene);
                    stage.show();
                    signinbutton.getScene().getWindow().hide();
                }
                else {
                    alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username or Password");
                    alert.showAndWait();
                    usernamefield.setText("");
                    passwordfield.setText("");
                }

            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
