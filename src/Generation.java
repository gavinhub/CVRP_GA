/*
 * 遗传算法解决CVRP问题 - 路径可视化 - 人工智能作业
 * @author: 葛明洋，Gavin
 * email: gmy.gavin@gmail.com
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class Generation {
	public static final int ENV_SUPPORT = 350;
	public static final int MAX_LOAD = 450;
	
	public Generation(DataBase database) {
		pool = new ArrayList<Route>();
		db = database;
	}
	public Generation(ArrayList<Route> p, DataBase database) {
		pool = p;
		ta = 0;
		for(int i = 0; i < pool.size(); i ++) {
			ta += pool.get(i).adaptation();
		}
		db = database;
	}
	
	public boolean add(Route r) {
		if(pool.size() < ENV_SUPPORT) {
			pool.add(r);
			ta += r.adaptation();
			return true;
		} else {
			return false;
		}
	}
	private void sort() {
		Collections.sort(pool);
	}
	
	/**
	 * 计算  最优值 及 均值
	 */
	private void evaluate() {
		sort();
		double bbest = 0;
		averiage = 0;
		for(int i = 0; i < pool.size(); i ++) {
			double ad;
			if ( (ad = pool.get(i).adaptation()) > bbest) {
				bbest = ad;
				//System.out.println("bbest update: " + bbest);
			}
			averiage += 1 / pool.get(i).adaptation();
		}
		averiage /= pool.size();
		best = 1 / bbest;
	}

	private int pick() {
		double tmp = 0;
		double flag;
		double switcher = Math.random();
		if(switcher < 0.7) {
			flag = Math.random() * ta * 0.5;
		} else {
			flag = (Math.random() + 1) * ta * 0.5;
		}
		for(int i = 0; i < pool.size(); i ++) {
			if (tmp < flag) {
				tmp += pool.get(i).adaptation();
			} else {
				return i;
			}
		}
		return pool.size() -1;
	}
	private void generate() {
		if(!isSorted()) sort();
		
		for(int i = 0; i < MAX_LOAD; i ++) {
			int choice = pick();
			double p = Math.random();
			if(p < 0.4) {
				int tar = pick();
				Route tmp = new Route(pool.get(choice));
				tmp.crossOver(pool.get(tar));
				next.add(tmp);
				//System.out.println("Adding crossOver...");
			} else if ( p < 0.7 ) {
				Route tmp = new Route(pool.get(choice));
				tmp.mutate();
				next.add(tmp);
				//System.out.println("Adding mutate...");
			} else {
				Route tmp = new Route(pool.get(choice));
				tmp.split();
				next.add(tmp);
				//System.out.println("Adding split...");
			}
		}
	}
	private void kill() {
		int index = 0;
		while(index < next.size()) {
			if(!next.get(index).shouldLive()) {
				next.remove(index);
			} else {
				index ++;
			}
		}
	}
	
	private void putIn(Generation gen) {
		// 父代精英
		int index = 1;
		gen.add(pool.get(0));
		if(pool.size() > 1) {
			gen.add(pool.get(1));
			index += 1;
		}
		// 外星人
		int zeros = pool.get(0).getRouteNumber();
		int geSize = pool.get(0).getSize();
		
		for (int i = 0; i < ENV_SUPPORT * 0.1; i ++) {
			ArrayList<Integer> align = new ArrayList<Integer>();		
			
			for(int j = 0; j < zeros; j ++) {
				align.add(0);
			}
			for(int j = 0; j < geSize - zeros; j ++) {
				align.add(j+1);
			}
			Collections.shuffle(align);
			Route et = new Route(align, db);
			if(et.shouldLive())
				gen.add(et);
			//System.out.println("Add align");
			index ++;
		}
		//
		// 加入子代
		while (index < ENV_SUPPORT && index < next.size()) {
			gen.add(next.get(index));
			index ++;
		}
	}
	
	public double getAver() {return averiage;}
	public double getBest() {return best;}
	
	public Route getBestRoute() {
		return getBestRoute(0);
	}
	public Route getBestRoute(int index) {
		if(!isSorted()) sort();
		return pool.get(index);
	}
	
	public boolean isSorted() {
		double best = 0;
		for(int i = 0; i < pool.size(); i ++) {
			if(pool.get(i).adaptation() < best) {
				return false;
			} else {
				best = pool.get(i).adaptation();
			}
		}
		return true;
	}
	
	public Generation nextGeneration() {
		next = new ArrayList<Route>();
		generate();
		kill();
		Generation g = new Generation(db);
		putIn(g);
		g.evaluate();
		return g;
	}
	
	private ArrayList<Route> pool;
	private ArrayList<Route> next = null;
	private double ta = 0; // total adaptation
	private double best;
	private double averiage;
	private DataBase db;
	
	public static void main(String[] args) {
		int [] ge = {0, 1, 0,2, 3,0, 15, 0,5, 6, 0,7, 8, 9, 0,10,
				11, 0, 12, 13, 0,14, 4, 16, 0,17, 18, 0,19, 20,
				21, 22, 23, 0,24, 25, 26, 0,27, 28, 29, 30, 
				31, 32, 0, 33, 34, 35, 36, 37, 38, 39, 40,
				41,0, 42, 43, 0,44, 45 ,46, 0, 47, 48, 49, 50,
				51, 0, 52, 53, 0,54, 55, 56, 0, 57, 0,58, 59, 60,
				61, 0, 62, 63, 64, 65, 0,66, 67, 0, 68, 69, 70, 
				71, 0,72, 73, 0, 74, 75};
		
		int [] ge2 = {0, 1, 0,2, 3,0, 15, 0,5, 6, 0,7, 8, 9, 0,10,
				11, 0, 12, 13, 0,14, 4, 16, 0,17, 18, 0,19, 20,
				21, 22, 23, 0,24, 25, 26, 0,27, 28, 29, 30, 
				31, 32, 0, 33, 34, 35, 36, 37, 38, 39, 40,
				41,0, 42, 43,44, 45 ,46, 0, 47, 48, 49, 50,
				51, 0, 52, 53, 0,54, 55, 56, 57, 0,58, 59, 60,
				61, 62, 63, 64, 65, 0,66, 67, 68, 69, 70, 
				71, 0,72, 73, 74, 75};
		File f = new File("taillard/tai75a.dat");
		DataBase db = new DataBase(f);
		Route rond1 = new Route(ge, db);
		Route rond2 = new Route(ge2, db);
		Generation test = new Generation(db);
		test.add(rond1);
		test.add(rond2);
		test.display();
		
		for(int i =0 ;i < 5000; i ++) {
			test = test.nextGeneration();
			if(i % 1000 == 0) {
				System.out.println("Generation: " + i );
				test.display();
			}
		}
		test.sort();
		test.display();
		System.out.println(test.pool.get(0));
	}
	
	public void display() {
		System.out.println("Individials: " + pool.size());
		System.out.println("Best value: " + (int)best + " Ave: " + (int)averiage);
	}
}
