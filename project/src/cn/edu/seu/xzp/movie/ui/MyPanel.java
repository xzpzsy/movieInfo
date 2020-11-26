package cn.edu.seu.xzp.movie.ui;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    public MyPanel(Dimension d, int h, int v)
    {
        this.setPreferredSize(new Dimension(d));
        this.setLayout(new FlowLayout(FlowLayout.LEFT, h, v));
        this.setOpaque(false);
    }
}
