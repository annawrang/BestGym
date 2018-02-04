package bestgymptapplication;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GuiFrame {

    private DatabaseConnection dc;

    // LOGGA IN -------------------------------
    JFrame frame = new JFrame();
    private ActionHandler a = new ActionHandler();
    private Properties p = new Properties();
    private JPanel panel;
    private JPanel panelLogIn = new JPanel();
    private JPanel panelLogInCenter = new JPanel();
    private JLabel name = new JLabel("Namn");
    private JLabel password = new JLabel("Lösenord");
    private String ptName;
    List<Instructor> instructors = new ArrayList<>();
    private JList list1instructors;
    private JPasswordField passwordEntry = new JPasswordField(20);
    private JButton logIn = new JButton("Logga in");
    Instructor tempInstructor;

    private JScrollPane scroll;

    // VÄLJA MEDLEM -----------------------------
    private JPanel panelChooseMember = new JPanel();
    private JLabel welcome = new JLabel();
    private JPanel panelChooseMemberCenter = new JPanel();
    List<Member> members = new ArrayList<>();
    Member chosenMember;
    JList list2members;
    Member tempMember;
    JButton trainingHistory = new JButton("Träningshistoria");
    JButton kommentar = new JButton("Anteckningar");
    JButton newComment = new JButton("Ny anteckning");
    JPanel panelChooseMemberSouth = new JPanel();

    // ATTENDADE PASS ------------------------------
    private JPanel panelClasses = new JPanel();
    private JPanel panelClassesSouth = new JPanel();
    private JPanel panelClassesSouthEast = new JPanel();

    private JLabel träningsHistorik = new JLabel("Träningshistorik");
    List<Class> classes = new ArrayList<>();
    private JLabel selectClass = new JLabel("Välj ett pass för att se kommentar");
    private JButton läsMer = new JButton("Mer information");
    private JButton saveComment = new JButton("Spara ny kommentar");
    JList list3classes;
    private Class tempClass;
    private JTextField commentField = new JTextField(150);

    
    // COMMENTS
    List<PTComment> comments = new ArrayList<>();
    public GuiFrame(DatabaseConnection dc) {
        this.dc = dc;
        frame.setTitle("Personal Trainer Application");
        instructors = dc.getPTs();
        List<String> ptNameList = this.makeStringList(instructors);

        list1instructors = new JList(ptNameList.toArray());
        list1instructors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list1instructors.addListSelectionListener(a);
        scroll = new JScrollPane(list1instructors);
        frame.add(panelLogIn);


        panelLogIn.add(name);
        panelLogIn.add(scroll);

        panelLogIn.add(password);
        panelLogIn.add(passwordEntry);
        panelLogIn.add(logIn);

        passwordEntry.addActionListener(a);
        logIn.addActionListener(a);

        frame.add(panelLogIn);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocation(1000, 400);
        frame.setSize(600, 500);
    }

    public void setChooseMember() {
        members = dc.getMembers();
        List<String> memberNameList = makeStringList(members);
        panelChooseMember.setLayout(new BorderLayout());
        panelChooseMemberCenter.setLayout(new BorderLayout());

        panelChooseMember.add(panelChooseMemberCenter, BorderLayout.CENTER);
        list2members = new JList(memberNameList.toArray());
        scroll = new JScrollPane(list2members);
        panelChooseMemberCenter.add(scroll, BorderLayout.WEST);
        panelChooseMemberCenter.add(trainingHistory, BorderLayout.EAST);



        list2members.addListSelectionListener(a);
        trainingHistory.addActionListener(a);

    }

    public void setTrainingHistory(Member member) {
        classes = dc.getClasses(member);
        List<String> classInfoList = this.makeInfoList(classes);
        list3classes = new JList(classInfoList.toArray());
        scroll = new JScrollPane(list3classes);
        panelClasses.setLayout(new GridLayout(4,1));
        panelClasses.add(selectClass);
        panelClasses.add(scroll);

        panelClassesSouth.setLayout(new GridLayout(2,1));
        panelClassesSouthEast.setLayout(new GridLayout(1,2));
        panelClasses.add(commentField);
        panelClasses.add(panelClassesSouthEast);
        panelClassesSouthEast.add(läsMer);
        panelClassesSouthEast.add(saveComment);

        list3classes.addListSelectionListener(a);

    }
    
    public void showComment(){
        comments = dc.getComments(tempMember);
                int index = 0;
                int count = 0;
        for(Class c : classes){
            if(c.getId()==tempClass.getId()){
                index = count;
            }
            count++;
        }

        commentField.setText(comments.get(index).getComment());
        frame.revalidate();
        frame.repaint();
    }

    class ActionHandler implements ActionListener, ListSelectionListener {

        String passwordIn;
        PreparedStatement pStmt;
        ResultSet rs;

        public void makeConnection() {
            try (Connection con = DriverManager.getConnection(
                    p.getProperty("ConnectionString"),
                    p.getProperty("username"),
                    p.getProperty("password"))) {

            } catch (SQLException ex) {
                Logger.getLogger(GuiFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logIn) {
                passwordIn = new String(passwordEntry.getPassword());
                if (passwordIn == null) {
                    System.out.println("Du måste skriva in ett lösenord.");
                } else if (tempInstructor == null) {
                    System.out.println("Du måste välja en användare.");
                } else {
                    if (tempInstructor.getPassword().equals(passwordIn)) {
                        setChooseMember();
                        frame.remove(panelLogIn);
                        frame.add(panelChooseMember);
                        frame.revalidate();
                        frame.repaint();
                        System.out.println("Inloggad!");
                    } else {
                        System.out.println("Fel lösenord.");
                    }
                }

            } else if (e.getSource() == trainingHistory) {
                if (tempMember == null) {
                    System.out.println("Du måste välja en medlem");
                } else {
                    frame.remove(panelChooseMember);
                    setTrainingHistory(tempMember);
                    frame.add(panelClasses);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == list1instructors) {
                if (e.getValueIsAdjusting() == false) {
                    int index = list1instructors.getSelectedIndex();
                    tempInstructor = instructors.get(index);
                }
            } else if (e.getSource() == list2members) {
                if (e.getValueIsAdjusting() == false) {
                    int index = list2members.getSelectedIndex();
                    tempMember = members.get(index);
                }
            } else if (e.getSource() == list3classes) {
                if (e.getValueIsAdjusting() == false) {
                    int index = list3classes.getSelectedIndex();
                    tempClass = classes.get(index);
                    showComment();
                }
            } 
        }
    }

    public List<String> makeStringList(List<? extends Person> personList) {
        List<String> nameList = new ArrayList<>();
        for (Person i : personList) {
            nameList.add(i.getFirstName() + " " + i.getSurName());
        }
        return nameList;
    }

    public List<String> makeInfoList(List<Class> classList) {
        List<String> stringList = new ArrayList<>();
        for (Class c : classList) {
            String temp = c.getDateAndTime() + "    " + c.getName().toUpperCase() + "     "
                    + c.getInctructor().getFirstName() + " "
                    + c.getInctructor().getSurName();
            stringList.add(temp);
            System.out.println(temp);
        }

        return stringList;
    }

}
