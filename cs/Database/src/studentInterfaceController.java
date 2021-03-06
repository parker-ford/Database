import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;
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
    @FXML Button exitProgramButton;
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
    @FXML RadioButton radio2010;
    @FXML RadioButton radio2009;
    @FXML RadioButton radioFall;
    @FXML RadioButton radioSpring;
    @FXML RadioButton radioSummer;
    @FXML TableView addCourseTable;
    @FXML TableColumn<Course, String> addCourseID = new TableColumn<>("Course ID");
    @FXML TableColumn<Course, String> addCourseTitle = new TableColumn<>("Title");
    @FXML Button addSelectedClassButton;
    @FXML Button addSearchCourseButton;
    @FXML ToggleGroup addYear;
    @FXML ToggleGroup addSemester;

    @FXML HBox removeHBox;
    @FXML TableView removeCourseTable;
    @FXML TableColumn<Course, String> removeCourseID = new TableColumn<>("Course ID");
    @FXML TableColumn<Course, String> removeCourseTitle = new TableColumn<>("Course ID");
    @FXML TableColumn<Course, String> removeCourseSemester = new TableColumn<>("Course ID");
    @FXML TableColumn<Course, String> removeCourseYear = new TableColumn<>("Course ID");
    @FXML Button removeSelectedButton;



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

        addCourseID.setCellValueFactory(new PropertyValueFactory<>("course_id"));
        addCourseTitle.setCellValueFactory(new PropertyValueFactory<>("title"));

        removeCourseID.setCellValueFactory(new PropertyValueFactory<>("course_id"));
        removeCourseTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        removeCourseSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        removeCourseYear.setCellValueFactory(new PropertyValueFactory<>("year"));
    }

    public void queryStudentInfo(Connection connection){
        try {


            PreparedStatement statement = connection.prepareStatement("SELECT * FROM student WHERE ID = " + studentID);
            PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM Student AS A, takes AS b, course AS c, section as d WHERE a.ID = b.id AND A.id = " + this.studentID + " AND b.course_id = c.course_id AND b.sec_id = d.sec_id AND c.course_id = d.course_id AND b.semester = d.semester");

            ResultSet result = statement.executeQuery();
            ResultSet result2 = statement2.executeQuery();
            ResultSetMetaData rsmd = result2.getMetaData();

            System.out.println(rsmd.getColumnName(6) + " = column name");

            currentCourses.clear();
            while(result2.next()){
                Course course = new Course(result2.getString("course_id"),result2.getString("sec_id"),result2.getString("semester"),result2.getString("year"),result2.getString("title"),result2.getString("dept_name"),result2.getString("credits"),result2.getString("time_slot_id"),result2.getString("grade"));
                currentCourses.add(course);
            }
            System.out.println(Arrays.toString(currentCourses.toArray()));

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

    public void queryAddCourse(Connection connection, String year, String semester){
        coursesAdd.clear();
        try {
            PreparedStatement statement = connection.prepareStatement("select *  from section as A left outer join (select * from takes where id = '" + studentID + "') as B on A.course_id = B.course_id, course as C where grade IS NULL AND A.course_id = C.course_id AND A.year = '" + year + "' AND A.semester = '" + semester + "';");
            ResultSet result = statement.executeQuery();

            while(result.next()){
                Course course = new Course(result.getString("course_id"), result.getString("title"), result.getString("building"), result.getString("room_number"), result.getString("sec_id"), result.getString("year"), result.getString("semester"));
                coursesAdd.add(course);
            }
            addCourseTable.setItems(coursesAdd);
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
        missingCourseTable.refresh();
        missingCourseTable.setVisible(true);
    }

    public void resetView(){
        currentCourseTable.setVisible(false);
        missingCourseTable.setVisible(false);
        addHBox.setVisible(false);
        removeHBox.setVisible(false);
    }

    public void handleAdd(){
        resetView();
        addHBox.setVisible(true);


        addSearchCourseButton.setOnAction(e -> {
            String year;
            String semester;
            RadioButton getYear = (RadioButton) addYear.getSelectedToggle();
            year = getYear.getText();
            RadioButton getSemester = (RadioButton) addSemester.getSelectedToggle();
            semester = getSemester.getText();

            queryAddCourse(connection, year, semester);
        });




    }

    public void handleAddSelected(){

        try {
            Course addedCourse = (Course) addCourseTable.getSelectionModel().getSelectedItem();
            PreparedStatement statement = connection.prepareStatement("insert into takes values ('" + studentID + "', '" + addedCourse.getCourse_id() + "', '" + addedCourse.getSec_id() + "', '" + addedCourse.getSemester() + "', '" + addedCourse.getYear() + "', 'C')");

            statement.executeUpdate();

            System.out.println("insert made");

            queryStudentInfo(connection);
            handleTranscript();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleRemove(){
        resetView();
        removeHBox.setVisible(true);
        removeCourseTable.setItems(currentCourses);
        removeCourseTable.refresh();

        removeSelectedButton.setOnAction(e -> {
            try {
                Course courseRemoved = (Course) removeCourseTable.getSelectionModel().getSelectedItem();

                PreparedStatement statement = connection.prepareStatement("delete from takes where id = '" + studentID + "' and course_id = '" + courseRemoved.getCourse_id() + "' and sec_id = '" + courseRemoved.getSec_id() + "' and semester = '" + courseRemoved.getSemester() + "' and year= '" + courseRemoved.getYear() + "'");
                statement.executeUpdate();
                System.out.println("course deleted");

                queryStudentInfo(connection);
                handleTranscript();
            }
            catch (Exception z){
                z.printStackTrace();
            }
        });
    }

    public void handleExitProgram(){
        Stage stage = (Stage) exitProgramButton.getScene().getWindow();
        stage.close();
    }


}
