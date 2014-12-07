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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hfs5022
 */
public class CustomerTableConnector extends MagicianAgentConnector {

    PreparedStatement addCustomer;
    PreparedStatement getAllCustomers;

    public CustomerTableConnector() {
        super();

        try {

            Connection connection = getConnection();

            // Instantiate prepared statements.
            addCustomer = connection.prepareStatement(
                    "INSERT INTO customer VALUES ?"
            );
            getAllCustomers = connection.prepareStatement(
                    "SELECT name FROM customer"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * @deprecated 
     * @param name
     * @return 
     */
    public boolean addCustomer(String name) {

        try {

            addCustomer.setString(1, name);
            addCustomer.executeUpdate();
            return true;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }
    
    public boolean addCustomer(Customer customer) {

        try {

            addCustomer.setString(1, customer.toString());
            addCustomer.executeUpdate();
            return true;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }
    
    public List<Customer> getAllCustomers() {
            
        List<Customer> results = null;
        
        try (ResultSet resultSet = getAllCustomers.executeQuery()) {
            
            results = new ArrayList<>();

            while (resultSet.next()) {
                results.add(new Customer(resultSet.getString("name")));
            }

        } catch (SQLException e) {
            //e.printStackTrace();
            results = null;
        }
        
        return results;
    }
}
