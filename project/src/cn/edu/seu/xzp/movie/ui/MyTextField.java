package cn.edu.seu.xzp.movie.ui;

import javax.swing.*;
import java.awt.*;

public class MyTextField extends JTextField {
    public MyTextField(Font font, Dimension dimension){
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        // this.setUI(new MyButtonUI());
        this.setPreferredSize(dimension);
        this.setFont(font);
        this.setBackground(new Color(91, 157, 171));
        this.setForeground(new Color(238, 107, 99));
    }
}
