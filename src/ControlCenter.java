/*
 * 遗传算法解决CVRP问题 - 路径可视化 - 人工智能作业
 * @author: 葛明洋，Gavin
 * email: gmy.gavin@gmail.com
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ControlCenter extends JPanel {

	public ControlCenter(Performer p) {
		per = p;
		listener = new ActionHandler(this);
		init();
	}
	
	public ControlCenter() {
		init();
	}
	
	protected void init() {
		startBtn = new JButton("New Start");
		startBtn.addActionListener(listener);
		
		stopBtn = new JButton("Stop");
		stopBtn.addActionListener(listener);
		
		this.setLayout(new FlowLayout());
		this.add(startBtn);
		this.add(stopBtn);
	}
	
	public void startAlgo() {
		per.run();
	}
	
	public void stopAlog() {
		per.stopGA();
	}
	
	public static void main(String[] args) {
		JFrame fr = new JFrame("Settings");
		ControlCenter set = new ControlCenter();
		fr.addWindowListener(new WindowHandler(fr));
		fr.setContentPane(set);
		fr.setSize(600, 200);
		fr.validate();
		fr.setVisible(true);
	}
	
	private Performer per;
	private JButton startBtn, stopBtn;
	private ActionHandler listener;
}

class ActionHandler implements ActionListener {
	public ActionHandler(ControlCenter p) {
		se = p;
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		if( evt.getActionCommand().equals("New Start")){
			se.startAlgo();
		} else if( evt.getActionCommand().equals("Stop")) {
			se.stopAlog();
		} else {
			System.out.println(evt.getActionCommand());
		}
		
	}
	ControlCenter se;
}
