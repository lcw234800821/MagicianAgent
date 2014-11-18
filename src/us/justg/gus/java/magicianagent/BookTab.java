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

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
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
import javax.swing.JScrollPane;
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
    
    JScrollPane consoleScrollPane;
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
        consoleScrollPane = new JScrollPane(console);
        
        bottomContainer.add(consoleScrollPane,BorderLayout.CENTER);
        
        add(topContainer,BorderLayout.NORTH);
        add(bottomContainer,BorderLayout.CENTER);
        
    }
    
    private class AddNewBookingListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            // We need to connect to all of the tables at different points.
            BookingsTableConnector bookings = new BookingsTableConnector();
            MagicianTableConnector magicianTableConnector = 
                    new MagicianTableConnector();
            CustomerTableConnector connector = new CustomerTableConnector();
            WaitlistTableConnector waitlist = new WaitlistTableConnector();
            
            String customer = textbox.getText();
            String holiday = holidayDropdown.getSelectedItem().toString();
            List<Magician> freeMagicians; 
            
            
            if(textbox.getText() == null) return;
            if(holidayDropdown.getSelectedItem() == null) return;
            
            
            
            // Add the customer to the database
            if(connector.addCustomer(textbox.getText())) {
                console.info("New customer " + textbox.getText() + " added to database.");
            } else {
                console.info(
                        String.format("Customer %s already in database, no need to add.", 
                                textbox.getText())
                );
            }
            
            // Check if they already booked that holiday.
            if(bookings.checkIfBooked(customer, holiday)) {
                console.error(
                        String.format(
                                "Customer %s has already booked a magician for %s!",
                                customer, holiday)
                );
                return;
            }
            
            // Check if they are on the waitlist.
            if(waitlist.checkIfOnWaitlist(customer, holiday)) {
                console.error(
                        String.format(
                                "Customer %s is already on the waitlist for %s!",
                                customer, holiday)
                );
                return;
            }
            
            
            freeMagicians = 
                    magicianTableConnector
                            .getFreeMagicians(
                                    holiday,
                                    customer
                            );
            
            // No free magicians; add to waitlist.
            if(freeMagicians.size() < 1) {
                
                
                
                waitlist.addToWaitlist(
                        customer,holiday
                );
                
                console.info(
                        String.format(
                                "Put customer %s on the waitlist for holiday %s.", 
                                customer,holiday
                        )
                );
            }
            // Else, book them.
            else {
                
                
                
                bookings.addToBookings(
                        customer,
                        holiday,
                        freeMagicians.get(0).toString()
                );
                
                console.info(
                        String.format(
                                "Booked customer %s with magician %s for holiday %s.", 
                                customer, freeMagicians.get(0).toString(), holiday
                        )
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
        
        public void warning(String warning){
            
            writeLine(
                    String.format(
                            "[WARN]\t%s", warning
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
