import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class studentInterfaceController {

    private String studentID;

    @FXML Button transcriptButton;
    @FXML Button degreeButton;
    @FXML Button addCourseButton;
    @FXML Button removeCourseButton;
    @FXML Button exitButton;
    @FXML Label nameLabel;
    @FXML Label idLabel;
    @FXML Label deptLabel;
    @FXML Label credLabel;

    public void initData(String studentID){
        this.studentID = studentID;

    }
}
