/*
 * �Ŵ��㷨���CVRP���� - ·�����ӻ� - �˹�������ҵ
 * @author: ������Gavin
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

