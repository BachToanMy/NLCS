package com.example.denno;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class cardProductcontroller implements Initializable {
    @FXML private Button card_addBtn;

    @FXML private AnchorPane card_form;

    @FXML private ImageView card_imageView;

    @FXML private Label card_productname;

    @FXML private Label card_productprice;

    @FXML private Spinner<Integer> card_spinner;
    private SpinnerValueFactory<Integer> spinner;
    private String prodID;
    private String prodtype;
    private String prodimage;
    private String proddate;
    private  productdata card_proData;
    private Image image;
    private PreparedStatement preparedStatement;
    private ResultSet result;
    private database db = new database();
    private Connection connect = db.getConnection();
    private  double totalP;

    public void setCard_proData(productdata proData){
        this.card_proData = proData;
        prodimage = proData.getImage();
        proddate = String.valueOf(proData.getDate());
        prodtype = proData.getType();
        prodID = proData.getProductID();
        card_productname.setText(card_proData.getProductName());
        card_productprice.setText("$"+card_proData.getPrice().toString());
        data.path = card_proData.getImage();
        String path = "File:"+ card_proData.getImage();
        image = new Image(path,280,219,false,true);
        card_imageView.setImage(image);
        pr = proData.getPrice();

    }
    private  int qty;
    public void setQuantity(){
        spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0);
        card_spinner.setValueFactory(spinner);
    }

    private Double pr;
    public void addBtn(){
        dashboardcontroller dbcontroller = new dashboardcontroller();
        dbcontroller.customerID();
        qty = card_spinner.getValue();
        System.out.println("QTY: "+qty);
        String check="";
        String checkAvailable="SELECT status FROM product WHERE product_id = '" + prodID + "'";
        try{
            preparedStatement = connect.prepareStatement(checkAvailable);
            result = preparedStatement.executeQuery();

            if(result.next()){
                check = result.getString("status");
            }
            if(!check.equals("Available") || qty==0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Something Wrong :<");
                alert.showAndWait();
            }else{
                int checkstk = 0;
                String checkStock = "SELECT stock FROM product WHERE product_id = '" + prodID + "'";
                preparedStatement = connect.prepareStatement(checkStock);
                result = preparedStatement.executeQuery();
                if(result.next()){
                    checkstk = result.getInt("stock");
                }
                if(checkstk < qty){
                    System.out.println("Stock:"+checkstk+" Qty: "+qty);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Quantity !");
                    alert.showAndWait();
                }else {
                    System.out.println("CUSTOMER:"+prodimage);
                    String insertData = "INSERT INTO customer (customer_id,product_id,product_name,type,quantity,price,date,image,em_username)"
                            + "VALUES(?,?,?,?,?,?,?,?,?)";
                    preparedStatement = connect.prepareStatement(insertData);
                    preparedStatement.setString(1, String.valueOf(data.cID));
                    preparedStatement.setString(2,prodID);
                    preparedStatement.setString(3, card_productname.getText());
                    preparedStatement.setString(4, prodtype);
                    preparedStatement.setString(5, String.valueOf(qty));
                    totalP = (qty * pr);
                    preparedStatement.setString(6, String.valueOf(totalP));

                    Date date = new Date();
                    java.sql.Date sqldate = new java.sql.Date(date.getTime());
                    preparedStatement.setString(7, String.valueOf(sqldate));
                    preparedStatement.setString(8, prodimage);
                    preparedStatement.setString(9, data.username);
                    preparedStatement.executeUpdate();
                    int upStock = checkstk - qty;
                    if(upStock==0){
                        check="Unavailable";
                    }
                    prodimage = prodimage.replace("\\","\\\\");
                    System.out.println("PRODUCT:"+prodimage);
                    String updateStock = "UPDATE product SET product_name = '"
                            + card_productname.getText() + "',type = '"
                            + prodtype+ "',stock = '"
                            + upStock + "',price = '"
                            + pr + "',status = '"
                            + check + "',date = '"
                            + proddate + "',image = '" + prodimage + "' WHERE product_id = '" +prodID+"'";
                    preparedStatement = connect.prepareStatement(updateStock);
                    preparedStatement.executeUpdate();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added !");
                    alert.showAndWait();

                    dashboardcontroller dashboardcontroller = new dashboardcontroller();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setQuantity();
    }
}
