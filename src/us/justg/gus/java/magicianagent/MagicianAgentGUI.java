package us.justg.gus.java.magicianagent;

import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 *
 * Notes: I'm thinking tabs - one for Status, one for Book, and expand as
 * needed?
 *
 * @author hfs5022
 */
public class MagicianAgentGUI extends JFrame {

    // Dimension of the window. 
    private final Dimension d = new Dimension(400, 400);

    JTabbedPane jTabbedPane;

    public MagicianAgentGUI() {
        // Open window.
        super("Magician Agent");

        try {
            UIManager.setLookAndFeel(new WindowsClassicLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {

        }

        // Set window properties.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(d);                                         // Set size
        //setResizable(false);                              // Freeze size
        //setLayout(new FlowLayout(FlowLayout.CENTER));     // Layout
        setLocationRelativeTo(null);                        // Center window

        jTabbedPane = new JTabbedPane();
        jTabbedPane.addTab("Book", null, new BookTab(), "Book a magician");
        jTabbedPane.addTab("Status", null, new StatusTab(), "Check the status of a holiday or magician");
        jTabbedPane.addTab("Edit Database", null, new EditTab(), "Edit data in the database.");
        
        jTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ((MagicianAgentTab)jTabbedPane.getSelectedComponent())
                        .updateComboBoxes();
            }
        });

        // Finally, add components and set visible.
        add(jTabbedPane);
        setVisible(true);
        setResizable(false);
    }
    
}
