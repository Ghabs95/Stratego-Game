package gj.stratego.player.pratesi;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Pursue {
	private final static boolean isFirst = PratesiPlayer.isFirst;
	private final static int SIZE = PratesiPlayer.BOARD_SIZE;
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
			if (grid[p1.row][p1.col] < grid[p2.row][p2.col]) {
				return -1;
			} else if (grid[p1.row][p1.col] > grid[p2.row][p2.col]) {
				return 1;
			} else {
				return 0;
			}
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

				if (!isNull(arr, neighbour) && isReached(arr, neighbour, opponentPiece)) {
					return backwards(grid, arr, current, opponentPiece);
				}
			}
		}
		return null;
	}

	private static Position backwards(int[][] grid, String arr[][], Position current, String opponentPiece) {
		int num = grid[current.row][current.col];
		Stack<Integer> list = fillStack(new Stack<>(), num);
		while (num > 1) {
			current = getNearest(current, p -> gridIsEqToInt(grid, p, list.peek()));
			list.pop();
			num--;
		}
		if (num == 0) {
			current = getNearest(current, p -> isReached(arr, p, opponentPiece));
		}
		return current;
	}

	private static Position getNearest(Position current, Predicate<Position> selector) {
		List<Position> neighbours = getNeighbour(current);
		return neighbours.stream()
				.filter(selector)
				.findFirst()
				.get();
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

	private static boolean isMyPiece(String[][] arr, Position p, PieceType e) {
		return arr[p.row][p.col].equals(e.toString() + (isFirst ? "R" : "B"));
	}

	private static boolean isReached(String[][] arr, Position p, String opponentPiece) {
		return !isNull(arr, p) && arr[p.row][p.col].equals(opponentPiece);
	}

	private static boolean isMine(String[][] arr, Position p) {
		return Arrays.stream(PieceType.values())
				.anyMatch(e -> !isNull(arr, p) && isMyPiece(arr, p, e));
	}

}
