package gj.stratego.player.console;

import java.util.Scanner;

import gj.stratego.player.Player;

public class ConsolePlayer implements Player {
	private boolean isFirst;
	private static final String[] PIECE_TYPE = { "FL", "FB", "SB", "MA", "GE", "FM", "SM", "FS", "SS", "SP" };
	private static final int[][] PIECE_POS_FIRST = { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 2, 2 }, { 2, 1 }, { 2, 0 },
			{ 0, 2 }, { 3, 0 }, { 1, 2 }, { 1, 1 } };
	private static final int[][] PIECE_POS_SECOND = { { 9, 0 }, { 8, 0 }, { 9, 1 }, { 7, 2 }, { 7, 1 }, { 7, 0 },
			{ 9, 2 }, { 6, 0 }, { 8, 2 }, { 8, 1 } };
	private Scanner sc = new Scanner(System.in);

	public void start(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public int[] position(String piece) {
		int i = 0;
		boolean found = false;
		while (!found) {
			if (PIECE_TYPE[i].equals(piece)) {
				found = true;
			} else {
				i = i + 1;
			}
		}
		int[] r = PIECE_POS_FIRST[i];
		if (!isFirst) {
			r = PIECE_POS_SECOND[i];
		}
		return r;
	}

	public void viewPositions(int[][] position) {
	}

	public int[] move() {
		System.out.print("Insert " + (isFirst ? "red" : "blue") + " move row: ");
		int row = sc.nextInt();
		System.out.print("Insert " + (isFirst ? "red" : "blue") + " move column: ");
		int col = sc.nextInt();
		System.out.print("Insert " + (isFirst ? "red" : "blue") + " move direction: ");
		int dir = sc.nextInt();
		System.out.print("Insert " + (isFirst ? "red" : "blue") + " move number of steps: ");
		int steps = sc.nextInt();
		return new int[] { row, col, dir, steps };
	}

	public void tellMove(int[] move) {
	}

	public void fight(String piece) {
	}
}
