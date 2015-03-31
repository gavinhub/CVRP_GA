/*
 * 遗传算法解决CVRP问题 - 路径可视化 - 人工智能作业
 * @author: 葛明洋，Gavin
 * email: gmy.gavin@gmail.com
 */
public class GAlgorithm extends Thread{
	public GAlgorithm(Performer p, DataBase da) {
		per = p;
		db = da;
		keepRun = true;
		genCount = 0;
	}
	
	/**
	 * 运行遗传算法
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		int points = db.getSize();		// 获取城市数量
		int[] ge = new int[points * 2];									//初始个体 1
		int[] ge2 = new int[points/2 + points + ((points % 2)==0?0:1)]; //初始个体 2
		/* 个体1： 每个点作为单一线路， 形如： 0,1,0,2,0,3,... */
		for(int i = 0; i < ge.length; i += 2) {
			ge[i] = 0;
			ge[i+1] = i / 2 + 1;
		}
		/* 个体2： 每两个点一条线路，形如：0,1,2,0,3,4,0,5,... */
		ge2[0] = 0;
		int j = 1;
		int fl = 1;
		while(fl < ge2.length) {
			ge2[fl] = j;
			fl ++;
			j ++;
			if(fl < ge2.length) {
				ge2[fl] = j;
				fl ++;
				j ++;
			}
			if(fl < ge2.length) {
				ge2[fl] = 0;
				fl ++;
			}
		}
	
		Route rond1 = new Route(ge, db);
		Route rond2 = new Route(ge2, db);
		curr = new Generation(db);
		curr.add(rond1);
		curr.add(rond2);  					// 第一代祖先只有两个个体，亚当和夏娃 ^_^
		
		genCount = 0;
		while(keepRun) {
			if(genCount % 10 == 0) {		// 每10代输出一次数据
				Route mat = curr.getBestRoute();
				//Route sec = curr.getBestRoute(1);
				best = curr.getBest();
				ave = curr.getAver();
				lineNumber = mat.getRouteNumber();
				maxLoad = mat.getLoad();
				per.drowRoute(mat, ave, status());
			}
			curr = curr.nextGeneration(); 	// 迭代
			genCount ++;
		}
		
	}
	
	public String[] status() {
		String s[] = new String[5];
		s[0] = "Generation: " + genCount ;
		s[1] = "Shortest: " + String.format("%.2f", best) ;
		s[2] = "Average: " + String.format("%.2f", ave);
		s[3] = "Car number:" + lineNumber;
		s[4] = "Load: " + maxLoad;
		return s;
	}
	public void setRunFlag(boolean b) {
		keepRun = b;
	}
	
	private Generation curr;
	private Performer per;
	private DataBase db;
	private boolean keepRun;
	private long genCount;
	private double best;
	private double ave;
	private int lineNumber;
	private int maxLoad;
}
