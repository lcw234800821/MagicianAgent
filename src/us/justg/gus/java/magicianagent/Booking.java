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

import java.sql.Timestamp;

/**
 *
 * @author hfs5022
 */
public class Booking {

    private final Timestamp timestamp;
    private final Holiday holiday;
    private final Customer customer;
    private final Magician magician;

    public Booking(Timestamp timestamp, Holiday holiday, Customer customer, Magician magician) {
        this.timestamp = timestamp;
        this.holiday = holiday;
        this.customer = customer;
        this.magician = magician;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Holiday getHoliday() {
        return holiday;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Magician getMagician() {
        return magician;
    }

}
