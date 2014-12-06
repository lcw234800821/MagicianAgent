/*
 * Copyright (C) 2014 hfs5022
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package us.justg.gus.java.magicianagent;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author hfs5022
 */
public class EditTab extends MagicianAgentTab {
    
    private final int TEXT_FIELD_WIDTH = 20;
    
    //-ADD MAGICIAN-------------------------------------------------------------
    private JPanel addMagicianPanel;
    private JTextField addMagicianTextField;
    private JButton addMagicianButton;
    //-END ADD MAGICIAN---------------------------------------------------------

    //-DROP MAGICIAN------------------------------------------------------------
    private JPanel dropMagicianPanel;
    private JComboBox<Magician> dropMagicianComboBox;
    private JButton dropMagicianButton;
    //-END DROP MAGICIAN--------------------------------------------------------
    
    //-ADD HOLIDAY--------------------------------------------------------------
    private JPanel addHolidayPanel;
    private JTextField addHolidayTextField;
    private JButton addHolidayButton;
    //-END ADD HOLIDAY----------------------------------------------------------
    
    //-DROP HOLIDAY-------------------------------------------------------------
    private JPanel dropHolidayPanel;
    private JComboBox<Holiday> dropHolidayComboBox;
    private JButton dropHolidayButton;
    //-END DROP HOLIDAY---------------------------------------------------------
    
    //-CONSOLE------------------------------------------------------------------
    private MagicianAgentConsole console;
    //-END CONSOLE--------------------------------------------------------------
    
    
    
            

    public EditTab() {

        // Set layout.
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //-ADD MAGICIAN-------------------------------------------------------------
        addMagicianPanel = new JPanel(new FlowLayout());
        addMagicianTextField = new JTextField(TEXT_FIELD_WIDTH);
        addMagicianButton = new JButton("Add");
        
        // Set border.
        addMagicianPanel.setBorder(
                BorderFactory.createTitledBorder("Add Magician")
        );
        
        // Add listener.
        AddMagicianListener addMagicianListener = new AddMagicianListener();
        addMagicianTextField.addActionListener(addMagicianListener);
        addMagicianButton.addActionListener(addMagicianListener);
        
        
        // Add everything into the panel.
        addMagicianPanel.add(addMagicianTextField);
        addMagicianPanel.add(addMagicianButton);
        
        //
        
        //-END ADD MAGICIAN---------------------------------------------------------

        //-DROP MAGICIAN------------------------------------------------------------
        dropMagicianPanel = new JPanel(new FlowLayout());
        dropMagicianComboBox = new JComboBox<>();
        dropMagicianButton = new JButton("Drop");
        
        // Set border.
        dropMagicianPanel.setBorder(
                BorderFactory.createTitledBorder("Drop Magician")
        );
        
        // Add everything into the panel.
        dropMagicianPanel.add(dropMagicianComboBox);
        dropMagicianPanel.add(dropMagicianButton);
        //-END DROP MAGICIAN--------------------------------------------------------

        //-ADD HOLIDAY--------------------------------------------------------------
        addHolidayPanel = new JPanel(new FlowLayout());
        addHolidayTextField = new JTextField(TEXT_FIELD_WIDTH);
        addHolidayButton = new JButton("Add");
        
        // Set border.
        addHolidayPanel.setBorder(
                BorderFactory.createTitledBorder("Add Holiday")
        );
        
        
        // Add everything into the panel.
        addHolidayPanel.add(addHolidayTextField);
        addHolidayPanel.add(addHolidayButton);
        //-END ADD HOLIDAY----------------------------------------------------------

        //-DROP HOLIDAY-------------------------------------------------------------
        dropHolidayPanel = new JPanel(new FlowLayout());
        dropHolidayComboBox = new JComboBox<>();
        dropHolidayButton = new JButton("Drop");
        
        // Set border.
        dropHolidayPanel.setBorder(
                BorderFactory.createTitledBorder("Drop Holiday")
        );
        
        // Add everything into the panel.
        dropHolidayPanel.add(dropHolidayComboBox);
        dropHolidayPanel.add(dropHolidayButton);
        //-END DROP HOLIDAY-----------------------------------------------------
        
        //-CONSOLE------------------------------------------------------------------
        console = new MagicianAgentConsole("Console");
        //-END CONSOLE--------------------------------------------------------------
        
        // Update combo boxes.
        updateComboBoxes();
        
        
        
        
        // Add everything in.
        add(addMagicianPanel);
        add(dropMagicianPanel);
        add(addHolidayPanel);
        add(dropHolidayPanel);
        add(console);
    }

    public void updateComboBoxes() {
        
        // Drop Holiday combo box.
        HolidayTableConnector holidayTableConnector = 
                new HolidayTableConnector();
        ArrayList<Holiday> holidays = 
                (ArrayList) holidayTableConnector.getAllHolidays();
        dropHolidayComboBox.removeAllItems();
        for (Holiday h : holidays) dropHolidayComboBox.addItem(h);
        holidayTableConnector.close();
        
        
        
        // Drop Magician combo box.
        MagicianTableConnector magicianTableConnector = new MagicianTableConnector();
        ArrayList<Magician> magicians = 
                (ArrayList) magicianTableConnector.getAllMagicians();
        dropMagicianComboBox.removeAllItems();
        for (Magician m : magicians) dropMagicianComboBox.addItem(m);
        magicianTableConnector.close();
        
        
    }
    
  

    private class AddMagicianListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            String magicianName = addMagicianTextField.getText();
            
            // Check that the text box isn't empty.
            if (magicianName == "") {
                console.log(MagicianAgentConsole.ERROR,
                        "Please enter a magician's name first.");
                return;
            }
            
            MagicianTableConnector magicianTableConnector = 
                    new MagicianTableConnector();
            
            
            try {
                magicianTableConnector.addMagician(magicianName);
                console.log(MagicianAgentConsole.INFO,
                        "%s added to database.", magicianName);
            } catch (SQLException ex) {
                // It's likely that the magician exists if we get here. But how 
                //      can we be sure that's the error?
                console.log(MagicianAgentConsole.ERROR,
                        "Magician already exists.");
                return;
            }
            
            // Now we resolve the waitlist.
            
            
            
            
            // Finally, update combo boxes.
            updateComboBoxes();
            
        }
        
    }
    
}
