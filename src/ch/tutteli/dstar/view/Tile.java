/*
 * Copyright 2012 Robert Stoll <rstoll@tutteli.ch>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package ch.tutteli.dstar.view;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class Tile extends JPanel
{

    private JLabel top = new JLabel("0");
    private JLabel bottom = new JLabel("0");
    private JLabel left = new JLabel("0");
    private JLabel right = new JLabel("0");
    private JLabel centre = new JLabel("0");
    

    public Tile() {
        init();
    }

    public void init() {
        top.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bottom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        left.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        right.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        centre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        setLayout(new BorderLayout());

        add(top, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);
        add(left, BorderLayout.EAST);
        add(right, BorderLayout.WEST);
        add(centre, BorderLayout.CENTER);

    }
}
