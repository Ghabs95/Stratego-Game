package gj.stratego.player.pratesi;

public class PieceActions {

	public int[] pieceMove(PieceType p) {
		String colour = PratesiPlayer.isFirst ? "R" : "B";
		int[] currXY = PratesiPlayer.xy[p.getValue()];
		Position path = Pursue.getPath(PratesiPlayer.board, p.toString() + colour, "OPP");

		if (path != null) {
			PratesiPlayer.myDesiredCell = path.toArray();

			int direction = pathToDirection(path, currXY);
			int steps = 1;
			PratesiPlayer.myLastMove = PratesiPlayer.xy[p.getValue()];
			return new int[] { currXY[0], currXY[1], direction, steps };

		}
		return null;
	}

	public int pathToDirection(Position p, int[] start) {
		int direction = 0;
		if (p.row - start[0] < 0) {
			direction = PratesiPlayer.isFirst ? PratesiPlayer.DOWN : PratesiPlayer.UP;
		} else if (p.row - start[0] > 0) {
			direction = PratesiPlayer.isFirst ? PratesiPlayer.UP : PratesiPlayer.DOWN;
		} else if (p.col - start[1] < 0) {
			direction = PratesiPlayer.isFirst ? PratesiPlayer.RIGHT : PratesiPlayer.LEFT;
		} else if (p.col - start[1] > 0) {
			direction = PratesiPlayer.isFirst ? PratesiPlayer.LEFT : PratesiPlayer.RIGHT;
		} 
		return direction;
	}
}
