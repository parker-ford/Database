
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

    public Course(String course_id, String title, String building, String room_number) {
        this.course_id = course_id;
        this.title = title;
        this.building = building;
        this.room_number = room_number;
    }

}
