/*
 * 遗传算法解决CVRP问题 - 路径可视化 - 人工智能作业
 * @author: 葛明洋，Gavin
 * email: gmy.gavin@gmail.com
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Resault extends JPanel {
	public Resault() {
		this.setPreferredSize(new Dimension(1000, 3000));
	}
	
	public void paintComponent(Graphics gr) {
		g = gr;
		setSize();
		g.clearRect(0, 0, width, height);
		drawLines();
	}
	
	private void setSize() { // 获取组件大小
		width = getWidth();
		height = getHeight();
	}
	
	public void setStrings(String [] s) {
		lines = s;
		repaint();
	}
	public void drawLines() {
		if(lines == null) return;
		for(int i = 0; i < lines.length; i ++) {
			String prefix = "Route " + String.valueOf(i+1);
			if(i+1 < 10) prefix = prefix + "  ";
			prefix = prefix + ": ";
			g.drawString(prefix + lines[i], 10, (i+1) * 15);
		}
	}
	
	private int width, height;
	private Graphics g;
	private String[] lines;
}
