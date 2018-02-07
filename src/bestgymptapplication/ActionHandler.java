package bestgymptapplication;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ActionHandler implements ActionListener, ListSelectionListener {

    private GuiFrame gf;
    String passwordIn;
    PreparedStatement pStmt;
    ResultSet rs;
    Connection con;

    ActionHandler(GuiFrame gf) {
        this.gf = gf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gf.logIn) {
            passwordIn = new String(gf.passwordEntry.getPassword());
            if (passwordIn == null) {
                JOptionPane.showMessageDialog(gf.frame, "Du måste skriva in ett lösenord.");
            } else if (gf.tempInstructor == null) {
                JOptionPane.showMessageDialog(gf.frame, "Du måste välja en användare.");
            } else {
                if (gf.tempInstructor.getPassword().equals(passwordIn)) {
                    gf.setChooseMember();
                    gf.frame.remove(gf.panelLogIn);
                    gf.frame.add(gf.panelChooseMember);
                    gf.frame.revalidate();
                    gf.frame.repaint();

                } else {
                    JOptionPane.showMessageDialog(gf.frame, "Felaktigt lösenord.");
                }
            }

        } else if (e.getSource() == gf.trainingHistory) {
            if (gf.tempMember == null) {
                JOptionPane.showMessageDialog(gf.frame, "Du måste välja en medlem");
            } else {
                gf.frame.remove(gf.panelChooseMember);
                gf.setTrainingHistory(gf.tempMember);
                gf.frame.add(gf.panelClasses);
                gf.frame.revalidate();
                gf.frame.repaint();
            }
        } else if (e.getSource() == gf.edit) {
            gf.commentField.setEditable(true);
            if (!(gf.commentField.getText().equalsIgnoreCase("Ingen anteckning.") ||
                    gf.commentField.getText().equalsIgnoreCase(""))) {
                gf.commentField.setForeground(Color.BLUE);
                gf.edit.setForeground(Color.black);
            } else {
                gf.commentField.setText("Skriv kommentar här...");
                gf.commentField.setForeground(Color.BLUE);
                gf.edit.setForeground(Color.black);
            }
            gf.saveComment.setForeground(Color.red);

            gf.saveComment.addActionListener(this);
        } else if (e.getSource() == gf.saveComment) {
            gf.saveComment();
            gf.saveComment.removeActionListener(this);
            gf.commentField.setEditable(false);
            gf.commentField.setForeground(Color.black);
            gf.saveComment.setForeground(Color.black);
            gf.classes.stream().filter((ss) -> (ss == gf.tempClass)).forEachOrdered((ss) -> {
                ss.setComment(gf.commentField.getText());
            });
            gf.frame.revalidate();
            gf.frame.repaint();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == gf.list1instructors) {
            if (e.getValueIsAdjusting() == false) {
                int index = gf.list1instructors.getSelectedIndex();
                gf.tempInstructor = gf.instructors.get(index);
            }
        } else if (e.getSource() == gf.list2members) {
            if (e.getValueIsAdjusting() == false) {
                int index = gf.list2members.getSelectedIndex();
                gf.tempMember = gf.members.get(index);
            }
        } else if (e.getSource() == gf.list3classes) {
            if (e.getValueIsAdjusting() == false) {
                int index = gf.list3classes.getSelectedIndex();
                gf.tempClass = gf.classes.get(index);
                gf.showComment();
                gf.makeChangeableComment();
            }
        }
    }

}
