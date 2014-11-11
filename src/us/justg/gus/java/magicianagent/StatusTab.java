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
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author hfs5022
 */
public class StatusTab extends JPanel {
    
    // Top section
    JPanel top;
    JPanel radioButtonsGroup;
    JRadioButton holidayRadioButton;
    
    JPanel bottom;
    
    
    public StatusTab() {
        
        top = new JPanel(new FlowLayout());
        
        
    }
}
