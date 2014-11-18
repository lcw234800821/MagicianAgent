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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
    JPanel statusButtonContainer;           // Right side
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
        // Instantiate radio buttons.
        holidayRadioButton = new JRadioButton("Holiday");
        magicianRadioButton = new JRadioButton("Magician");
        waitlistRadioButton = new JRadioButton("Waitlist");
        // Instantiate dropdown.
        dropDown = new JComboBox();
        //dropDown.setPrototypeDisplayValue("                          ");
        // Instantiate radio button container.
        radioButtonsContainer = new JPanel();
        BoxLayout bl = new BoxLayout(radioButtonsContainer, BoxLayout.LINE_AXIS);
        radioButtonsContainer.setLayout(bl);
        radioButtonsContainer.setBorder(BorderFactory.createTitledBorder("Status of"));
        // Add radio buttons and dropdown.
        radioButtonsContainer.add(holidayRadioButton);
        radioButtonsContainer.add(magicianRadioButton);
        radioButtonsContainer.add(waitlistRadioButton);
        radioButtonsContainer.add(dropDown);
        // Add the radio butttons into a group.
        radioButtonsGroup = new ButtonGroup();
        radioButtonsGroup.add(holidayRadioButton);
        radioButtonsGroup.add(magicianRadioButton);
        radioButtonsGroup.add(waitlistRadioButton);
        // Instantiate right container and add in button
        statusButton = new JButton("Status");
        statusButtonContainer = new JPanel(new FlowLayout());
        statusButtonContainer.add(statusButton);
        
        // Adding event handlers.
        // Holiday radio button
        holidayRadioButton.addActionListener(new ActionListener() {
            // Basically, what we do here is get the list of holidays and add
            //      that list to the combobox for selection.
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Enable the combo box.
                dropDown.setEnabled(true);
                
                // Populate the combo box.
                dropDown.removeAllItems();
                HolidayTableConnector connector = new HolidayTableConnector();
                ArrayList<Holiday> holidays = (ArrayList<Holiday>) connector.getAllHolidays();
                connector.close();
                for (Holiday h : holidays) dropDown.addItem(h.toString());
                
            }
        });
        // Magician radio button
        magicianRadioButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // Enable the combo box.
                dropDown.setEnabled(true);
                
                // Populate the combo box.
                dropDown.removeAllItems();
                MagicianTableConnector connector = new MagicianTableConnector();
                ArrayList<Magician> magicians = (ArrayList<Magician>) connector.getAllMagicians();
                connector.close();
                for (Magician m : magicians) dropDown.addItem(m.toString());            
            }
        });
        // Waitlist radio button
        waitlistRadioButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dropDown.removeAllItems();
                dropDown.setEnabled(false);
            }
        });
        // Status button
        statusButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Return if nothing selected (and we're not looking for 
                //      the waiting list...
                if (!waitlistRadioButton.isSelected() 
                        && dropDown.getSelectedItem() == null) return; 
                
                if (waitlistRadioButton.isSelected()) {
                    
                    String query = "SELECT * FROM waitlist";
                    
                    List<String> args = new ArrayList<>();
                    
                    try {
                        table.setModel(new StatusTableModel(MagicianAgentConnector.URL,
                                MagicianAgentConnector.USERNAME,
                                MagicianAgentConnector.PASSWORD,
                                query, args));
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                    
                }
                
                //
                // NOTE: YOU COULD PROBABLY MAKE A NICE FUNCTION FOR THE NEXT
                //      TWO ELSE-IFs 
                //
                else if (magicianRadioButton.isSelected()) {
                    
                    String query = "SELECT timestamp, holiday, customer "
                            + "FROM bookings "
                            + "WHERE magician = ?";                           
                    
                    // Our arguments.
                    List<String> args = new ArrayList<>();
                    args.add((String)dropDown.getSelectedItem());
                    
                    try {
                        table.setModel(new StatusTableModel(MagicianAgentConnector.URL,
                                MagicianAgentConnector.USERNAME,
                                MagicianAgentConnector.PASSWORD,
                                query, args));
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
                
                else if (holidayRadioButton.isSelected()) {
                    
                    String query = "SELECT timestamp, magician, customer "
                            + "FROM bookings "
                            + "WHERE holiday = ?";                           
                    
                    // Our arguments.
                    List<String> args = new ArrayList<>();
                    args.add((String)dropDown.getSelectedItem());
                    
                    try {
                        table.setModel(new StatusTableModel(MagicianAgentConnector.URL,
                                MagicianAgentConnector.USERNAME,
                                MagicianAgentConnector.PASSWORD,
                                query, args));
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
                
                
                // Set the size of the table.
                refreshTableSize();
            }
                    
           
        });
        
        
        //-BOTTOM HALF----------------------------------------------------------
        table = new JTable();
        tableScrollPane = new JScrollPane(table);
        

        // Adding in the two halves.
        top.add(radioButtonsContainer);
        top.add(statusButtonContainer);
        
        add(top, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }
    
    
    private void refreshTableSize() {
        Dimension tableSize = new Dimension(getSize().width, tableScrollPane.getSize().height);
        tableScrollPane.setPreferredSize(tableSize);
        validate();
    }
        
    
    class StatusTableModel extends AbstractTableModel { 
        
        private final Connection connection;
        private PreparedStatement preparedStatement;
        private ResultSet resultSet;
        private ResultSetMetaData metadata;
        private boolean connected = false;
        
        private int rowCount;
        private int columnCount;

        public StatusTableModel(String url, String username,
                String password, String query, List<String> args) throws SQLException {
            
            connection = DriverManager.getConnection(url, username, password);
            
            connected = true;
            
            executeGivenQuery(query, args);
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

        @Override
        public String getColumnName(int i) {
            try {
                char[] columnName = metadata.getColumnName(i+1)
                        .toLowerCase()
                        .toCharArray();
                String firstLetter = columnName[0] + "";
                columnName[0] = firstLetter.toUpperCase().toCharArray()[0];
                
                return new String(columnName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            return super.getColumnName(i);
        }
        
        

        private void executeGivenQuery(String query, List<String> args) 
                throws SQLException, IllegalStateException {
            
            // Throw exception if we didn't connect.
            if(!connected) throw new IllegalStateException("Not connected.");
            
            // Prepare our statement.
            preparedStatement = connection.prepareStatement(query, 
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_READ_ONLY);
            
            // Set arguments.
            for (int i = 1; i < args.size() + 1; i++) {
                preparedStatement.setString(i, args.get(i-1));
            }
            
            
            
            // Execute query.
            resultSet = preparedStatement.executeQuery();
            
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
