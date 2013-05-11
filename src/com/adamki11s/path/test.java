package com.adamki11s.path;

import java.util.HashSet;

import com.adamki11s.pathing.Tile;

public class test {
	
	public static void main(String[] args){
		/*int range = 100000000; // 1000mil
		
		long time = -System.currentTimeMillis();
		
		int sx = 100, ex = 200, sy = -335, ey = 334, sz = -445, ez = -664;
		
		for(int i = -range; i < range; i++){
			getManhattanDistance(sx, sy, sz, ex, ey, ez);
		}
		
		time += System.currentTimeMillis();
		
		System.out.println("Time for manhattan = " + ( (double) time / 1000D) + "s");
		
		time = -System.currentTimeMillis();
		
		for(int i = -range; i < range; i++){
			getEuciledianDistance(sx, sy, sz, ex, ey, ez);
		}
		
		time += System.currentTimeMillis();
		
		System.out.println("Time for euciledian = " + ( (double) time / 1000D) + "s");*/
		
		HashSet<Tile> tiles = new HashSet<Tile>();
		
		Tile t1 = new Tile((short)1, (short)1, (short)1, null),
		t2 = new Tile((short)1, (short)1, (short)1, null),
		t3 = new Tile((short)3, (short)1, (short)1, null);
		
		//t1 and 2 are equal
		print("T1 hashcode = " + t1.hashCode());
		print("T2 hashcode = " + t2.hashCode());
		print("T3 hashcode = " + t3.hashCode());
		
		tiles.add(t1);
		tiles.add(t2);
		tiles.add(t3);
		tiles.add(t1);
		
		for(Tile t : tiles){
			print(t.getX() + ", " + t.getY() + ", " + t.getZ());
		}
		
	}
	
	private static long time;
	private static boolean logged = false;
	
	static void print(String message){
		System.out.println(message);
	}
	
	static void profile(String message){
		profile();
		System.out.println(message + " - " + time + "ms.");
	}
	
	static void profile(){
		if(!logged){
			time = -System.currentTimeMillis();
		} else {
			time += System.currentTimeMillis();
		}
		logged ^= true;
	}
	
	private static double getManhattanDistance(int sx, int sy, int sz, int ex, int ey, int ez) {
		double dx = sx - ex, dy = sy - ey, dz = sz - ez;
		dx = abs(dx);
		dy = abs(dy);
		dz = abs(dz);
		return (dx + dy + dz);
	}
	
	private static double getEuciledianDistance(int sx, int sy, int sz, int ex, int ey, int ez) {
		double dx = sx - ex, dy = sy - ey, dz = sz - ez;
		/*dx = abs(dx);
		dy = abs(dy);
		dz = abs(dz);*/
		return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
		//return (dx + dy + dz);
	}
	
	private static double abs(double i){
		return (i < 0 ? -i : i);
	}

}
