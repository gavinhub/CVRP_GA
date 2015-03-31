/*
 * 遗传算法解决CVRP问题 - 路径可视化 - 人工智能作业
 * @author: 葛明洋，Gavin
 * email: gmy.gavin@gmail.com
 */
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class WindowHandler extends WindowAdapter {
	public WindowHandler(JFrame f) {
		fr = f;
	}
	
	public void windowClosing(WindowEvent e) {
		fr.setVisible(false);
		fr.dispose();
		System.exit(0);
	}
	
	private JFrame fr;
}

