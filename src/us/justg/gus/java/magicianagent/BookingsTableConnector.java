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
import java.util.Calendar;

/**
 *
 * @author hfs5022
 */
public class BookingsTableConnector extends MagicianAgentConnector {

    PreparedStatement addToBookings;
    PreparedStatement checkIfBooked;

    public BookingsTableConnector() {
        super();

        try {

            Connection connection = getConnection();

            // Instantiate prepared statements.
            addToBookings = connection.prepareStatement(
                    "INSERT INTO bookings "
                    + "(timestamp, customer, holiday, magician) "
                    + "VALUES "
                    + "(?,?,?,?)"
            );
            checkIfBooked = connection.prepareStatement(
                    "SELECT * FROM bookings "
                    + "WHERE "
                    + "customer = ? "
                    + "AND "
                    + "holiday = ? "
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addToBookings(String customer, String holiday, String magician)
            throws SQLException {

        addToBookings.setTimestamp(1,
                new Timestamp(Calendar.getInstance().getTime().getTime())
        );
        addToBookings.setString(2, customer);
        addToBookings.setString(3, holiday);
        addToBookings.setString(4, magician);

        addToBookings.executeUpdate();

    }

    public boolean checkIfBooked(String customer, String holiday) {
        try {

            checkIfBooked.setString(1, customer);
            checkIfBooked.setString(2, holiday);

        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }

        try (ResultSet results = checkIfBooked.executeQuery()) {

            // If there's no results, we return false.
            if (results.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
}
