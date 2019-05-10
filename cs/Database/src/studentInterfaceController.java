import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.awt.event.ActionEvent;
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
    private ObservableList<Course> missingCourses = FXCollections.observableArrayList();
    private ObservableList<Course> coursesAdd = FXCollections.observableArrayList();


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
    @FXML TableColumn<Course, String> gradeColumn = new TableColumn<>("Grade");

    @FXML TableView missingCourseTable;
    @FXML TableColumn<Course, String> missingID = new TableColumn<>("Course ID");
    @FXML TableColumn<Course, String> missingTitle = new TableColumn<>("Title");

    @FXML HBox addHBox;
    @FXML RadioButton radioSecOne;
    @FXML RadioButton radioSecTwo;
    @FXML ChoiceBox addCourseChoice;


    public void initData(String studentID, Connection connection){
        this.studentID = studentID;
        this.connection = connection;

        queryStudentInfo(connection);
        queryMissingCourse(connection);

        courseIDColumn.setCellValueFactory(new PropertyValueFactory<>("course_id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        currentCourseTable.setItems(currentCourses);

        missingID.setCellValueFactory(new PropertyValueFactory<>("course_id"));
        missingTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
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
                Course course = new Course(result2.getString("course_id"),result2.getString("sec_id"),result2.getString("semester"),result2.getString("year"),result2.getString("title"),result2.getString("dept_name"),result2.getString("credits"),result2.getString("time_slot_id"),result2.getString("grade"));
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

    public void queryMissingCourse(Connection connection){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from course left outer join (select * from takes where id = '" + studentID + "') as A on course.course_id = a.course_id where course.dept_name = '" + studentDept + "' AND grade IS NULL");
            ResultSet result = statement.executeQuery();

            while(result.next()){
                System.out.println("MISSING COURSE FOUND");
                Course course = new Course(result.getString("course_id"),result.getString("title"));
                missingCourses.add(course);
            }

            missingCourseTable.setItems(missingCourses);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void queryAddCourse(Connection connection, String section){
        coursesAdd.clear();
        //addCourseChoice.getItems().clear();
        try {
            PreparedStatement statement = connection.prepareStatement("select *  from section as A left outer join (select * from takes where id = '" + studentID + "') as B on A.course_id = B.course_id, course as C where grade IS NULL AND A.sec_id = '" + section + "'AND A.course_id = C.course_id");
            ResultSet result = statement.executeQuery();

            while(result.next()){
                Course course = new Course(result.getString("course_id"), result.getString("title"), result.getString("building"), result.getString("room_number"));
                coursesAdd.add(course);
            }
            addCourseChoice.getItems().addAll(coursesAdd);
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

    public void handleTranscript(){
        resetView();
        currentCourseTable.setVisible(true);
    }

    public void handleDegree(){
        resetView();
        missingCourseTable.setVisible(true);
    }

    public void resetView(){
        currentCourseTable.setVisible(false);
        missingCourseTable.setVisible(false);
        addHBox.setVisible(false);
    }

    public void handleAdd(){
        resetView();
        addHBox.setVisible(true);
        radioSecOne.setOnAction(e -> {
            queryAddCourse(connection, "1");
        });
        radioSecTwo.setOnAction(e -> {
            queryAddCourse(connection, "2");
        });
    }


}
