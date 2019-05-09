import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Observable;

public class studentInterfaceController {

    private String studentID;
    private String studentName;
    private String studentDept;
    private String studentCred;
    private Connection connection;
    private ObservableList<Course> currentCourses = FXCollections.observableArrayList();


    @FXML Button transcriptButton;
    @FXML Button degreeButton;
    @FXML Button addCourseButton;
    @FXML Button removeCourseButton;
    @FXML Button exitButton;
    @FXML Label nameLabel;
    @FXML Label idLabel;
    @FXML Label deptLabel;
    @FXML Label credLabel;
    @FXML TableView currentCourseTable;
    @FXML TableColumn<Course, String> courseIDColumn = new TableColumn<>("Course ID");
    @FXML TableColumn<Course, String> titleColumn = new TableColumn<>("Title");
    @FXML TableColumn<Course, String> semesterColumn = new TableColumn<>("Semester");
    @FXML TableColumn<Course, String> yearColumn = new TableColumn<>("Year");


    public void initData(String studentID, Connection connection){
        this.studentID = studentID;
        this.connection = connection;

        queryStudentInfo(connection);

        courseIDColumn.setCellValueFactory(new PropertyValueFactory<>("course_id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        currentCourseTable.setItems(currentCourses);
    }

    public void queryStudentInfo(Connection connection){
        try {


            PreparedStatement statement = connection.prepareStatement("SELECT * FROM student WHERE ID = " + studentID);
            PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM Student AS A, takes AS b, course AS c, section as d WHERE a.ID = b.id AND A.id = " + this.studentID + " AND b.course_id = c.course_id AND b.sec_id = d.sec_id AND c.course_id = d.course_id AND b.semester = d.semester");

            ResultSet result = statement.executeQuery();
            ResultSet result2 = statement2.executeQuery();
            ResultSetMetaData rsmd = result2.getMetaData();

            System.out.println(rsmd.getColumnName(6) + " = column name");

            while(result2.next()){
                Course course = new Course(result2.getString("course_id"),result2.getString("sec_id"),result2.getString("semester"),result2.getString("year"),result2.getString("title"),result2.getString("dept_name"),result2.getString("credits"),result2.getString("time_slot_id"));
                currentCourses.add(course);
            }

            while(result.next()){
                System.out.println(result.getString("name"));
                this.studentName = result.getString("name");
                this.studentCred = result.getString("tot_cred");
                this.studentDept = result.getString("dept_name");
                updateLabel();
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void updateLabel(){
        nameLabel.setText("Welcome " + studentName);
        idLabel.setText("Student ID " + studentID);
        credLabel.setText("Total Credits: " + studentCred);
        deptLabel.setText("Department: " + studentDept);
    }

    public ObservableList<Course> getCurrentClasses(){

        return null;

    }
}
