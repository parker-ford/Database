
public class Main {
    public static void main(String[] args){
        JDBC jdbc = new JDBC(args[0], args[1]);
        jdbc.run();
    }
}
