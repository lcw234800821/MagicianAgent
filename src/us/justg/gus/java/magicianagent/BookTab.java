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

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author hfs5022
 */
public class BookTab extends JPanel {
    
    final int TEXT_BOX_WIDTH = 20;
    
    JPanel topContainer;
    
    JPanel textboxContainer;
    JLabel textboxLabel;
    JTextField textbox;
    
    JComboBox holidayDropdown;
    HolidayTableConnector connector;
    
    JButton bookButton; 
    
    JPanel bottomContainer;
    
    BookTabConsole console;
    
    
    public BookTab() {
        
        setLayout(new BorderLayout());
        
        topContainer = new JPanel(new FlowLayout());
        
        textboxContainer = new JPanel();
        textboxContainer.setLayout(
                new BoxLayout(textboxContainer, BoxLayout.PAGE_AXIS)
        );
        textboxLabel = new JLabel("Customer Name");
        textbox = new JTextField(TEXT_BOX_WIDTH);
        textboxContainer.add(textboxLabel);
        textboxContainer.add(textbox);
        
        
        connector = new HolidayTableConnector();
        holidayDropdown = new JComboBox(connector.getAllHolidays().toArray());
        connector.close();        bookButton = new JButton("Book");
        
        bookButton = new JButton("Book");
        bookButton.addActionListener(new AddNewBookingListener());
        
        topContainer.add(textboxContainer);
        topContainer.add(holidayDropdown);
        topContainer.add(bookButton);
        
        
        bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setBorder(BorderFactory.createTitledBorder("Console"));
        
        console = new BookTabConsole();
        
        bottomContainer.add(console,BorderLayout.CENTER);
        
        add(topContainer,BorderLayout.NORTH);
        add(bottomContainer,BorderLayout.CENTER);
        
    }
    
    private class AddNewBookingListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            String customer = textbox.getText();
            String holiday = holidayDropdown.getSelectedItem().toString();
            List<Magician> freeMagicians; 
            
            
            if(textbox.getText() == null) return;
            if(holidayDropdown.getSelectedItem() == null) return;
            
            CustomerTableConnector connector = new CustomerTableConnector();
            
            // Add the customer to the database
            if(connector.addCustomer(textbox.getText())) {
                console.info("New customer " + textbox.getText() + " added to database.");
            } else {
                
            }
            
            MagicianTableConnector magicianTableConnector = 
                    new MagicianTableConnector();
            
            freeMagicians = 
                    magicianTableConnector
                            .getFreeMagicians(
                                    holiday,
                                    customer
                            );
            
            // No free magicians; add to waitlist.
            if(freeMagicians.size() < 1) {
                
                WaitlistTableConnector waitlist = new WaitlistTableConnector();
                
                waitlist.addToWaitlist(
                        customer,holiday
                );
            }
            // Else, book them.
            else {
                
                BookingsTableConnector bookings = new BookingsTableConnector();
                
                bookings.addToBookings(
                        customer,
                        holiday,
                        freeMagicians.get(0).toString()
                );
                
            }
            
            
            
        }
        
    }
    
    private class BookTabConsole extends JTextArea {

        public BookTabConsole() {
            super();
            
            // The console's not editable.
            setEditable(false);
            
            setLineWrap(true);
            setWrapStyleWord(true);
        }
        
        public void error(String error) {
            
            writeLine(
                    String.format(
                            "[ERROR]\t%s", error
                    )
            );
            
        }
        
        public void info(String info) {
            
            writeLine(
                    String.format(
                            "[INFO]\t%s", info
                    )
            );
        }
        
        public void writeLine(String line) {
            String message = String.format("%s%s\n", getText(), line);
            setText(message);
        }
        
    }
    
}
