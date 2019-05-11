
public class Course {
    private String course_id;
    private String sec_id;
    private String semester;
    private String year;
    private String title;
    private String dept_name;
    private int credits;
    private String time_slot_id;
    private String grade;


    private String building;
    private String room_number;


    public Course(String course_id, String sec_id, String semester, String year, String title, String dept_name, String credits, String time_slot_id, String grade) {
        this.course_id = course_id;
        this.sec_id = sec_id;
        this.semester = semester;
        this.year = year;
        this.title = title;
        this.dept_name = dept_name;
        this.credits = Integer.parseInt(credits);
        this.time_slot_id = time_slot_id;
        this.grade = grade;

        System.out.println("course created");
    }

    public Course(String course_id, String title) {
        this.course_id = course_id;
        this.title = title;
    }

    public Course(String course_id, String title, String building, String room_number, String sec_id, String year, String semester) {
        this.course_id = course_id;
        this.title = title;
        this.building = building;
        this.room_number = room_number;
        this.sec_id = sec_id;
        this.year = year;
        this.semester = semester;
    }
    public String getCourse_id() {
        return course_id;
    }

    public String getSec_id() {
        return sec_id;
    }

    public String getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getDept_name() {
        return dept_name;
    }

    public int getCredits() {
        return credits;
    }

    public String getTime_slot_id() {
        return time_slot_id;
    }

    public String getGrade() {
        return grade;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoom_number() {
        return room_number;
    }

}
