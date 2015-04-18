package pl.wrzesien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michał Wrzesień on 2015-03-08.
 */
public class MainWindow extends JFrame {
    private JComboBox cmbAdresSerwera;
    private JButton logowanieButton;
    private JButton rejestracjaButton;
    private JPanel MainWindow;

    public MainWindow() {
        initComponents();
    }

    private void initComponents() {

        //final MainWindow mainWindow = this;

        logowanieButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openLogowanieWindow(getServerIpAddress());
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainWindow);
                topFrame.dispose();
            }
        });

        rejestracjaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRejestracjaWindow(getServerIpAddress());
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainWindow);
                topFrame.dispose();
            }
        });
    }

    public String getServerIpAddress() {
        String ipAddress = cmbAdresSerwera.getSelectedItem().toString();
        System.out.println("Obecny adres IP: " + ipAddress);
        return ipAddress;
    }

    public void setCmbAdresSerwera(JComboBox cmbAdresSerwera) {
        this.cmbAdresSerwera = cmbAdresSerwera;
    }

    public void openLogowanieWindow(String serverIp)
    {
        JFrame frame = new JFrame("Komunikator - Logowanie");
        LogowanieWindow logowanieWindow = new LogowanieWindow(serverIp);
        frame.setContentPane(logowanieWindow.getLogowanieWindow());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void openRejestracjaWindow(String serverIp)
    {
        JFrame frame = new JFrame("Komunikator - Rejestracja");
        RejestracjaWindow rejestracjaWindow = new RejestracjaWindow(serverIp);
        frame.setContentPane(rejestracjaWindow.getRejestracjaWindow());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Komunikator");
        MainWindow mainwindow = new MainWindow();
        frame.setContentPane(mainwindow.MainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //Client c = new Client(obj);
    }
}

