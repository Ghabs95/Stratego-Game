package gj.stratego.player.pratesi2;

public class PieceActions {

	public int[] pieceMove(PieceType p) {
		String colour = PratesiPlayer2.isFirst ? "R" : "B";
		int[] currXY = PratesiPlayer2.xy[p.getValue()];
		Position path = Pursue.getPath(PratesiPlayer2.board, p.toString() + colour, "OPP");

		if (path != null) {
			PratesiPlayer2.myDesiredCell = path.toArray();

			int direction = pathToDirection(path, currXY);
			int steps = 1;
			PratesiPlayer2.myLastMove = PratesiPlayer2.xy[p.getValue()];
			return new int[] { currXY[0], currXY[1], direction, steps };

		}
		return null;
	}

	public int pathToDirection(Position p, int[] start) {
		int direction = 0;
		if (p.row - start[0] <= -1) {
			direction = PratesiPlayer2.isFirst ? PratesiPlayer2.DOWN : PratesiPlayer2.UP;
		} else if (p.row - start[0] >= 1) {
			direction = PratesiPlayer2.isFirst ? PratesiPlayer2.UP : PratesiPlayer2.DOWN;
		} else if (p.col - start[1] <= -1) {
			direction = PratesiPlayer2.isFirst ? PratesiPlayer2.RIGHT : PratesiPlayer2.LEFT;
		} else if (p.col - start[1] >= 1) {
			direction = PratesiPlayer2.isFirst ? PratesiPlayer2.LEFT : PratesiPlayer2.RIGHT;
		} 
		return direction;
	}

	public String toString(PieceType p) {
		String[] pieces = { "FL", "FB", "SB", "MA", "GE", "FM", "SM", "FS", "SS", "SP" };
		return pieces[p.getValue()];
	}

}
