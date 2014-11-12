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
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author hfs5022
 */
public class HolidayTableConnector extends MagicianAgentConnector {
    
    PreparedStatement getAllHolidays;
    
    public HolidayTableConnector() {
        super();
        
        try {
            
            Connection connection = getConnection();
            
            // Instantiate prepared statements.
            getAllHolidays = connection.prepareStatement(
                    "SELECT name "
                            + "FROM holiday"
                            /*+ "ORDER BY name ASC"*/
            );
            
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public List<Holiday> getAllHolidays() {
        
        List<Holiday> results = null;
        
        try (ResultSet resultSet = getAllHolidays.executeQuery()) {
            
            results = new ArrayList<>();
            
            while(resultSet.next()) results.add(new Holiday(resultSet.getString("name")));
            
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        return results;
    }
}
