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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author hfs5022
 */
public class StatusTab extends JPanel {
    
    // Top section
    JPanel top;
    JPanel radioButtonsContainer;       // Left side
    ButtonGroup radioButtonsGroup; 
    JRadioButton holidayRadioButton;
    JRadioButton magicianRadioButton;
    JRadioButton waitlistRadioButton;
    JPanel dropDownContainer;           // Right side
    JComboBox<String> dropDown;
    JButton statusButton;
    
    // Bottom section
    JScrollPane tableScrollPane;
    JTable table;
    
    JPanel bottom;
    
    
    public StatusTab() {
        
        // Debug
        //setBorder(BorderFactory.createTitledBorder("Settings"));
        
        
        //-BUILDING TOP PORTION-------------------------------------------------
        top = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        // Radio buttons.
        holidayRadioButton = new JRadioButton("Holiday");
        magicianRadioButton = new JRadioButton("Magician");
        waitlistRadioButton = new JRadioButton("Waitlist");
        // Instantiate radio button container.
        radioButtonsContainer = new JPanel();
        BoxLayout bl = new BoxLayout(radioButtonsContainer, BoxLayout.LINE_AXIS);
        radioButtonsContainer.setLayout(bl);
        radioButtonsContainer.setBorder(BorderFactory.createTitledBorder("Status of"));
        // Add radio buttons.
        radioButtonsContainer.add(holidayRadioButton);
        radioButtonsContainer.add(magicianRadioButton);
        radioButtonsContainer.add(waitlistRadioButton);
        // Add the radio butttons into a group.
        radioButtonsGroup = new ButtonGroup();
        radioButtonsGroup.add(holidayRadioButton);
        radioButtonsGroup.add(magicianRadioButton);
        radioButtonsGroup.add(waitlistRadioButton);
        // Instantiate right container and add in combo box and button
        dropDownContainer = new JPanel(new FlowLayout());
        dropDown = new JComboBox();
        dropDown.setPrototypeDisplayValue("This is a sample");
        dropDownContainer.add(dropDown);
        statusButton = new JButton("Status");
        dropDownContainer.add(statusButton);
        
        // Adding event handlers.
        // Holiday radio button
        holidayRadioButton.addActionListener(new ActionListener() {
            // Basically, what we do here is get the list of holidays and add
            //      that list to the combobox for selection.
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Enable the combo box.
                dropDown.setEnabled(true);
                
            }
        });
        
        
        //-BOTTOM HALF----------------------------------------------------------
        table = new JTable();
        tableScrollPane = new JScrollPane(table);
        

        // Adding in the two halves.
        top.add(radioButtonsContainer);
        top.add(dropDownContainer);
        
        add(top, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }
    
    class StatusTableModel extends AbstractTableModel { 
        
        private final Connection connection;
        private final Statement statement;
        private ResultSet resultSet;
        private ResultSetMetaData metadata;
        private boolean connected = false;
        
        private int rowCount;
        private int columnCount;

        public StatusTableModel(String url, String username,
                String password, String query) throws SQLException {
            
            connection = DriverManager.getConnection(url, query, password);
            
            statement = connection
                    .createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
            
            connected = true;
            
            executeGivenQuery(query);
        }
        
        @Override
        public int getRowCount() {
            return rowCount;
        }

        @Override
        public int getColumnCount() {
            return columnCount;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) 
            throws IllegalStateException    {
            if(!connected) throw new IllegalStateException("Not connected.");
            
            try {
                // Get the specified object.
                resultSet.absolute(rowIndex + 1);
                return resultSet.getObject(columnIndex + 1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            // If we catch an exception, return empty string.
            return "";
        }

        private void executeGivenQuery(String query) 
                throws SQLException, IllegalStateException {
            
            // Throw exception if we didn't connect.
            if(!connected) throw new IllegalStateException("Not connected.");
            
            // Execute query.
            resultSet = statement.executeQuery(query);
            
            // Get metadata.
            metadata = resultSet.getMetaData();
            
            // Get number of rows.
            resultSet.last();
            rowCount = resultSet.getRow();
            resultSet.beforeFirst();
            
            // Get number of columns.
            columnCount = metadata.getColumnCount();
            
            // Call event.
            fireTableStructureChanged();
        }
        
    }
    
}
