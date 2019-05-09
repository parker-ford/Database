
public class Course {
    private String course_id;
    private String sec_id;
    private String semester;
    private String year;
    private String title;
    private String dept_name;
    private int credits;
    private String time_slot_id;



    public Course(String course_id, String sec_id, String semester, String year, String title, String dept_name, String credits, String time_slot_id){
        this.course_id = course_id;
        this.sec_id = sec_id;
        this.semester = semester;
        this.year = year;
        this.title = title;
        this.dept_name = dept_name;
        this.credits = Integer.parseInt(credits);
        this.time_slot_id = time_slot_id;

        System.out.println("course created");
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
}
