package stratego.clear.engine;

//Referenced classes of package gj.stratego.engine:
//         Cell, Direction

public class Move {

	private Cell cell;
	private Direction direction;
	private int steps;

	protected Move(int move[]) {
		cell = new Cell(move);
		direction = new Direction(move[2]);
		steps = move[3];
	}

	protected Cell getCell() {
		return cell;
	}

	protected Direction getDir() {
		return direction;
	}

	protected int getSteps() {
		return steps;
	}

	public String toString() {
		return (new StringBuilder("[")).append(cell.toString()).append(";").append(direction.toString()).append(";")
				.append(steps).append("]").toString();
	}
}
