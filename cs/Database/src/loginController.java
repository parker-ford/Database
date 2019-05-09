import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class loginController {

    @FXML
    public Button submitButton;
    @FXML
    public TextField studentIDText;
    @FXML
    public Label nameDisplay;

    private String userID;
    private String password;

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
            Connection connection = getConnection();
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
        String studentID = studentIDText.getText();
        System.out.println(studentID);

    }

    public void updateLabel(){

    }
}
