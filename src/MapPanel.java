/*
 * 遗传算法解决CVRP问题 - 路径可视化 - 人工智能作业
 * @author: 葛明洋，Gavin
 * email: gmy.gavin@gmail.com
 */
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MapPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MapPanel(DataBase d) {
		db = d;
		points = new ArrayList<Point>();
		route = null;
		subroute = null;
		str = null;
		init();
	}
	/**
	 * 设置画布、生成点
	 * 生成颜色列表
	 */
	private void init() {
		int padding = 5;
		minX = db.getMinX() - padding;
		minY = db.getMinY() - padding;
		maxX = db.getMaxX() + padding;
		maxY = db.getMaxY() + padding;
		int size = db.getSize() + 1;
		//points.add(new Point(0, 0));
		for(int i = 0; i < size; i ++) {
			Point t = new Point(db.getX(i), db.getY(i));
			points.add(t);
		}
		points.get(0).setCol(Color.green);
		
		clist = new Color[size];
		for(int i = 0; i < size; i ++) 
			clist[i] = randomColor();
	}
	
	public void paintComponent(Graphics gra){
		g = gra;
		setSize();
		g.clearRect(0, 0, width, height); // 清空区域，结果为白色
		//drawAxis();
		//drawRoute(true);
		drawRoute(false);
		paintPoints();
		paintStatus();
	}
	
	public void paintStatus() {
		if(str == null) return; 
		g.setColor(Color.black);
		for(int i = 0; i < str.length; i ++) {
			g.drawString(str[i], 10, (i+1) * 20);
		}
	}
	
	public void paintPoints() {
		for(int i = 0; i < points.size(); i ++) {
			drawPoint(points.get(i));
		}
	}
	
	private synchronized void drawRoute(boolean sub) {
		ArrayList<Integer> theroute = sub?subroute:route;
		if(theroute == null) return;
		else {
			Point pre = points.get(theroute.get(0));
			int rorder = 0;
			g.setColor(clist[0]);
			if(sub) {g.setColor(new Color(0.8f, 0.8f, 0.8f));}
			for(int i = 1; i < theroute.size(); i ++) {
				/* set color */
				Point cur = points.get(theroute.get(i));
				drawEdge(pre, cur);
				pre = points.get(theroute.get(i));
				if(theroute.get(i) == 0 || i == theroute.size() - 1) {
					rorder ++;
					drawEdge(pre, points.get(theroute.get(0)));
					if(!sub)g.setColor(clist[rorder]);
				}
			}
		}
	}
	
	private void drawPoint(Point p) {
		if(p == null) return;
		int radius = 3;
		g.setColor(p.col());
		g.drawOval(p.x()-radius, p.y()-radius, 2*radius, 2*radius);
		g.fillOval(p.x()-radius, p.y()-radius, 2*radius, 2*radius);
	}
	
	private void drawEdge(Point a, Point b) {
		g.drawLine(a.x(), a.y(), b.x(), b.y());
	}
	
	protected Color randomColor() {
		float r,g,b;
		r = (float)Math.random();
		b = (float)Math.random();
		g = (float)Math.random();
		Color c = new Color(r, g, b);
		return c;
	}
	
	protected void drawAxis() {
		g.setColor(Color.green);
		g.drawLine(points.get(0).x(), 0, points.get(0).x(), height);
		g.drawLine(0, points.get(0).y(), width, points.get(0).y());
	}
	
	public void clear() {
		route = null;
	}
	
	public void setRoute(ArrayList<Integer> z) {
		route = z;
	}
	public void setSubRoute(ArrayList<Integer> z) {
		subroute = z;
	}
	public void setStatus(String s[]) {
		str = s;
	}
	
	private void setSize() { // 获取组件大小
		width = getWidth();
		height = getHeight();
	}
	
	private int minX, maxX, minY, maxY;
	private int width, height;
	private Graphics g;
	private DataBase db;
	private ArrayList<Integer> route, subroute;
	private ArrayList<Point> points;				// 点集
	private Color[] clist;
	private String str[];
 	
	public static void main(String[] args) {
		JFrame fr = new JFrame("ScatterPanel");
		File f = new File("taillard/tai75a.dat");
		DataBase db = new DataBase(f);
		MapPanel mp = new MapPanel(db);
		
		fr.addWindowListener(new WindowHandler(fr));
		fr.setContentPane(mp);
		fr.setSize(600, 600);
		fr.validate();
		fr.setVisible(true);
		
		int [] ge = {0, 1, 0,2, 3,0, 15, 0,5, 6, 0,7, 8, 9, 0,10,
				11, 0, 12, 13, 0,14, 4, 16, 0,17, 18, 0,19, 20,
				21, 22, 23, 0,24, 25, 26, 0,27, 28, 29, 30, 
				31, 32, 0, 33, 34, 35, 36, 37, 38, 39, 40,
				41,0, 42, 43, 0,44, 45 ,46, 0, 47, 48, 49, 50,
				51, 0, 52, 53, 0,54, 55, 56, 0, 57, 0,58, 59, 60,
				61, 0, 62, 63, 64, 65, 0,66, 67, 0, 68, 69, 70, 
				71, 0,72, 73, 0, 74, 75};
		
		Route rt = new Route(ge, db);
		mp.setRoute(rt.gene);
	}
	
	private class Point{
		public Point(double x, double y) {
			this(x,y,Color.black);
		}
		
		public Point(double x, double y, Color c) {
			dX = x;
			dY = y;
			col = c;
		}
		
		public int x() {
			return (int) Math.round((dX - minX) / (maxX - minX) * width);
		}
		public int y() {
			return height - (int) Math.round((dY - minY) / (maxY - minY) * height);
		}
		public Color col() { return col;}
		
		public void setCol(Color co) {col = co;}
		
		private double dX, dY;
		private Color col;
	}
}
