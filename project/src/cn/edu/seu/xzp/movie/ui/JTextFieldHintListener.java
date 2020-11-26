package cn.edu.seu.xzp.movie.ui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
 
import javax.swing.JTextField;
 
public class JTextFieldHintListener implements FocusListener {
	private String hintText;
	private JTextField textField;
	public JTextFieldHintListener(JTextField jTextField,String hintText) {
		this.textField = jTextField;
		this.hintText = hintText;
		jTextField.setText(hintText);  //默认直接显示
		jTextField.setForeground(new Color(237, 255, 0));
	}
 
	@Override
	public void focusGained(FocusEvent e) {
		//获取焦点时，清空提示内容
		String temp = textField.getText();
		if(temp.equals(hintText)) {
			textField.setText("");
			textField.setForeground(new Color(237, 255, 255));
		}
		
	}
 
	@Override
	public void focusLost(FocusEvent e) {	
		//失去焦点时，没有输入内容，显示提示内容
		String temp = textField.getText();
		if(temp.equals("")) {
			textField.setForeground(new Color(237, 255, 255));
			textField.setText(hintText);
		}
		
	}
 
}