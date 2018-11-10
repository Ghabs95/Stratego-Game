package stratego.clear.engine;

import gj.stratego.exception.*;

// Referenced classes of package gj.stratego.engine:
//            Constants, Piece, Cell, Move, 
//            Direction

public class Board {

	private Piece board[][];

	protected Board() {
		board = new Piece[10][10];
		for (int i = 0; i < Constants.FORBIDDEN.length; i++) {
			board[Constants.FORBIDDEN[i][0]][Constants.FORBIDDEN[i][1]] = new Piece(-1, true);
		}

	}

	protected void checkSourceCell(Cell cell, boolean isFirst) throws WrongSourceCellException, WrongPieceException {
		if (board[cell.getRow()][cell.getColumn()] == null
				|| board[cell.getRow()][cell.getColumn()].isFirst() != isFirst) {
			WrongSourceCellException wse = new WrongSourceCellException();
			String msg = (new StringBuilder("\nMethod: checkSourceCell\nSource cell: ")).append(cell).toString();
			wse.setMessage(isFirst ? "Red" : "Blue", toString(), msg);
			throw wse;
		}
		if (board[cell.getRow()][cell.getColumn()].getPiece() >= 0
				&& board[cell.getRow()][cell.getColumn()].getPiece() <= 2) {
			WrongPieceException wpe = new WrongPieceException();
			String msg = "Method: checkSourceCell\n";
			msg = (new StringBuilder(String.valueOf(msg))).append("Source cell: ").append(cell).append("\n").toString();
			msg = (new StringBuilder(String.valueOf(msg))).append("Piece: ")
					.append(Constants.PIECE_TYPE[board[cell.getRow()][cell.getColumn()].getPiece()]).toString();
			wpe.setMessage(isFirst ? "Red" : "Blue", toString(), msg);
			throw wpe;
		} else {
			return;
		}
	}

	protected void checkTargetCell(Cell source, Cell target, boolean isFirst)
			throws WrongTargetCellException, TwoFieldsException {
		if (!isEmpty(target) && isFirst(target, isFirst) || isForbidden(target)) {
			WrongTargetCellException wte = new WrongTargetCellException();
			String msg = "Method: checkTargetCell\n";
			msg = (new StringBuilder(String.valueOf(msg))).append("Target cell: ").append(target).toString();
			wte.setMessage(isFirst ? "Red" : "Blue", toString(), msg);
			throw wte;
		}
		int twoFields = isTwoFields(source, target);
		if (twoFields > 0) {
			TwoFieldsException tfe = new TwoFieldsException();
			String msg = "Method: checkTargetCell\n";
			msg = (new StringBuilder(String.valueOf(msg))).append("Target cell: ").append(target).append("\n")
					.toString();
			msg = (new StringBuilder(String.valueOf(msg))).append("Last time visited: ").append(twoFields)
					.append(" move(s) ago").toString();
			tfe.setMessage(isFirst ? "Red" : "Blue", toString(), msg);
			throw tfe;
		} else {
			return;
		}
	}

	private void empty(Cell cell) {
		board[cell.getRow()][cell.getColumn()] = null;
	}

	protected Piece getPiece(int row, int col) {
		return board[row][col];
	}

	private boolean isEmpty(Cell cell) {
		return board[cell.getRow()][cell.getColumn()] == null;
	}

	private boolean isFirst(Cell cell, boolean isFirst) {
		return board[cell.getRow()][cell.getColumn()].isFirst() == isFirst;
	}

	private boolean isForbidden(Cell cell) {
		boolean r = false;
		for (int i = 0; !r && i < Constants.FORBIDDEN.length; i++) {
			if (cell.getRow() == Constants.FORBIDDEN[i][0] && cell.getColumn() == Constants.FORBIDDEN[i][1]) {
				r = true;
			}
		}

		return r;
	}

	private int isTwoFields(Cell source, Cell target) {
		Piece piece = board[source.getRow()][source.getColumn()];
		return piece.lastTimeVisited(target);
	}

	protected void move(Cell source, Cell target, boolean isFirst) {
		if (board[target.getRow()][target.getColumn()] == null) {
			swap(source, target, isFirst);
			board[target.getRow()][target.getColumn()].comesFrom(source);
		} else {
			int r = board[source.getRow()][source.getColumn()].compare(board[target.getRow()][target.getColumn()]);
			updateOnResult(r, source, target, isFirst);
		}
	}

	protected Cell scoutTarget(Move move, boolean isFirst) throws WrongScoutPathException {
		Cell currentCell = move.getCell();
		int current[] = { currentCell.getRow(), currentCell.getColumn() };
		int i = 1;
		boolean ok;
		for (ok = true; ok && i <= move.getSteps(); i++) {
			currentCell.update(current, move.getDir().getDir(), isFirst);
			currentCell = new Cell(current);
			if (!isEmpty(currentCell) || isForbidden(currentCell)) {
				ok = false;
			}
		}

		if (!ok) {
			WrongScoutPathException wspe = new WrongScoutPathException();
			String msg = "Method: scoutTarget\n";
			msg = (new StringBuilder(String.valueOf(msg))).append("Move: ").append(move.toString()).toString();
			wspe.setMessage(isFirst ? "Red" : "Blue", toString(), msg);
			throw wspe;
		} else {
			return currentCell;
		}
	}

	protected void set(Cell cell, Piece piece, boolean isFirst) throws TakenCellException {
		if (board[cell.getRow()][cell.getColumn()] == null) {
			board[cell.getRow()][cell.getColumn()] = piece;
		} else {
			TakenCellException tce = new TakenCellException();
			String msg = "Method: set\n";
			msg = (new StringBuilder(String.valueOf(msg))).append("Cell: ").append(cell).toString();
			tce.setMessage(isFirst ? "Red" : "Blue", toString(), msg);
			throw tce;
		}
	}

	private void swap(Cell source, Cell target, boolean isFirst) {
		try {
			empty(target);
			set(target, board[source.getRow()][source.getColumn()], isFirst);
			empty(source);
		} catch (TakenCellException takencellexception) {
		}
	}

	private void updateOnResult(int r, Cell source, Cell target, boolean isFirst) {
		if (r == 0) {
			swap(source, target, isFirst);
			board[target.getRow()][target.getColumn()].comesFrom(source);
		} else if (r == 1) {
			empty(source);
		} else if (r == 2) {
			empty(source);
			empty(target);
		}
	}

	public String toString() {
		String r = "   0   1   2   3   4   5   6   7   8   9\n";
		r = (new StringBuilder(String.valueOf(r))).append(" ----------------------------------------\n").toString();
		for (int i = 0; i < 10; i++) {
			r = (new StringBuilder(String.valueOf(r))).append(i).append("|").toString();
			for (int j = 0; j < 10; j++) {
				if (board[i][j] == null) {
					r = (new StringBuilder(String.valueOf(r))).append("   |").toString();
				} else {
					r = (new StringBuilder(String.valueOf(r))).append(board[i][j]).append("|").toString();
				}
			}

			r = (new StringBuilder(String.valueOf(r))).append("\n").toString();
		}

		return r;
	}
}
