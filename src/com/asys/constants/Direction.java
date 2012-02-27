/**
 * 
 */
package com.asys.constants;

/**
 *
 */
public enum Direction {
	UP, DOWN, LEFT, RIGHT;
	
	public static Direction getOpposite(Direction dir){
		if (dir == UP) return DOWN;
		else if (dir == DOWN) return UP;
		else if (dir == LEFT) return RIGHT;
		else{
			assert dir == RIGHT;
			return LEFT;
		}
	}
	
	public static boolean isOrthogonal(Direction dir1, Direction dir2){
		if (dir1 == dir2 || dir1 == getOpposite(dir2)) return false;
		else return true;
	}
}
