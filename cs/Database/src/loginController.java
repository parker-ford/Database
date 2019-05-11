import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;

public class loginController {

    @FXML
    public Button submitButton;
    @FXML
    public TextField studentIDText;
    @FXML
    public Label errorLabel;

    private String userID;
    private String password;
    private String studentID;
    private Connection connection;

    private final String preConnectionString = "jdbc:mysql://mysql.cs.wwu.edu:3306/";
    private final String postConnectionString = "?useSSL=false";

    public loginController() {
        System.out.println("login");


    }

    public void initData(String[] loginInfo) {
        System.out.println("data init");
        this.userID = loginInfo[0];
        this.password = loginInfo[1];

        try {
            this.connection = getConnection();
            System.out.println("successful connection");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getConnectionString(),
                userID,
                password);
    }

    private String getConnectionString() {
        return preConnectionString + userID + postConnectionString;
    }


    public void handleSubmit(ActionEvent event) throws IOException, ParserConfigurationException {
        this.studentID = studentIDText.getText();
        boolean check = checkUser();

        if(check == false){
            updateLabel();
            studentIDText.clear();
        }

        else {
            System.out.println(studentID);
            //String studentName = queryName(connection);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("studentInterface.fxml"));

            Parent studentInterfaceParent = loader.load();
            Scene studentInterfaceScene = new Scene(studentInterfaceParent);

            studentInterfaceController controller = loader.getController();
            controller.initData(studentID, connection);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(studentInterfaceScene);
            window.show();
        }

    }

    public boolean checkUser(){
        try {
            PreparedStatement statement = connection.prepareStatement("select id from student where id = '" + studentID + "'");
            ResultSet result = statement.executeQuery();

            System.out.println(result.getFetchSize() + " fetch size");
            System.out.println("test");

            if(result.next() == false){
                System.out.println("FALSE");
                return false;
            }
            else{
                System.out.println("TRUE");
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void updateLabel(){

    }


}
