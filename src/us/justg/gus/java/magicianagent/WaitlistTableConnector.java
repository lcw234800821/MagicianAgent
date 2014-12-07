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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 *
 * @author hfs5022
 */
public class WaitlistTableConnector extends MagicianAgentConnector {
    
    PreparedStatement addToWaitlist;
    PreparedStatement checkIfOnWaitlist;
    PreparedStatement getAllWaitlistItems;
    PreparedStatement dropFromWaitlist;
    
    public WaitlistTableConnector() {
        super();
        
        try {
            
            Connection connection = getConnection();
            
            // Instantiate prepared statements.
            addToWaitlist = connection.prepareStatement(
                    "INSERT INTO waitlist "
                            + "(timestamp, customer, holiday) "
                            + "VALUES "
                            + "(?,?,?)"
            );
            checkIfOnWaitlist = connection.prepareStatement(
                    "SELECT * FROM waitlist "
                            + "WHERE "
                            + "customer = ? "
                            + "AND "
                            + "holiday = ? "
            );
            getAllWaitlistItems = connection.prepareStatement(
                    "SELECT * FROM waitlist "
                            + "ORDER BY timestamp ASC"
            );
            dropFromWaitlist = connection.prepareStatement(
                    "DELETE FROM waitlist "
                            + "WHERE "
                            + "customer = ? "
                            + "AND holiday = ?"
            );
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * @deprecated 
     * @param customer
     * @param holiday
     * @throws SQLException 
     */
    public void addToWaitlist(String customer, String holiday)
            throws SQLException {

        addToWaitlist.setTimestamp(1,
                new Timestamp(Calendar.getInstance().getTime().getTime())
        );
        addToWaitlist.setString(2, customer);
        addToWaitlist.setString(3, holiday);

        addToWaitlist.executeUpdate();

    }
    
    public void addToWaitlist(WaitlistItem waitlistItem)
            throws SQLException {

        addToWaitlist.setTimestamp(1, waitlistItem.getTimestamp());
        addToWaitlist.setString(2, waitlistItem.getCustomer().toString());
        addToWaitlist.setString(3, waitlistItem.getHoliday().toString());

        addToWaitlist.executeUpdate();

    }
    
    public void addToWaitlist(Booking booking)
            throws SQLException {

        addToWaitlist.setTimestamp(1, booking.getTimestamp());
        addToWaitlist.setString(2, booking.getCustomer().toString());
        addToWaitlist.setString(3, booking.getHoliday().toString());

        addToWaitlist.executeUpdate();

    }
    
    public boolean checkIfOnWaitlist(String customer, String holiday) {
        try {
            
            checkIfOnWaitlist.setString(1, customer);
            checkIfOnWaitlist.setString(2, holiday);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
        
        try(ResultSet results = checkIfOnWaitlist.executeQuery()) {
            
            // If there's no results, we return false.
            return results.next();

            
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        } 
    }
    
    public List<WaitlistItem> getAllWaitlistItems() {
    
        List<WaitlistItem> results = null;

        try (ResultSet resultSet = getAllWaitlistItems.executeQuery()) {

            results = new ArrayList<>();

            while (resultSet.next()) {
                results.add(
                        new WaitlistItem(
                                resultSet.getObject("timestamp", Timestamp.class),
                                new Holiday(resultSet.getString("holiday")),
                                new Customer(resultSet.getString("customer"))
                        )
                );
            }

        } catch (SQLException e) {
            //e.printStackTrace();
            results = null;
        }

        return results;
    }
    
    public void dropFromWaitlist(Customer customer, Holiday holiday) 
            throws SQLException {
        dropFromWaitlist(customer.toString(), holiday.toString());
    }
    
    public void dropFromWaitlist(String customer, String holiday) 
            throws SQLException {
        
        dropFromWaitlist.setString(1, customer);
        dropFromWaitlist.setString(2, holiday);
        
        dropFromWaitlist.executeUpdate();
    }
    
    /**
     * Get all waitlist items and check if there's any open spots for them. 
     * 
     * @return A list of created bookings.
     */
    public List<Booking> resolveWaitlist() {
        
        // Table connectors.
        BookingsTableConnector bookingsTableConnector = new BookingsTableConnector();
        MagicianTableConnector magicianTableConnector = new MagicianTableConnector();
        
        // Our waitlist, sorted ascending.
        ArrayList<WaitlistItem> waitlistItems = (ArrayList) getAllWaitlistItems();
        
        // The created bookings.
        ArrayList<Booking> newBookings = new ArrayList<>();
        
        for (WaitlistItem item : waitlistItems) {
            // Get the free magicians.
            ArrayList<Magician> freeMagicians = 
                    (ArrayList)magicianTableConnector
                    .getFreeMagicians(item.getHoliday().toString(),
                            item.getCustomer().toString());
            
            // If there's a free magician...
            if(freeMagicians.size() > 0) {
                try {
                    // Add new booking.
                    Booking booking = new Booking(
                                new Timestamp(Calendar.getInstance().getTime().getTime()),
                                item.getHoliday(),
                                item.getCustomer(),
                                freeMagicians.get(0)
                        );
                    bookingsTableConnector.addToBookings(booking);
                    newBookings.add(booking);
                    
                    // Drop from waitlist.
                    dropFromWaitlist(item.getCustomer(), item.getHoliday());
                    
                } catch (SQLException e) {
                    
                }
            }
            
        }
        
        bookingsTableConnector.close();
        magicianTableConnector.close();
        
        return newBookings;
    }
}
