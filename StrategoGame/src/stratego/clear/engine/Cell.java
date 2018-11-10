package stratego.clear.engine;

import gj.stratego.exception.WrongCellException;
import gj.stratego.exception.WrongTargetCellException;

public class Cell {

	private final int row;
	private final int column;

	protected Cell(int coords[]) {
		row = coords[0];
		column = coords[1];
	}

	protected void checkRange(int upRow, int downRow, int leftColumn, int rightColumn) throws WrongCellException {
		if (row < upRow || row > downRow || column > rightColumn || column < leftColumn) {
			throw new WrongCellException();
		} else {
			return;
		}
	}

	protected boolean equals(Cell cell) {
		return row == cell.row && column == cell.column;
	}

	protected int getColumn() {
		return column;
	}

	protected int getRow() {
		return row;
	}

	protected Cell move(int dir, boolean isFirst) throws WrongTargetCellException {
		int current[] = { row, column };
		update(current, dir, isFirst);
		Cell target = new Cell(current);
		try {
			target.checkRange(0, 9, 0, 9);
		} catch (WrongCellException e) {
			throw new WrongTargetCellException();
		}
		return target;
	}

	public final String toString() {
		return (new StringBuilder("(")).append(row)
				.append(",")
				.append(column)
				.append(")")
				.toString();
	}

	protected void update(int coords[], int dir, boolean isFirst) {
		int deltaFirst[][] = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
		int deltaSecond[][] = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
		if (isFirst) {
			coords[0] = coords[0] + deltaFirst[dir - 1][0];
			coords[1] = coords[1] + deltaFirst[dir - 1][1];
		} else {
			coords[0] = coords[0] + deltaSecond[dir - 1][0];
			coords[1] = coords[1] + deltaSecond[dir - 1][1];
		}
	}
}
