package stratego.clear.engine;

//Referenced classes of package gj.stratego.engine:
//         Cell, Constants

public class Piece {

	private int piece;
	private boolean isFirst;
	Cell last[];

	protected Piece(int piece, boolean isFirst) {
		this.piece = piece;
		this.isFirst = isFirst;
		last = new Cell[3];
	}

	protected int compare(Piece tp) {
		int r = 0;
		if ((tp.getPiece() == 1 || tp.getPiece() == 2) && piece != 5 && piece != 6) {
			r = 1;
		} else if (piece == 9 && tp.getPiece() == 3) {
			r = 0;
		} else if (Constants.PIECE_VALUE[tp.getPiece()] > Constants.PIECE_VALUE[piece]) {
			r = 1;
		} else if (Constants.PIECE_VALUE[tp.getPiece()] == Constants.PIECE_VALUE[piece]) {
			r = 2;
		}
		return r;
	}

	protected final int getPiece() {
		return piece;
	}

	protected final boolean isFirst() {
		return isFirst;
	}

	protected int lastTimeVisited(Cell cell) {
		int i;
		for (i = 0; i < last.length && last[i] != null && !last[i].equals(cell); i++) {
		}
		if (i == last.length || last[i] == null) {
			i = -1;
		}
		return i + 1;
	}

	protected void comesFrom(Cell cell) {
		for (int i = last.length - 1; i > 0; i--) {
			last[i] = last[i - 1];
		}

		last[0] = cell;
	}

	public String toString() {
		String r = " L ";
		if (piece >= 0) {
			r = (new StringBuilder(String.valueOf(Constants.PIECE_TYPE[piece]))).append(isFirst ? "R" : "B").toString();
		}
		return r;
	}
}
