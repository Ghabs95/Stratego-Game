package gj.stratego.player.pratesi2;

public class Position {
	public int row;
	public int col;

	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public Position getLeft() {
		return new Position(row, col - 1);
	}

	public Position getRight() {
		return new Position(row, col + 1);
	}

	public Position getBottom() {
		return new Position(row + 1, col);
	}

	public Position getUp() {
		return new Position(row - 1, col);
	}

	public int[] toArray() {
		return new int[] { row, col };
	}

	public String toString() {
		return "row:" + row + " col:" + col;
	}
}
