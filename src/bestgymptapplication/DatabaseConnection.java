package bestgymptapplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

public class DatabaseConnection {

    Properties p = new Properties();
    List<Instructor> instructorList = new ArrayList<>();
    List<PTComment> commentList = new ArrayList<>();
    List<Member> memberList = new ArrayList<>();
    List<Class> classList = new ArrayList<>();

    public DatabaseConnection() {
        try {
            p.load(new FileInputStream("src/BestGymPTApplication/settings.properties"));
            java.lang.Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Instructor> getPTs() {
        List<Instructor> instructorList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(
                p.getProperty("ConnectionString"),
                p.getProperty("username"),
                p.getProperty("password"))) {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "Select Employee.firstname, Employee.password, "
                    + "Employee.surname, Employee.id from Employee "
                    + "inner join Instructor on Instructor.Employee_id=Employee.id "
                    + "where Instructor.PTqualification=true;");

            while (rs.next()) {
                String namn = rs.getString("Employee.firstname");
                String surname = rs.getString("Employee.surname");
                int id = rs.getInt("Employee.id");
                String password = rs.getString("Employee.password");
                instructorList.add(new Instructor(id, namn, surname, password));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instructorList;

    }

    public List<Member> getMembers() {
        List<Member> memberList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(
                p.getProperty("ConnectionString"),
                p.getProperty("username"),
                p.getProperty("password"))) {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "Select Member.firstname, Member.surname, "
                    + "Member.id from Member; ");

            while (rs.next()) {
                String name = rs.getString("Member.firstname");
                String surname = rs.getString("Member.surname");
                int id = rs.getInt("Member.id");
                memberList.add(new Member(id, name, surname));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return memberList;
    }

    public List<Class> getClasses(Member member) {
        List<Class> classList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(
                p.getProperty("ConnectionString"),
                p.getProperty("username"),
                p.getProperty("password"))) {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "select Class.dateAndTime, ExerciseType.name, "
                    + "Employee.firstname, Employee.surname, PTComment.comment from Class "
                    + "inner join ExerciseType on ExerciseType.id=Class.exercisetype_id "
                    + "inner join Instructor on Instructor.id=Class.instructor_id "
                    + "inner join Employee on Employee.id = Instructor.employee_id "
                    + "inner join PTComment on Class.id=PTComment.class_id "
                    + "inner join Member on Member.id=PTComment.Member_id "
                    + "where Member.id=" + member.getId() + ";");

            while (rs.next()) {
                String name = rs.getString("Employee.firstname");
                String surname = rs.getString("Employee.surname");
                String exerciseType = rs.getString("ExerciseType.name");
                Date date = rs.getDate("Class.dateAndTime");
                String comment = rs.getString("PTComment.comment");
                Instructor tempInstructor = new Instructor(name, surname);
                classList.add(new Class(tempInstructor, new ExerciseType(exerciseType), exerciseType, date, comment));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return classList;
    }

    public List<PTComment> getComments(Member tempMember) {
        List<PTComment> commentList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(
                p.getProperty("ConnectionString"),
                p.getProperty("username"),
                p.getProperty("password"))) {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "select PTComment.id, PTComment.Class_id, PTComment.Member_id, "
                    + "PTComment.comment, PTComment.Instructor_id "
                    + "from PTComment inner join Member on Member.id = "
                    + "PTComment.Member_id "
                    + "where Member.id=" + tempMember.getId() + ";");

            while (rs.next()) {
                int id = rs.getInt("PTComment.id");
                int classId = rs.getInt("PTComment.Class_id");
                int memberId = rs.getInt("PTComment.Member_id");
                int instructorId = rs.getInt("PTComment.Instructor_id");
                String comment = rs.getString("PTComment.comment");
                Instructor instruct = instructorList.get(instructorId);
                Member memb = memberList.get(memberId);
                Class clas = classList.get(classId);
                commentList.add(new PTComment(id, instruct, memb, comment, clas));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return commentList;
    }

}
