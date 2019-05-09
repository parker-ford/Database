import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;

public class Main extends Application {

    private static String[] loginInfo;

    @Override
    public void start(Stage primaryStage) throws Exception{

        System.out.println(Arrays.toString(loginInfo));

        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));

        Parent loginParent = loader.load();
        Scene loginScene = new Scene(loginParent);

        loginController controller = loader.getController();
        controller.initData(loginInfo);



        //System.out.println("start");
        //Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        //primaryStage.setTitle("Database");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void main(String[] args){
        System.out.println("test");
        loginInfo = args;


        //JDBC jdbc = new JDBC(args[0], args[1]);
        //jdbc.run();
        launch(args);

    }


}
