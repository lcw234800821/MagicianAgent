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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author hfs5022
 */
public class BookTab extends JPanel {

    // Width of the "name" text  box.
    final int TEXT_BOX_WIDTH = 20;

    //-TOP----------------------------------------------------------------------
    JPanel topContainer;

    // Text box (and label)
    JPanel textboxContainer;
    JLabel textboxLabel;
    JTextField textbox;

    // Dropdown and the connector needed to populate it.
    JComboBox holidayDropdown;
    HolidayTableConnector connector;

    // Button to create booking.
    JButton bookButton;

    // The listener that will create the booking - will be attached to both
    //      the bookButton and the textbox.
    AddNewBookingListener listener;
    //-END TOP------------------------------------------------------------------

    //-BOTTOM-------------------------------------------------------------------
    JPanel bottomContainer;

    // Console
    JScrollPane consoleScrollPane;
    BookTabConsole console;
    //-END BOTTOM---------------------------------------------------------------

    public BookTab() {

        // Set layout.
        setLayout(new BorderLayout());

        //-TOP------------------------------------------------------------------
        topContainer = new JPanel(new FlowLayout());

        // Instantiate listener for button and textbox.
        listener = new AddNewBookingListener();

        // Set up textbox (with border label)
        textboxContainer = new JPanel();
        textboxContainer.setLayout(
                new BoxLayout(textboxContainer, BoxLayout.PAGE_AXIS)
        );
        textboxLabel = new JLabel("Customer Name");
        textbox = new JTextField(TEXT_BOX_WIDTH);
        textbox.addActionListener(listener);
        textboxContainer.add(textboxLabel);
        textboxContainer.add(textbox);

        // Set up dropdown.
        connector = new HolidayTableConnector();
        holidayDropdown = new JComboBox(connector.getAllHolidays().toArray());
        connector.close();

        // Set up button.
        bookButton = new JButton("Book");
        bookButton.addActionListener(listener);

        // Add everything.
        topContainer.add(textboxContainer);
        topContainer.add(holidayDropdown);
        topContainer.add(bookButton);
        //-END TOP--------------------------------------------------------------

        //-BOTTOM---------------------------------------------------------------
        bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setBorder(BorderFactory.createTitledBorder("Console"));

        // Set up console.
        console = new BookTabConsole();
        consoleScrollPane = new JScrollPane(console);

        // Add console.
        bottomContainer.add(consoleScrollPane, BorderLayout.CENTER);
        //-END BOTTOM-----------------------------------------------------------

        // Add everything to the tab.
        add(topContainer, BorderLayout.NORTH);
        add(bottomContainer, BorderLayout.CENTER);

    }

    /**
     * AddNewBookingListener handles the actual booking process on the Book tab.
     * It is currently attached to both the textbox (called on "enter") and the
     * "Book" button itself. It draws user input from the textbox and the drop-
     * down to complete the booking process.
     */
    private class AddNewBookingListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // Magicians who have not yet been booked.
            List<Magician> freeMagicians;

            // Instantiate connectors: we need to connect to all of the tables 
            //      at different points.
            BookingsTableConnector bookingsTableConnector = new BookingsTableConnector();
            MagicianTableConnector magicianTableConnector = new MagicianTableConnector();
            CustomerTableConnector customerTableConnector = new CustomerTableConnector();
            WaitlistTableConnector waitlistTableConnector = new WaitlistTableConnector();

            // Collect the needed user input (customer name, holiday to book)
            String customerName = textbox.getText();
            Customer customer = new Customer(customerName);
            String holidayName = holidayDropdown.getSelectedItem().toString();

            // Return if there's no input.
            if (textbox.getText() == null
                    || holidayDropdown.getSelectedItem() == null) {

                // Log to console.
                console.log(BookTabConsole.ERROR, "Missing user input.");

                return;
            }

            // Add the customer to the database.
            if (customerTableConnector.addCustomer(customer)) {

                // Log if they're new.
                console.log(
                        BookTabConsole.INFO,
                        "New customer %s added to database.",
                        textbox.getText());
            }

            // Check if they already booked that holiday.
            if (bookingsTableConnector.checkIfBooked(customerName, holidayName)) {
                console.log(
                        BookTabConsole.ERROR,
                        "Customer %s has already booked a magician for %s!",
                        customer, holidayName
                );
                return;
            }

            // Check if they are on the waitlist.
            if (waitlistTableConnector.checkIfOnWaitlist(customerName, holidayName)) {
                console.log(
                        BookTabConsole.ERROR,
                        "Customer %s is already on the waitlist for %s!",
                        customer, holidayName
                );
                return;
            }

            // If we've made it this far, we now find the free magicians.
            freeMagicians = magicianTableConnector.getFreeMagicians(
                    holidayName,
                    customerName
            );

            // No free magicians: add to waitlist.
            if (freeMagicians.size() < 1) {

                try {

                    /* Deprecated...
                     waitlistTableConnector.addToWaitlist(
                     customer, holiday
                     );*/
                    // New format:
                    WaitlistItem waitlistItem = new WaitlistItem(
                            new Timestamp(Calendar.getInstance().getTime().getTime()),
                            (Holiday) holidayDropdown.getSelectedItem(),
                            customer
                    );
                    waitlistTableConnector.addToWaitlist(waitlistItem);

                    console.log(
                            BookTabConsole.INFO,
                            "Put %s on the waitlist for %s.",
                            customerName, holidayName
                    );

                } catch (SQLException exception) {

                    exception.printStackTrace();

                    console.log(
                            BookTabConsole.ERROR,
                            "Database error; failed to put customer on the waitlist.",
                            customerName, holidayName
                    );
                }
            } // Else (if there are available magicians) book them.
            else {

                try {

                    /* Deprecated...
                     bookingsTableConnector.addToBookings(
                     customer,
                     holiday,
                     freeMagicians.get(0).toString()
                     );*/
                    // New format:
                    Booking booking = new Booking(
                            new Timestamp(Calendar.getInstance().getTime().getTime()),
                            (Holiday) holidayDropdown.getSelectedItem(),
                            customer,
                            freeMagicians.get(0)
                    );
                    bookingsTableConnector.addToBookings(booking);

                    console.log(
                            BookTabConsole.INFO,
                            "Booked %s with magician %s for %s.",
                            customerName, freeMagicians.get(0).toString(), holidayName
                    );

                } catch (SQLException exception) {
                    exception.printStackTrace();
                    console.log(
                            BookTabConsole.ERROR,
                            "Database error; failed to book customer.",
                            customerName, freeMagicians.get(0).toString(), holidayName
                    );
                }

            }

            // Close all connections.
            bookingsTableConnector.close();
            magicianTableConnector.close();
            customerTableConnector.close();
            waitlistTableConnector.close();

        }

    }

    /**
     * BookTabConsole is a simple class that acts as a logging system, giving
     * useful feedback to the user.
     */
    private class BookTabConsole extends JTextArea {

        public static final String INFO = "INFO";
        public static final String WARNING = "WARN";
        public static final String ERROR = "ERROR";

        public BookTabConsole() {
            super();

            // The console's not editable.
            setEditable(false);

            // Word wrapping.
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        // Logging with variable levels of alert.
        public void log(String level, String format, Object... args) {
            writeLine(
                    String.format(
                            "[" + level + "]\t" + format, args
                    )
            );
        }

        // Write to textbox.
        public void writeLine(String format, Object... args) {
            String message = String.format(getText() + format + "\n", args);
            setText(message);
        }

    }

}
