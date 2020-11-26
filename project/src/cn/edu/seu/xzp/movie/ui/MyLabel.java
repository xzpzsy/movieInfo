package cn.edu.seu.xzp.movie.ui;

import javax.swing.*;
import java.awt.*;

public class MyLabel extends JLabel {
    public MyLabel(String content, Font font,Dimension dimension, Color color){
        super(content);
        this.setPreferredSize(dimension);
        this.setFont(font);
        // searchNameJTextFiled.setBorder(null);
        this.setHorizontalAlignment(0);
        this.setForeground(color);
    }
}
