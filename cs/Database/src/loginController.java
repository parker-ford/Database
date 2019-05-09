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
    public Label nameDisplay;

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
        System.out.println(studentID);
        //String studentName = queryName(connection);

        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("studentInterface.fxml"));

        Parent studentInterfaceParent = loader.load();
        Scene studentInterfaceScene = new Scene(studentInterfaceParent);

        studentInterfaceController controller = loader.getController();
        controller.initData(studentID);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(studentInterfaceScene);
        window.show();

    }

    public void updateLabel(String name){
        nameDisplay.setText(name);
    }

    public String queryName(Connection connection){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM student WHERE ID = " + studentID);

            ResultSet result = statement.executeQuery();

            while(result.next()){
                System.out.println(result.getString("name"));
                String studentName = result.getString("name");
                updateLabel(studentName);
                return studentName;
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
