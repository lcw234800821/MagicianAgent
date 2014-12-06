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
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author hfs5022
 */
public class MagicianAgentConsole extends JPanel {

    public static final String INFO = "INFO";
    public static final String WARNING = "WARN";
    public static final String ERROR = "ERROR";
    
    private JTextArea consoleTextArea;
    private JScrollPane outerScrollPane;

    public MagicianAgentConsole(String title) {
        
        super();
        
        // Set layout.
        setLayout(new BorderLayout());
        
        consoleTextArea = new JTextArea();

        // The console's not editable.
        consoleTextArea.setEditable(false);

        // Word wrapping.
        consoleTextArea.setLineWrap(true);
        consoleTextArea.setWrapStyleWord(true);
        
        // Set border.
        setBorder(BorderFactory.createTitledBorder(title));
        
        // Set up scroll pane.
        outerScrollPane = new JScrollPane(consoleTextArea);
        
        // Add in text area.
        add(outerScrollPane, BorderLayout.CENTER);
        
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
        String message = String.format(consoleTextArea.getText() + format + "\n", args);
        consoleTextArea.setText(message);
    }

   }
