/*
 * 遗传算法解决CVRP问题 - 路径可视化 - 人工智能作业
 * @author: 葛明洋，Gavin
 * email: gmy.gavin@gmail.com
 */
import java.io.*;
import java.util.*;

public class DataBase {
	public DataBase(File datafile) {
		df = datafile;
		try {
			Scanner sc = new Scanner(df);
			size = sc.nextInt();
			x = new int[size + 1];
			y = new int[size + 1];
			com = new int[size + 1];
			bestSolution = sc.nextDouble();
			capacity = sc.nextInt();
			x[0] = sc.nextInt();
			y[0] = sc.nextInt();
			com[0] = 0;
			sc.nextInt();
			x[1] = minX = maxX = sc.nextInt();
			y[1] = minY = maxY = sc.nextInt();
			com[1] = sc.nextInt();
			for(int i = 2; i <= size; i ++) {
				int t = sc.nextInt();
				x[t] = sc.nextInt();
				y[t] = sc.nextInt();
				com[t] = sc.nextInt();
				minX = minX < x[t] ? minX:x[t];
				maxX = maxX > x[t] ? maxX:x[t];
				minY = minY < y[t] ? minY:y[t];
				maxY = maxY > y[t] ? maxY:y[t];
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getX(int i) {return x[i];}
	public int getY(int i) {return y[i];}
	public int getCom(int i) {return com[i];}
	
	public int getMinX() {return minX;}
	public int getMaxX() {return maxX;}
	public int getMinY() {return minY;}
	public int getMaxY() {return maxY;}
	
	public int getSize() {return size;}
	public double getBestSolution() {return bestSolution;}
	public int getCapacity() {return capacity;}
	
	private int[] x;
	private int[] y;
	private int[] com;
	private File df;
	private int size;
	private double bestSolution;
	private int capacity;
	
	private int minX, maxX, minY, maxY;
}
