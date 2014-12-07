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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
    
    //-DROP BOOKING-------------------------------------------------------------
    private JPanel dropBookingPanel;
    private JComboBox<Customer> dropBookingCustomerComboBox;
    private JComboBox<Holiday> dropBookingHolidayComboBox;
    private JButton dropBookingButton;
    //-END DROP BOOKING---------------------------------------------------------
    
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
        
        // Add listener.
        DropMagicianListener dropMagicianListener = new DropMagicianListener();
        dropMagicianButton.addActionListener(dropMagicianListener);
        
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
        
        // Add listener.
        AddHolidayListener addHolidayListener = new AddHolidayListener();
        addHolidayTextField.addActionListener(addHolidayListener);
        addHolidayButton.addActionListener(addHolidayListener);
        
        
        // Add everything into the panel.
        addHolidayPanel.add(addHolidayTextField);
        addHolidayPanel.add(addHolidayButton);
        //-END ADD HOLIDAY----------------------------------------------------------

        //-DROP BOOKING---------------------------------------------------------
        dropBookingPanel = new JPanel(new FlowLayout());
        dropBookingCustomerComboBox = new JComboBox<>();
        dropBookingHolidayComboBox = new JComboBox<>();
        dropBookingButton = new JButton("Drop");
        
        // Set border.
        dropBookingPanel.setBorder(
                BorderFactory.createTitledBorder("Drop Booking")
        );
        
        // Add listener.
        DropBookingListener dropBookingListener = new DropBookingListener();
        dropBookingButton.addActionListener(dropBookingListener);
        
        // Add everything into the panel.
        dropBookingPanel.add(dropBookingCustomerComboBox);
        dropBookingPanel.add(dropBookingHolidayComboBox);
        dropBookingPanel.add(dropBookingButton);
        //-END DROP BOOKING-----------------------------------------------------
        
        //-CONSOLE--------------------------------------------------------------
        console = new MagicianAgentConsole("Console");
        //-END CONSOLE----------------------------------------------------------
        
        // Update combo boxes.
        updateComboBoxes();
        
        
        
        
        // Add everything in.
        add(addMagicianPanel);
        add(dropMagicianPanel);
        add(addHolidayPanel);
        add(dropBookingPanel);
        add(console);
    }

    public void updateComboBoxes() {
        
        // Drop booking holiday box.
        HolidayTableConnector holidayTableConnector = 
                new HolidayTableConnector();
        Holiday[] holidays = new Holiday[0];
        holidays = holidayTableConnector.getAllHolidays().toArray(holidays);
        dropBookingHolidayComboBox.removeAllItems();
        dropBookingHolidayComboBox.setModel(new DefaultComboBoxModel<Holiday>(holidays));
        dropBookingHolidayComboBox.setPrototypeDisplayValue(new Holiday("                                              "));
        //for (Holiday h : holidays) dropBookingHolidayComboBox.addItem(h); 
        holidayTableConnector.close();
        
        // Drop booking customer box.
        CustomerTableConnector customerTableConnector = 
                new CustomerTableConnector();
        Customer[] customers = new Customer[0];
        customers = customerTableConnector.getAllCustomers().toArray(customers);
        dropBookingCustomerComboBox.removeAllItems();
        dropBookingCustomerComboBox.setModel(new DefaultComboBoxModel<Customer>(customers));
        dropBookingCustomerComboBox.setPrototypeDisplayValue(new Customer("                           "));
        customerTableConnector.close(); 
        
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
            
            MagicianTableConnector magicianTableConnector = 
                    new MagicianTableConnector();
            WaitlistTableConnector waitlistTableConnector = 
                    new WaitlistTableConnector();
            String magicianName = addMagicianTextField.getText();
            
            // Check that the text box isn't empty.
            if (magicianName == "") {
                console.log(MagicianAgentConsole.ERROR,
                        "Please enter a magician's name first.");
                return;
            }
            
            
            try {
                magicianTableConnector.addMagician(magicianName);
                console.log(MagicianAgentConsole.INFO,
                        "Magician %s added to database.", magicianName);
            } catch (SQLException ex) {
                // It's likely that the magician exists if we get here. But how 
                //      can we be sure that's the error?
                console.log(MagicianAgentConsole.ERROR,
                        "Magician already exists.");
                return;
            }
            
            // Now we resolve the waitlist.
            console.log(MagicianAgentConsole.INFO, "Resolving waitlist...");
            ArrayList<Booking> newBookings = 
                    (ArrayList) waitlistTableConnector.resolveWaitlist();
            
            // Log the created bookings (if any created)
            console.log(MagicianAgentConsole.INFO, 
                    "%d new bookings after waitlist resolved.", newBookings.size());
            for (Booking b : newBookings) {
                console.log(MagicianAgentConsole.INFO, 
                        "Booked %s with magician %s for %s.",
                        b.getCustomer().toString(),
                        b.getMagician().toString(),
                        b.getHoliday().toString());
            }
            
            
            
            // Finally, update combo boxes.
            updateComboBoxes();
            
            
            waitlistTableConnector.close();
            magicianTableConnector.close();
        }
        
    }
    
    private class DropMagicianListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            // Connectors
            BookingsTableConnector bookingsTableConnector = 
                    new BookingsTableConnector();
            WaitlistTableConnector waitlistTableConnector = 
                    new WaitlistTableConnector();
            MagicianTableConnector magicianTableConnector =
                    new MagicianTableConnector();

            // The magician in question.
            Magician magician = (Magician) dropMagicianComboBox.getSelectedItem();


            // Before we can drop the magician, we have to remove all booki-
            //      ngs dependant on that magician.

            // All the bookings of that magician.
            ArrayList<Booking> bookingsToBeDropped =
                    (ArrayList) bookingsTableConnector
                            .getAllBookingsForMagician(magician.toString());

            // Log.
            console.log(MagicianAgentConsole.INFO,  
                    "Magician %s has %d bookings that will be moved to the waitlist.", 
                    magician.toString(),bookingsToBeDropped.size());

            // Now we'll loop, adding new waitlist items & removing bookings.
            for (Booking b : bookingsToBeDropped) {

                // Add to waitlist.
                try {
                    waitlistTableConnector.addToWaitlist(b);
                    console.log(MagicianAgentConsole.INFO,
                            "Customer %s moved to waitlist for %s.", 
                            b.getCustomer().toString(),
                            b.getHoliday().toString());

                } catch (SQLException e) {
                    console.log(MagicianAgentConsole.ERROR, 
                            "Problem adding item to waitlist.");
                }

                // Remove booking.
                try {
                    bookingsTableConnector.removeBooking(b);
                } catch (SQLException e) {
                    console.log(MagicianAgentConsole.ERROR, 
                            "Problem deleting booking.");
                }

            }

            // Now we can drop the magician from the magician table.
            try {
                magicianTableConnector.dropMagician(magician.toString());
            } catch (SQLException e) {
                e.printStackTrace();
                console.log(MagicianAgentConsole.ERROR, 
                        "Could not drop magician from table.");
            }

            // Close connections.
            bookingsTableConnector.close();
            waitlistTableConnector.close();
            magicianTableConnector.close();
            
            // Update combo boxes.
            updateComboBoxes();
        }

    }
    
    private class AddHolidayListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            HolidayTableConnector holidayTableConnector = 
                    new HolidayTableConnector();
            
            String holidayName = addHolidayTextField.getText();
            
            if(holidayName == "") {
                console.log(MagicianAgentConsole.ERROR, 
                    "Please enter a holiday first.");
                return;
            }
            
            // Add holiday.
            try {
                holidayTableConnector.addHoliday(holidayName);
            } catch (SQLException e) {
                console.log(MagicianAgentConsole.ERROR, "Could not add holiday.");
            }
            
            // Close connectors.
            holidayTableConnector.close();
            
            // Update combo boxes.
            updateComboBoxes();
        }
        
    }
    
    private class DropBookingListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            // Connectors.
            BookingsTableConnector bookingsTableConnector =
                    new BookingsTableConnector();
            WaitlistTableConnector waitlistTableConnector =
                    new WaitlistTableConnector();
            
            Customer customer =
                    (Customer) dropBookingCustomerComboBox.getSelectedItem();
            Holiday holiday = 
                    (Holiday) dropBookingHolidayComboBox.getSelectedItem();
            
            if(customer == null || holiday == null) return;
            
            // Remove booking.
            try {
                int returned = bookingsTableConnector.removeBooking(customer, holiday);
                console.log(MagicianAgentConsole.INFO, 
                        "Removed %s booking for customer %s.",
                        holiday.toString(),customer.toString());
            } catch (SQLException e) {
                console.log(MagicianAgentConsole.ERROR,
                        "Problem dropping booking.");
            }
            
            // Resolve waitlist. 
            console.log(MagicianAgentConsole.INFO, "Resolving waitlist...");
            ArrayList<Booking> newBookings = 
                    (ArrayList) waitlistTableConnector.resolveWaitlist();
            
            // Log the created bookings (if any created)
            console.log(MagicianAgentConsole.INFO, 
                    "%d new bookings after waitlist resolved.", newBookings.size());
            for (Booking b : newBookings) {
                console.log(MagicianAgentConsole.INFO, 
                        "Booked %s with magician %s for %s.",
                        b.getCustomer().toString(),
                        b.getMagician().toString(),
                        b.getHoliday().toString());
            }
            
            // Close connections.
            waitlistTableConnector.close();
            bookingsTableConnector.close();
            
            // Update combo boxes.
            updateComboBoxes();
            
        }
        
    }
    
}
