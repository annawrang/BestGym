package bestgymptapplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
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
                    "select Class.dateAndTime, ExerciseType.name, Class.id, "
                    + "Employee.firstname, Employee.surname from Class "
                    + "inner join ExerciseType on ExerciseType.id=Class.exercisetype_id "
                    + "inner join Instructor on Instructor.id=Class.instructor_id "
                    + "inner join Employee on Employee.id = Instructor.employee_id "
                    //                    + "left join PTComment on Class.id=PTComment.class_id "
                    + "left join ClassesBooked on ClassesBooked.Class_id=Class.id "
                    + "left join Member on Member.id=ClassesBooked.Member_id "
                    + "where ClassesBooked.Member_id=" + member.getId() + ";");

            while (rs.next()) {
                String name = rs.getString("Employee.firstname");
                String surname = rs.getString("Employee.surname");
                int classId = rs.getInt("Class.id");
                String exerciseType = rs.getString("ExerciseType.name");
                Date date = rs.getDate("Class.dateAndTime");

                Instructor tempInstructor = new Instructor(name, surname);
                classList.add(new Class(classId, tempInstructor, new ExerciseType(exerciseType), exerciseType, date));
            }
            Statement statement = con.createStatement();
            ResultSet r = stmt.executeQuery("Select PTComment.comment, PTComment.Class_id "
                    + "from PTComment "
                    + "where PTComment.Member_id=" + member.getId());

            while (r.next()) {
                String comment = r.getString("PTComment.comment");
                int clasId = r.getInt("PTComment.Class_id");
                for (Class c : classList) {
                    if (clasId == c.getId()) {
                        c.setComment(comment);
                    } else if (c.getComment() == null) {
                        c.setComment("Ingen anteckning.");
                    }
                }
            }
            for (Class cc : classList) {
                System.out.println(cc.getId() + " " + cc.getComment());
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return classList;
    }

    public boolean saveNewComment(int classId, int memberId, int instructorId, String comment) {
        boolean success = false;
        try (Connection con = DriverManager.getConnection(
                p.getProperty("ConnectionString"),
                p.getProperty("username"),
                p.getProperty("password"))) {
            int beforeCount = 0;
            int afterCount = 0;
            Statement before = con.createStatement();
            ResultSet rBefore = before.executeQuery("select count(*) "
                    + "from PTComment where PTComment.Member_id=" + memberId);
            while(rBefore.next()){
                beforeCount=rBefore.getInt("count(*)");
            }

            CallableStatement cStmt = con.prepareCall(
                    "call add_ptcomment(?,?,?,?);");
            cStmt.setInt(1, classId);
            cStmt.setInt(2, memberId);
            cStmt.setInt(3, instructorId);
            cStmt.setString(4, comment);


            ResultSet rs = cStmt.executeQuery();

            Statement after = con.createStatement();
            ResultSet rAfter = before.executeQuery("select count(*) "
                    + "from PTComment where PTComment.Member_id=" + memberId);
            while(rAfter.next()){
                afterCount=rAfter.getInt("count(*)");
            }
            
            if(afterCount == (beforeCount+1)){
                success = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            return success;
        }
        return success;
    }

}
