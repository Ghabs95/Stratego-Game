package gj.stratego.player.pratesi2;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Pursue {
	private final static boolean isFirst = PratesiPlayer2.isFirst;
	private final static int SIZE = PratesiPlayer2.BOARD_SIZE;
	private final static String FORBIDDEN = " L ";

	public static List<Position> getNeighbour(Position p) {
		List<Position> neighbours = new ArrayList<Position>();
		Position posLeft = p.getLeft();
		if (posLeft.row >= 0 && posLeft.row < SIZE && posLeft.col >= 0 && posLeft.col < SIZE) {
			neighbours.add(posLeft);
		}
		Position posRight = p.getRight();
		if (posRight.row >= 0 && posRight.row < SIZE && posRight.col >= 0 && posRight.col < SIZE) {
			neighbours.add(posRight);
		}
		Position posUp = p.getUp();
		if (posUp.row >= 0 && posUp.row < SIZE && posUp.col >= 0 && posUp.col < SIZE) {
			neighbours.add(posUp);
		}
		Position posDown = p.getBottom();
		if (posDown.row >= 0 && posDown.row < SIZE && posDown.col >= 0 && posDown.col < SIZE) {
			neighbours.add(posDown);
		}
		return neighbours;
	}

	public static Position getPath(String[][] arr, String myPiece, String opponentPiece) {
		final int[][] grid = new int[SIZE][SIZE];
		PriorityQueue<Position> queue = new PriorityQueue<Position>(SIZE * SIZE, (p1, p2) -> {
			if (grid[p1.row][p1.col] < grid[p2.row][p2.col])
				return -1;
			else if (grid[p1.row][p1.col] > grid[p2.row][p2.col])
				return 1;
			else
				return 0;
		});

		initQueue(arr, myPiece, grid, queue);
		while (!queue.isEmpty()) {
			Position current = queue.poll();
			List<Position> neighbours = getNeighbour(current);

			for (Position neighbour : neighbours) {
				if (isNull(arr, neighbour) && !isForbidden(arr, neighbour) && !isMine(arr, neighbour)
						&& gridIsEqToInt(grid, neighbour, 0)) {
					grid[neighbour.row][neighbour.col] = grid[current.row][current.col] + 1;
					queue.add(neighbour);
				}

				if (!isNull(arr, neighbour) && arr[neighbour.row][neighbour.col].equals(opponentPiece)) {
				//	print(grid);
					//System.out.println(BoardPrinter.toString(arr));
					return backwards(grid, arr, current);
				}
			}
		}
		return null;
	}

//	private static void print(int[][] a) {
//		System.out.println();
//		for (int i = 0; i < a.length; i++)
//			System.out.println(Arrays.toString(a[i]));
//	}

	private static Position backwards(int[][] grid, String arr[][], Position current) {
		int num = grid[current.row][current.col];
		Stack<Integer> list = fillStack(new Stack<>(), num);
		while (num > 1) {
			List<Position> neighbours = getNeighbour(current);
			current = neighbours.stream()
					.filter(p -> gridIsEqToInt(grid, p, list.peek()))
					.findFirst()
					.get();
			list.pop();
			num--;
		}
		if (num == 0) {
			List<Position> neighbours = getNeighbour(current);
			current = neighbours.stream()
					.filter(p -> arr[p.row][p.col] == "OPP")
					.findFirst()
					.get();
		}
		return current;
	}

	private static Stack<Integer> fillStack(Stack<Integer> stack, int num) {
		Stream.iterate(0, f -> f + 1)
				.limit(num)
				.map(f -> stack.push(f))
				.collect(toList());
		return stack;
	}

	private static void initQueue(String[][] arr, String myPiece, final int[][] grid, PriorityQueue<Position> queue) {
		boolean found = false;
		for (int i = 0; i < SIZE && !found; i++) {
			for (int j = 0; j < SIZE && !found; j++) {
				if (arr[i][j] != null && arr[i][j].equals(myPiece)) {
					queue.add(new Position(i, j));
					grid[i][j] = 0;
					found = true;
				}
			}
		}
	}

	private static boolean isNull(String[][] arr, Position p) {
		return arr[p.row][p.col] == null;
	}

	private static boolean gridIsEqToInt(int[][] grid, Position p, int i) {
		return grid[p.row][p.col] == i;
	}

	private static boolean isForbidden(String[][] arr, Position p) {
		return !isNull(arr, p) && arr[p.row][p.col].equals(FORBIDDEN);
	}

	private static boolean isMine(String[][] arr, Position p) {
		for (PieceType e : PieceType.values()) {
			if (!isNull(arr, p) && arr[p.row][p.col].equals(e.toString() + (isFirst ? "R" : "B"))) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		Stack<Integer> list = fillStack(new Stack<>(), 5);
		System.out.println(list);
	}

}
