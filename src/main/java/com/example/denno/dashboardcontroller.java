package com.example.denno;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class dashboardcontroller implements Initializable {
    @FXML private Button customers_btn;
    @FXML private Label username;
    @FXML private Button dashboard_btn;
    @FXML private Button inventory_addBtn;
    @FXML private Button inventory_btn;
    @FXML private Button inventory_clearBtn;
    @FXML private AnchorPane dashboard_form;
    @FXML private TableColumn<productdata, Date> inventory_col_date;
    @FXML private TableColumn<productdata, Double> inventory_col_price;
    @FXML private TableColumn<productdata, String> inventory_col_productID;
    @FXML private TableColumn<productdata, String> inventory_col_productName;
    @FXML private TableColumn<productdata, String> inventory_col_status;
    @FXML private TableColumn<productdata, Integer> inventory_col_stock;
    @FXML private TableColumn<productdata, String> inventory_col_type;
    @FXML private Button inventory_deleteBtn;
    @FXML private AnchorPane inventory_form;
    @FXML private TextField inventory_productID;
    @FXML private TextField inventory_productName;
    @FXML private TextField inventory_stock;
    @FXML private ComboBox<?> inventory_type;
    @FXML private ComboBox<?> inventory_status;
    @FXML private TextField inventory_price;
    @FXML private ImageView inventory_imageView;
    @FXML private Button inventory_importBtn;
    @FXML private TableView<productdata> inventory_tableView;
    @FXML private Button inventory_updateBtn;
    @FXML private Button logout_btn;
    @FXML private AnchorPane main_form;
    @FXML private Button menu_btn;
    @FXML private TextField menu_amount;
    @FXML private Label menu_change;
    @FXML private TableColumn<productdata, Integer> menu_col_price;
    @FXML private TableColumn<productdata, String> menu_col_productName;
    @FXML private TableColumn<productdata, Integer> menu_col_quantity;
    @FXML private AnchorPane menu_form;
    @FXML private GridPane menu_gridPane;
    @FXML private Button menu_payBtn;
    @FXML private Button menu_receiptBtn;
    @FXML private Button menu_removeBtn;
    @FXML private ScrollPane menu_scrollPane;
    @FXML private TableView<productdata> menu_tableView;
    @FXML private Label menu_total;
    private Alert alert;
    private String[] typeList = {"Meals","Drinks"};
    private String[] statusList = {"Available","Unavailable"};

    private PreparedStatement prepared;
    private Statement statement;
    private ResultSet result;
    private database db = new database();
    private Connection connect = db.getConnection();
    private  ObservableList<productdata> cardListData = FXCollections.observableArrayList();
    private String img="";

    public void inventoryAddBtn(){
        if (inventory_productID.getText().isEmpty()||
                inventory_productName.getText().isEmpty()||
                inventory_type.getSelectionModel().getSelectedItem() == null||
                inventory_stock.getText().isEmpty()||
                inventory_price.getText().isEmpty()||
                inventory_status.getSelectionModel().getSelectedItem() == null ||
                data.path == null || data.id == 0){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }else {
            String checkProdID = "SELECT product_id FROM product WHERE product_id = '"
                    + inventory_productID.getText() + "'";

            try{
                 statement = connect.createStatement();
                 result = statement.executeQuery(checkProdID);
                 if(result.next()){
                     alert = new Alert(Alert.AlertType.ERROR);
                     alert.setTitle("Error Message");
                     alert.setHeaderText(null);
                     alert.setContentText(inventory_productID.getText() + "is already taken");
                     alert.showAndWait();
                 }else{
                     String insertData = "INSERT INTO product "
                             + "(product_id, product_name, type, stock,price,status,date,image)"
                             + "VALUE(?,?,?,?,?,?,?,?)";
                     prepared = connect.prepareStatement(insertData);
                     prepared.setString(1,inventory_productID.getText());
                     prepared.setString(2,inventory_productName.getText());
                     prepared.setString(3,inventory_type.getSelectionModel().getSelectedItem().toString());
                     prepared.setString(4,inventory_stock.getText());
                     prepared.setString(5,inventory_price.getText());
                     prepared.setString(6,inventory_status.getSelectionModel().getSelectedItem().toString());
                     Date date = new Date();
                     java.sql.Date sqlDate = new  java.sql.Date(date.getTime());;
                     prepared.setString(7,String.valueOf(sqlDate));
                     String path = data.path;
                     path = path.replace("\\\\","\\");
                     prepared.setString(8,path);

                     prepared.executeUpdate();

                     alert = new Alert(Alert.AlertType.INFORMATION);
                     alert.setTitle("Error Message");
                     alert.setHeaderText(null);
                     alert.setContentText("Successfully Add Product");
                     alert.showAndWait();

                     InventoryshowData();
                     InventoryClearBtn();

                 }
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void InventoryUpdateBtn(){
        if (inventory_productID.getText().isEmpty()||
                inventory_productName.getText().isEmpty()||
                inventory_type.getSelectionModel().getSelectedItem() == null||
                inventory_stock.getText().isEmpty()||
                inventory_price.getText().isEmpty()||
                inventory_status.getSelectionModel().getSelectedItem() == null ||
                data.path == null || data.id == 0){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }else {
            String path = data.path;
            path = path.replace("\\","\\\\");
            String updatedata = "UPDATE product SET "
                    + "product_id = '" + inventory_productID.getText()+"',product_name = '"
                    + inventory_productName.getText() + "',type = '"
                    + inventory_type.getSelectionModel().getSelectedItem()+ "',stock = '"
                    + inventory_stock.getText() + "',price = '"
                    + inventory_price.getText() + "',status = '"
                    + inventory_status.getSelectionModel().getSelectedItem() + "',date = '"
                    + data.date + "',image = '" + path + "' WHERE id = " +data.id;
            try {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("R U sure U want to update product id: "+inventory_productID.getText());
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    prepared = connect.prepareStatement(updatedata);
                    prepared.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated");
                    alert.showAndWait();

                    InventoryshowData();
                    InventoryClearBtn();

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled");
                    alert.showAndWait();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //Let make a behavior for import
    public void InventoryClearBtn(){
        inventory_productID.setText("");
        inventory_productName.setText("");
        inventory_type.getSelectionModel().clearSelection();
        inventory_stock.setText("");
        inventory_price.setText("");
        inventory_status.getSelectionModel().clearSelection();
        data.path="";
        data.id = 0;
        inventory_imageView.setImage(null);
    }

    public void InventoryDeleteBtn(){
        if (data.id == 0){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Product not exist !");
            alert.showAndWait();
        }else {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you wan to Delete product id : "+inventory_productID.getText());
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){
                String deletedata = "DELETE FROM product WHERE id = " + data.id;
                try{
                    prepared = connect.prepareStatement(deletedata);
                    prepared.executeUpdate();

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted");
                    alert.showAndWait();

                    InventoryshowData();
                    InventoryClearBtn();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }

        }
    }
    public void InventoryImportBtn(){
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("open image file","*png","*jpg"));

        File file = openFile.showOpenDialog(main_form.getScene().getWindow());

        if(file != null){
            data.path = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString(), 120, 127, false, true);
            inventory_imageView.setImage(image);
        }
    }
    //MERGE ALL DATAS
    public ObservableList<productdata> inventoryDataList(){
        ObservableList<productdata> Listdata=FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        try{
            prepared = connect.prepareStatement(sql);
            result =  prepared.executeQuery();
            productdata prodata;
            while(result.next()){
                prodata = new productdata(result.getInt("id"),
                        result.getString("product_id"),
                        result.getString("product_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date")
                        );
                Listdata.add(prodata);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Listdata;
    }

    private ObservableList<productdata> inventoryListData;
    //SHOW  DATA ON OUR TABLE
     public void InventoryshowData(){
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableView.setItems(inventoryListData);
    }

    public void InventorySelectData(){
         productdata prodata = inventory_tableView.getSelectionModel().getSelectedItem();
         int num = inventory_tableView.getSelectionModel().getSelectedIndex();

         if((num-1)<-1) return;

         inventory_productID.setText(prodata.getProductID());
         inventory_productName.setText(prodata.getProductName());
         inventory_stock.setText(String.valueOf(prodata.getStock()));
         inventory_price.setText(String.valueOf(prodata.getPrice()));

         data.path = prodata.getImage();
         String path = "File:" + prodata.getImage();
         data.date = String.valueOf(prodata.getDate());
         data.id = prodata.getId();
         Image image = new Image(path,120,127,false,true);
         inventory_imageView.setImage(image);
    }
    public void InventoryStatusList(){
        List<String> statusL = new ArrayList<>();
        for(String data: statusList){
            statusL.add(data);
        }
        ObservableList ListData = FXCollections.observableArrayList(statusL);
        inventory_status.setItems(ListData);
    }
    public void InventoryTypeList(){
        List<String> typeL = new ArrayList<>();
        for(String data: typeList){
            typeL.add(data);
        }
        ObservableList ListData = FXCollections.observableArrayList(typeL);
        inventory_type.setItems(ListData);
    }

    public ObservableList<productdata> menuGetData(){
         String sql = "SELECT * FROM product";
         ObservableList<productdata> listData = FXCollections.observableArrayList();

         try{
             prepared = connect.prepareStatement(sql);
             result = prepared.executeQuery();

             productdata prod;
             while (result.next()){
                 prod= new productdata(result.getInt("id"),
                         result.getString("product_id"),
                         result.getString("product_name"),
                         result.getString("type"),
                         result.getInt("stock"),
                         result.getDouble("price"),
                         result.getString("image"),
                         result.getDate("date"));
                 listData.add(prod);
             }
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
        return listData;
    }

    public void menuDisplayCard(){
         cardListData.clear();
         cardListData.addAll(menuGetData());
         int row = 0;
         int col = 0;
         menu_gridPane.getChildren().clear();
         menu_gridPane.getRowConstraints().clear();
         menu_gridPane.getColumnConstraints().clear();
        for(int m = 0; m  < cardListData.size(); m++){
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = loader.load();
                cardProductcontroller cardC = loader.getController();
                cardC.setCard_proData(cardListData.get(m));
                if(col == 3){
                    col = 0;
                    row +=1;
                }

                menu_gridPane.add(pane,col++,row);

                GridPane.setMargin(pane,new Insets(10));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ObservableList<productdata> menuDisplayOrder(){
         customerID();
         ObservableList<productdata> listData = FXCollections.observableArrayList();
         String sql = "SELECT * FROM customer WHERE customer_id = " + cID;
         try{
             prepared = connect.prepareStatement(sql);
             result = prepared.executeQuery();

             productdata prod;
             while(result.next()){
                 prod = new productdata(result.getInt("id"),
                         result.getString("product_id"),
                         result.getString("product_name"),
                         result.getString("type"),
                         result.getInt("quantity"),
                         result.getDouble("price"),
                         result.getString("image"),
                         result.getDate("date"));
                 listData.add(prod);
             }
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
         return listData;
    }

    private Double totalP;
    public void menuDisplayTotal(){
         customerID();
         String total = "SELECT SUM(price) FROM customer WHERE customer_id = "+cID;
         try{
             prepared=connect.prepareStatement(total);
             result= prepared.executeQuery();

             if(result.next()){
                 totalP = result.getDouble("SUM(price)");
             }
             menu_total.setText("$"+totalP.toString());
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
    }

    private ObservableList<productdata> menuListData;
     public void MenushowData(){
         menuListData = menuDisplayOrder();
         menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
         menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
         menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

         menu_tableView.setItems(menuListData);

     }
     private Double amount;
     private Double change=0.0;
     public void menuAMount(){
         if(menu_amount.getText().isEmpty()||totalP==0){
             alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Error Message");
             alert.setHeaderText(null);
             alert.setContentText("Invalid:3");
             alert.showAndWait();
         }else{
             amount = Double.parseDouble(menu_amount.getText());
             if(amount < totalP){
                 menu_amount.setText("");
             }else {
                 change = (amount-totalP);
                 menu_change.setText("$"+change);
             }
         }
     }

     public void menuPayBtn(){
         if(totalP==0){
             alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Error Message");
             alert.setHeaderText(null);
             alert.setContentText("Please choose your order first!");
             alert.showAndWait();
         }else{
             String insertPay = "INSERT INTO receipt (customer_id,total,date,em_username)"
                     + "VALUES(?,?,?,?)";
             try{
                 if(amount == 0.0){
                     alert = new Alert(Alert.AlertType.ERROR);
                     alert.setTitle("Error Message");
                     alert.setHeaderText(null);
                     alert.setContentText("Invalid:3");
                     alert.showAndWait();
                 }else{
                     alert = new Alert(Alert.AlertType.CONFIRMATION);
                     alert.setTitle("Error Message");
                     alert.setHeaderText(null);
                     alert.setContentText("Are you sure!");
                     Optional<ButtonType> optional = alert.showAndWait();

                     if(optional.get().equals(ButtonType.OK)){
                         customerID();
                         menuDisplayTotal();
                         prepared = connect.prepareStatement(insertPay);
                         prepared.setString(1,String.valueOf(cID));
                         prepared.setString(2,String.valueOf(totalP));
                         Date date = new Date();
                         java.sql.Date sqldate = new java.sql.Date(date.getTime());
                         prepared.setString(3,String.valueOf(sqldate));
                         prepared.setString(4,data.username);
                         prepared.executeUpdate();
                         alert = new Alert(Alert.AlertType.INFORMATION);
                         alert.setTitle("Information Message");
                         alert.setHeaderText(null);
                         alert.setContentText("Successful!");
                         alert.showAndWait();

                         menuDisplayOrder();
                         menuRestart();
                     }else{
                         alert = new Alert(Alert.AlertType.INFORMATION);
                         alert.setTitle("Information Message");
                         alert.setHeaderText(null);
                         alert.setContentText("Cancelled!");
                         alert.showAndWait();
                     }
                 }
             } catch (SQLException e) {
                 throw new RuntimeException(e);
             }
         }
     }

     public void menuRestart(){
         totalP=0.0;
         change=0.0;
         amount=0.0;
         menu_total.setText("");
         menu_amount.setText("");
         menu_change.setText("");

     }
    private int cID;
    public void customerID(){
        String sql = "SELECT MAX(customer_id) FROM customer";
        try{
            prepared = connect.prepareStatement(sql);
            result = prepared.executeQuery();

            if(result.next()){
                cID = result.getInt("MAX(customer_id)");
            }

            String checkCID = "SELECT MAX(customer_id) FROM receipt";
            prepared = connect.prepareStatement(checkCID);
            result = prepared.executeQuery();
            int checkID=0;
            if(result.next()){
                checkID = result.getInt("MAX(customer_id)");
            }
            if(cID == 0){
                cID += 1;
            }else if(cID == checkID){
                cID += 1;
            }

            data.cID = cID;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void switchForm(ActionEvent event){
         if(event.getSource() == dashboard_btn){
             dashboard_form.setVisible(true);
             inventory_form.setVisible(false);
             menu_form.setVisible(false);
         } else if (event.getSource() == inventory_btn) {
             dashboard_form.setVisible(false);
             inventory_form.setVisible(true);
             menu_form.setVisible(false);
             InventoryTypeList();
             InventoryStatusList();
             InventoryshowData();
         } else if (event.getSource() == menu_btn) {
             dashboard_form.setVisible(false);
             inventory_form.setVisible(false);
             menu_form.setVisible(true);
             menuDisplayCard();
             MenushowData();
             menuDisplayTotal();
         }
    }
    public void logout(){
        try{
            alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you want to sign out");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){

                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("mYCOMESTIC");

                stage.setScene(scene);
                stage.show();
                logout_btn.getScene().getWindow().hide();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void displayName(){
        String user = data.username;
        user = user.substring(0,1).toUpperCase() + user.substring(1);
        username.setText(user);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayName();
        menuDisplayCard();
        MenushowData();
        menuDisplayTotal();
    }
}
