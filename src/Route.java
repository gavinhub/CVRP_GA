/*
 * 遗传算法解决CVRP问题 - 路径可视化 - 人工智能作业
 * @author: 葛明洋，Gavin
 * email: gmy.gavin@gmail.com
 */
import java.io.File;
import java.util.ArrayList;


public class Route implements Comparable<Route> {
	public Route(int[] ge, DataBase database) {
		db = database;
		gene = new ArrayList<Integer>();
		for (int i = 0; i < ge.length; i ++) {
			gene.add(ge[i]);
		}
		routeLength = 0;
		valid = false;
		make();
	}
	
	public Route(ArrayList<Integer> ge, DataBase database) {
		db = database;
		gene = ge;
		routeLength = 0;
		valid = false;
		make();
		//if(!valid) System.out.println("Fatial wrong.");
	}
	
	public Route(Route r) {
		db = r.db;
		routeLength = r.routeLength;
		valid = r.valid;
		routeNumber = r.routeNumber;
		maxLoad = r.maxLoad;
		gene = new ArrayList<Integer>(r.gene);
	}
	
	public void mutate() {
		int x, y;
		x = (int)Math.floor(Math.random() * gene.size());
		y = (int)Math.floor(Math.random() * gene.size());
		int t = gene.get(x);
		gene.set(x, gene.get(y));
		gene.set(y, t);
		make();
	}
	public void split() {
		if(Math.random() < 0.5) {
			int x = (int)Math.floor(Math.random() * gene.size());
			gene.add(x, 0);
		} else {
			int x = (int)Math.floor(Math.random() * (getRouteNumber() - 1)) + 1;
			int t = 1;
			for(int i = 1; i < gene.size(); i ++) {
				if(gene.get(i) == 0) {
					t ++;
					if( t == x) {
						gene.remove(i);
						break;
					}
				}
			}
		}
		make();
	}
	
	public void crossOver(Route r) {
		int rn, tn, min;
		rn = r.getRouteNumber();
		tn = this.getRouteNumber();
		min = rn < tn ? rn : tn;
		int cross = (int)Math.floor(Math.random() * min);
		int rind = 0;
		int tind = 0;
		rn = tn = -1;
		// 定位第 cross个起点在基因上的位置
		for(int i = 0; i < gene.size(); i ++) {
			if(gene.get(i) == 0) {
				tn ++;
				if(tn == cross) {
					tind = i;
					break;
				}
			}
		}
		for(int i = 0; i < r.gene.size(); i ++) {
			if(r.gene.get(i) == 0) {
				rn ++;
				if(rn == cross) {
					rind = i;
					break;
				}
			}
		}
		// 计算交换长度
		int range = 0;
		for(int i = 1; i<(gene.size()-tind) && i<(r.gene.size()-rind); i ++) {
			if(gene.get(tind + i) != 0 && r.gene.get(rind + i) != 0) {
				range ++;
			} else {
				break;
			}
		}
		int[] seri = new int[range];
		boolean[] che = new boolean[range];
		boolean[] rche = new boolean[range];
		for(int i = 0; i < range; i ++) {
			seri[i] = 0;
			che[i] = false;
			rche[i] = false;
		}
		ArrayList<Integer> left = new ArrayList<Integer>();
		for(int i = 1; i <= range; i ++) {
			int po = inRange(r.gene.get(rind + i), tind+1, tind+range); 
			if( po != -1) {
				seri[i - 1] = r.gene.get(rind + i);
				rche[i - 1] = true;
				che[po] = true;
			}
		}
		for(int i = tind + 1; i <= tind + range; i ++) {
			if(!che[i - tind - 1]) left.add(gene.get(i));
		}
		for(int i = 0; i < range; i ++) {
			if(!rche[i]) {
				seri[i] = left.remove(0);
			}
		}
		for(int i = 0; i < range; i ++) {
			gene.set(tind + 1 + i, seri[i]);
		}
		make();
	}
	
	private int inRange(int f, int l, int h) {
		int x = 0;
		for(int i = l; i <= h; i ++ ) {
			if(gene.get(i) == f) return x;
			x ++;
		}
		return -1;
	}
	
