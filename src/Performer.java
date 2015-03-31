/*
 * 遗传算法解决CVRP问题 - 路径可视化 - 人工智能作业
 * @author: 葛明洋，Gavin
 * email: gmy.gavin@gmail.com
 */
import java.awt.*;
import java.io.File;

import javax.swing.*;

public class Performer extends JFrame {
	/**
	 * I don't know what this is, but hate the devil little yellow triangle.
	 */
	private static final long serialVersionUID = 1L;

	public Performer(File f) {
		db = new DataBase(f);
		panel = new MapPanel(db);
		chart = new ChartBoard(db.getBestSolution() * 3);
		res = new Resault();
		gthread = null;
		this.setTitle("CVRP");
	}
	
	public void go() {
		this.setSize(920, 600);
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc;
		this.getContentPane().setLayout(layout);
		Container pane = this.getContentPane();
		
		/* 左侧 */
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.ipadx = 500;
		gbc.ipady = 500;
		gbc.insets = new Insets(0, 5, 0, 15);
		pane.add(panel, gbc);
		
		/* 右侧 */
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.ipadx = 200;
		gbc.ipady = 50;
		pane.add(new ControlCenter(this), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.ipadx = 200;
		gbc.ipady = 200;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JScrollPane jsp = new JScrollPane(res);
		pane.add(jsp, gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.ipady = 150;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		pane.add(chart, gbc);
		
		this.addWindowListener(new WindowHandler(this));
		this.setVisible(true);
	}
	
	public void run() {
		if(gthread != null) stopGA();
		chart.clear();
		gthread = new GAlgorithm(this, db);
		gthread.start();
	}
	
	public void drowRoute(Route r, double x, String[] str) {
		/* MapPanel */
		panel.clear();
		panel.setRoute(r.gene); 		// best route
		//panel.setSubRoute(x.gene);	// second best route
		panel.setStatus(str);
		panel.repaint();
		
		/* ChartBoard */
		chart.addBest(r.getLength());
		chart.addAve(x);
		
		/* Result */
		res.setStrings(r.routeStrings());
	}
	
	public void stopGA() {
		if(gthread != null) {
			gthread.setRunFlag(false);
		}
	}
	
	public static void main(String[] args) {
		String fn = "tc/tai75a.dat";
		if(args.length >= 1) {
			fn = args[0];
		}
		File f = new File(fn);
		if(!f.exists()) {
			System.out.println("File not found.");
			System.exit(1);
		}
		Performer p = new Performer(f);
		p.setTitle("CVRP - " +fn);
		p.go();
		//p.run();
	}
	
	private GAlgorithm gthread;
	private DataBase db;
	private MapPanel panel;
	private ChartBoard chart;
	private Resault res;
}
