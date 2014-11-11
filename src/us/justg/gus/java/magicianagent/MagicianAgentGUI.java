
package us.justg.gus.java.magicianagent;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * 
 * Notes:
 * I'm thinking tabs - one for Status, one for Book, and expand as needed?
 * 
 * @author hfs5022
 */
public class MagicianAgentGUI extends JFrame {
    
    // Dimension of the window. 
    private final Dimension d = new Dimension(400,120);
    
    JTabbedPane jTabbedPane;
    
    
    public MagicianAgentGUI() {
        // Open window and set properties.
        super("Magician Agent");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(d);                                     // Set size
        setResizable(false);                            // Freeze size
        //setLayout(new FlowLayout(FlowLayout.CENTER));   // Layout
        setLocationRelativeTo(null);                    // Center window
        
        jTabbedPane = new JTabbedPane();
        add(jTabbedPane);
        
        
        
        // Finally, set visible.
        setVisible(true);
    }
}