	public boolean checkValid() {
		boolean iszero = false;
		if(gene.get(0) != 0) {
			valid = false;
			return false;
		}
		for (int i = 0; i < gene.size(); i ++) {
			if(gene.get(i) == 0) {
				if (iszero) {
					valid = false;
					return valid;
				}
				iszero = true;
			} else {
				iszero = false;
			}
		}
		valid = (gene.get(gene.size() - 1) != 0);
		return valid;
	}
	
	public boolean isValid() { return valid;}
	
	/**
	 * 计算路经长， 检查合法性
	 */
	public void make() { 
		if (! checkValid()) return ;
		double res = 0;
		double tmp = 0;
		int pri = 0;
		maxLoad = 0;
		int tl = 0;
		for (int i = 0; i < gene.size(); i ++) {
			if (gene.get(i) == 0) { 				/* 遇到 0 */
				// 有关距离
				tmp += distance(0, pri);
				res += tmp;
				pri = 0;
				tmp = 0;
				// 有关载重
				if(maxLoad < tl) maxLoad = tl;
				tl = 0;
			} else if( i == gene.size() - 1) {  	/* 结尾 */
				// 有关距离
				tmp += distance(gene.get(i), pri);
				tmp += distance(0, gene.get(i));
				res += tmp;
				tmp = 0;
				pri = 0;
				// 有关载重
				tl += db.getCom(gene.get(i));
				if(maxLoad < tl) maxLoad = tl;
				tl = 0;
			} else { 								/* 遇到非0 */
				// 有关距离
				tmp += distance(gene.get(i), pri);
				pri = gene.get(i);
				// 有关载重
				tl += db.getCom(gene.get(i));
			}
		}
		routeLength = res;
		routeNumber = getRouteNumber(true);
		// 关于载重的检查在 shouldLive 中实现。
	}
	
	public double getLength() { return routeLength;}
	
	public int getSize() {return gene.size();}
	
	public int getLoad() {return maxLoad; }
	
	public int getRouteNumber(boolean first) {
		if(first) {
			int c = 0;
			for (int i = 0; i < gene.size(); i ++) {
				if(gene.get(i) == 0) c ++;
			}
			return c;
		} else {
			return getRouteNumber();
		}
	}
	public int getRouteNumber() {return routeNumber;}
	public boolean shouldLive() {
		if(isValid() && maxLoad <= db.getCapacity()) return true;
		return false;
	}
	public double adaptation() {
		return (1.0 /getLength());
	}
	public String[] routeStrings() {
		String[] x = new String[routeNumber];
		int f = -1;
		for (int i = 0; i < gene.size(); i ++) {
			if(gene.get(i) == 0) {
				f ++;
				x[f] = gene.get(i+1) + "";
				i ++;
			} else {
				x[f] = x[f] + "-" + gene.get(i);
			}
		}
		return x;
	}
	
	private double distance(int a, int b) {
		double z = 0;
		try {
			if ( a == b) return 0;
			double dis = ((db.getX(a)-db.getX(b)) * (db.getX(a)-db.getX(b)) 
					+ (db.getY(a)-db.getY(b)) * (db.getY(a)-db.getY(b)));
			dis = Math.sqrt(dis);
			z = dis;
		} catch (NullPointerException e) {
			
		}
		return z;
	}
	
	public ArrayList<Integer> gene;
	private DataBase db;
	private double routeLength;
	private boolean valid;
	private int routeNumber;
	private int maxLoad;
	
	public String toString() {
		String show = "Gene is: ";
		for(int i = 0; i < gene.size(); i ++) {
			show += gene.get(i) + " ";
		}
		show += "\nRouteNumber: " + routeNumber;
		show += "\nSize: " + gene.size() + "\tMaxLoad: " + maxLoad;
		show += "\nLength: " + routeLength;
		return show;
	}
	
	public static void main(String[] args) {
		int[] gene 	= 	{0,2,3,1,5,0,6,8,7,9,0,4};
		int[] gene2 = 	{0,1,2,0,6,9};
		File f = new File("tai75a.dat");
		DataBase db = new DataBase(f);
		Route x = new Route(gene2, db);
		Route y = new Route(gene, db);
		
		x.mutate();
		y.split();
		
		if(x.isValid()) 
			System.out.println(x);
		if(y.isValid())
			System.out.println(y);
		
		y.crossOver(x);
	
	}

	@Override
	public int compareTo(Route obj) {
		if(this.getLength() < obj.getLength()) return -1;
		if(this.getLength() > obj.getLength()) return 1;
		return 0;
	}
}
