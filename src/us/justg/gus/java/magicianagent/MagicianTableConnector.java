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
public class MagicianTableConnector extends MagicianAgentConnector {

    PreparedStatement getAllMagicians;
    PreparedStatement getFreeMagicians;
    PreparedStatement addMagician;

    public MagicianTableConnector() {
        super();

        try {

            Connection connection = getConnection();

            // Instantiate prepared statements.
            getAllMagicians = connection.prepareStatement(
                    "SELECT name "
                    + "FROM magician"
            /*+ "ORDER BY name ASC"*/
            );
            getFreeMagicians = connection.prepareStatement(
                    "SELECT name "
                    + "FROM magician "
                    /*+ "ORDER BY name ASC"*/
                    + "WHERE "
                    + "name NOT IN "
                    + "(SELECT magician "
                    + "FROM bookings "
                    + "WHERE holiday = ? "
                    + "AND name <> ?)"
            );
            addMagician = connection.prepareStatement(
                            "INSERT INTO magician "
                            + "VALUES (?)"
            );
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Magician> getAllMagicians() {

        List<Magician> results = null;

        try (ResultSet resultSet = getAllMagicians.executeQuery()) {

            results = new ArrayList<>();

            while (resultSet.next()) {
                results.add(new Magician(resultSet.getString("name")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<Magician> getFreeMagicians(String holiday, String name) {

        List<Magician> results = null;

        try {
            getFreeMagicians.setString(1, holiday);
            getFreeMagicians.setString(2, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet resultSet = getFreeMagicians.executeQuery()) {

            results = new ArrayList<>();

            while (resultSet.next()) {
                results.add(new Magician(resultSet.getString("name")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;

    }
    
    // Throws exception if not added - likely because magician exists.
    public void addMagician(String name) throws SQLException {
        
        addMagician.setString(1, name);
        addMagician.executeUpdate();        
        
    }
}
