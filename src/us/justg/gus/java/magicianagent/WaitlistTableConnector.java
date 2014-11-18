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

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
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
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public boolean addToWaitlist(String customer, String holiday) {
                
        try {
            
            addToWaitlist.setTimestamp(1, 
                    new Timestamp(Calendar.getInstance().getTime().getTime())
            );
            addToWaitlist.setString(2, customer);
            addToWaitlist.setString(3, holiday);
            
            addToWaitlist.executeUpdate();
            
            return true;

            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
        
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
            if(results.next()) return true;
            else return false;

            
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        } 
    }
}
