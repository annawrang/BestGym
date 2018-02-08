package bestgymptapplication;

import javax.swing.JOptionPane;

public class BestGymPTApplication{

    DatabaseConnection dc;
    GuiFrame gf;

    public BestGymPTApplication() {
        dc = new DatabaseConnection();
        gf = new GuiFrame(dc);

    }


    public static void main(String[] args) {
        BestGymPTApplication ptApp = new BestGymPTApplication();


    }

}
