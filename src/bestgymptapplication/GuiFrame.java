package bestgymptapplication;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GuiFrame {

    private DatabaseConnection dc;

    // LOGGA IN -------------------------------
    protected JFrame frame = new JFrame();
    private ActionHandler ah = new ActionHandler(this);
    
    private Properties p = new Properties();
    protected JPanel panelLogIn = new JPanel();
    private JPanel panelLogInCenter = new JPanel();
    private JPanel panelLogInOne = new JPanel();
    private JPanel panelLogInTwo = new JPanel();
    private JLabel empty = new JLabel("                   ");
    private JLabel name = new JLabel("NAMN              ", SwingConstants.RIGHT);
    private JLabel password = new JLabel("LÖSENORD    ", SwingConstants.RIGHT);
    private String ptName;
    protected List<Instructor> instructors = new ArrayList<>();
    protected JList list1instructors;
    protected JPasswordField passwordEntry = new JPasswordField(20);
    protected JButton logIn = new JButton("Logga in");
    protected Instructor tempInstructor;

    private JScrollPane scroll;

    // VÄLJA MEDLEM -----------------------------
    protected JPanel panelChooseMember = new JPanel();
    private JLabel welcome = new JLabel();
    private JPanel panelChooseMemberCenter = new JPanel();
    protected List<Member> members = new ArrayList<>();
    private Member chosenMember;
    protected JList list2members;
    protected Member tempMember;
    protected JButton trainingHistory = new JButton("Se träningshistoria");
    private JButton kommentar = new JButton("Anteckningar");
    private JButton newComment = new JButton("Ny anteckning");
    private JPanel panelChooseMemberSouth = new JPanel();
    private JLabel chooseMember = new JLabel("Välj en medlem", SwingConstants.CENTER);

    // ATTENDADE PASS ------------------------------
    protected JPanel panelClasses = new JPanel();
    private JPanel panelClassesSouth = new JPanel();
    private JPanel panelClassesSouthEast = new JPanel();

    private JLabel träningsHistorik = new JLabel("Träningshistorik");
    protected List<Class> classes = new ArrayList<>();
    private JLabel selectClass = new JLabel("Välj ett pass för att se kommentar");
    protected JButton edit = new JButton("LÄGG TILL ANTECKNING");
    protected JButton saveComment = new JButton("SPARA");
    protected JList list3classes;
    protected Class tempClass;
    protected JTextField commentField = new JTextField(350);
    private List<String> classInfoList;
    private JPanel panelClassesNorth = new JPanel(); 

    // COMMENTS
    private List<PTComment> comments = new ArrayList<>();

    public GuiFrame(DatabaseConnection dc) {
        this.dc = dc;
        frame.setTitle("Personal Trainer Application");
        instructors = dc.getPTs();
        List<String> ptNameList = this.makeStringList(instructors);

        list1instructors = new JList(ptNameList.toArray());
        list1instructors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list1instructors.addListSelectionListener(ah);
        scroll = new JScrollPane(list1instructors);
        frame.add(panelLogIn);

        panelLogIn.setLayout(new GridLayout(2,1));
        panelLogIn.add(panelLogInCenter);
        panelLogInCenter.setLayout(new GridLayout(4,1));
                panelLogInCenter.add(empty);
        panelLogInCenter.add(panelLogInOne);
        panelLogInOne.setLayout(new GridLayout(1,2));
        panelLogInCenter.add(panelLogInTwo);
        panelLogInTwo.setLayout(new GridLayout());
        

        panelLogInOne.add(name);
        panelLogInOne.add(scroll);

        panelLogInTwo.add(password);
        panelLogInTwo.add(passwordEntry);
        panelLogInCenter.add(logIn);

        passwordEntry.addActionListener(ah);
        logIn.addActionListener(ah);

        frame.add(panelLogIn);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocation(1000, 400);
        frame.setSize(500, 400);
        frame.getRootPane().setDefaultButton(logIn);
    }

    public void setChooseMember() {
        members = dc.getMembers();
        List<String> memberNameList = makeStringList(members);
        
        Font f = new Font("Helvetica", Font.BOLD, 20);
        
        panelChooseMember.setLayout(new BorderLayout());
        panelChooseMemberCenter.setLayout(new BorderLayout());

        panelChooseMember.add(panelChooseMemberCenter, BorderLayout.CENTER);
        list2members = new JList(memberNameList.toArray());
        scroll = new JScrollPane(list2members);
        chooseMember.setFont(f);
        panelChooseMemberCenter.add(chooseMember, BorderLayout.NORTH);
        panelChooseMemberCenter.add(scroll, BorderLayout.CENTER);
        panelChooseMemberCenter.add(trainingHistory, BorderLayout.SOUTH);

        list2members.addListSelectionListener(ah);
        trainingHistory.addActionListener(ah);

    }

    public void setTrainingHistory(Member member) {
        classes = dc.getClasses(member);
        classInfoList = this.makeInfoList(classes);
        list3classes = new JList(classInfoList.toArray());
        scroll = new JScrollPane(list3classes);
        panelClasses.setLayout(new GridLayout(4, 1));
        panelClasses.add(panelClassesNorth);
        panelClasses.add(scroll);
        panelClassesNorth.add(selectClass);

        panelClassesSouth.setLayout(new GridLayout(1, 2));
        panelClassesSouthEast.setLayout(new GridLayout(1, 2));
        panelClasses.add(commentField);
        commentField.setEditable(false);
        panelClasses.add(panelClassesSouthEast);
        panelClassesSouthEast.add(edit);
        panelClassesSouthEast.add(saveComment);

        list3classes.addListSelectionListener(ah);
    }

    public void showComment() {
        classes.stream().filter((c) -> (c.getId() == tempClass.getId())).forEachOrdered((c) -> {
            commentField.setText(c.getComment());
        });
        edit.addActionListener(ah);
        frame.revalidate();
        frame.repaint();
    }

    public void makeChangeableComment() {
        String in = commentField.getText();
        if (!(in.equalsIgnoreCase("Ingen anteckning."))) {
            edit.setForeground(Color.black);
            edit.setText("ÄNDRA");
        } else {
            edit.setForeground(Color.red);
            edit.setText("LÄGG TILL ANTECKNING");
        }
    }
    
    public void saveComment(){
        dc.saveNewComment(tempClass.getId(), tempMember.getId(), tempInstructor.getId(), commentField.getText());
    }


    public List<String> makeStringList(List<? extends Person> personList) {
        List<String> nameList = new ArrayList<>();
        personList.forEach((i) -> {
            nameList.add(i.getFirstName() + " " + i.getSurName());
        });
        return nameList;
    }

    public List<String> makeInfoList(List<Class> classList) {
        List<String> stringList = new ArrayList<>();
        classList.stream().map((c) -> c.getDateAndTime() + "    " + c.getName().toUpperCase() + "     "
                + c.getInctructor().getFirstName() + " "
                + c.getInctructor().getSurName()).map((temp) -> {
                    stringList.add(temp);
            return temp;
        }).forEachOrdered((temp) -> {
            System.out.println(temp);
        });

        return stringList;
    }

}
