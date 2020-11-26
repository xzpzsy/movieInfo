package cn.edu.seu.xzp.movie.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MyButton extends JButton {
    public MyButton(String content, Font font, Dimension dimension, ActionListener actionListener){
        this.setFont(font);
        this.setOpaque(true);
        this.setFocusPainted(false);
        //this.setBorderPainted(false);
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        // this.setContentAreaFilled(false);
        this.setForeground(new Color(238, 107, 99));
        this.setBackground(new Color(91, 157, 171));
        // this.setUI(new MyButtonUI());
        this.setText(content);
        this.setPreferredSize(dimension);
        this.addActionListener(actionListener);
    }
}
