/*
 * �Ŵ��㷨���CVRP���� - ·�����ӻ� - �˹�������ҵ
 * @author: ������Gavin
 * email: gmy.gavin@gmail.com
 */
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ChartBoard extends JPanel {
	public ChartBoard(double ran) {
		best = new ArrayList<Double>();
		ave = new ArrayList<Double>();
		range = ran;
	}
	
	
	public void paintComponent(Graphics gr) {
		g = gr;
		setSize();
		g.clearRect(0, 0, width, height);
		drawChart(ave, Color.black);
		drawChart(best,Color.red);
	}
	
	public void addBest(Double x) {
		if(best.size() < vetex)
			best.add(0,x);
		else {
			best.add(0,x);
			best.remove(best.size() - 1);
		}
		repaint();
		ancor ++;
		ancor = ancor % cross;
	}
	public void addAve(Double x) {
		if(ave.size() < vetex)
			ave.add(0,x);
		else {
			ave.add(0,x);
			ave.remove(ave.size() - 1);
		}
	}
	
	public void clear() {
		best.clear();
		ave.clear();
	}
	
	private synchronized void drawChart(ArrayList<Double> z, Color c) {
		// the grid
		g.setColor(Color.gray);
		int floor = height / 5;
		for(int f = 1; f < 5; f ++) {
			g.drawLine(0, floor * f, width, f * floor);
		}
		
		if (z.size() == 0 || z == null) return;
		g.setColor(c);
		int px = 0;
		int py = (int)(z.get(0) / range * height);
		for(int i = 0; i < z.size(); i ++) {
			int x = (int)((1.0 * i / vetex) * width);
			int y = (int)(z.get(i) / range * height);
			// grid
			g.setColor(Color.gray);
			if((i - ancor) % cross == 0) {
				g.drawLine(x, 0, x, height);
			}
			// line
			g.setColor(c);
			g.drawLine(px, py, x, y);
			px = x;
			py = y;
		}
		//System.out.println("Draw chart");
	}
	
	private void setSize() { // ��ȡ�����С
		width = getWidth();
		height = getHeight();
	}
	
	private int width, height;
	private ArrayList<Double> best;
	private ArrayList<Double> ave;
	private Graphics g;
	private static final int vetex = 1000;
	private final double range;
	private int ancor;
	private static final int cross = 300;
}
