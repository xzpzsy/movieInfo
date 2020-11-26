package cn.edu.seu.xzp.movie.ui;

import javax.swing.*;
import java.awt.*;

public class MyJScrollPane extends JScrollPane {
    public MyJScrollPane(Component component,Dimension dimension){
        super(component);
        this.getVerticalScrollBar().setUI(new DemoScrollBarUI());
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setPreferredSize(dimension);
        this.setBorder(null);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
    }
}
