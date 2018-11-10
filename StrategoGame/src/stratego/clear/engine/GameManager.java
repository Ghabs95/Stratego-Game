package stratego.clear.engine;

import gj.stratego.exception.WrongMoveException;
import gj.stratego.player.Player;

public class GameManager {

	private final Player player[];
	private Board board;

	protected GameManager(Player player[]) {
		this.player = player;
	}

	private void checkStartingCell(Cell cell, int p) throws Exception {
		if (p == 0) {
			cell.checkRange(0, 3, 0, 9);
		} else {
			cell.checkRange(6, 9, 0, 9);
		}
	}

	private boolean isWinner(Piece piece) {
		return piece != null && piece.getPiece() == 0;
	}

	private Piece makeMove(Move move, boolean isFirst) throws Exception {
		Cell source = move.getCell();
		Piece piece = null;
		board.checkSourceCell(source, isFirst);
		if (move.getSteps() == 1) {
			Cell target = source.move(move.getDir().getDir(), isFirst);
			piece = board.getPiece(target.getRow(), target.getColumn());
			board.checkTargetCell(source, target, isFirst);
			board.move(source, target, isFirst);
		} else if (move.getSteps() > 1) {
			Cell target = board.scoutTarget(move, isFirst);
			piece = board.getPiece(target.getRow(), target.getColumn());
			board.move(source, target, isFirst);
		} else {
			throw new WrongMoveException();
		}
		return piece;
	}

	protected int playGame(boolean verbose) throws Exception {
		startGame();
		int turn = 1;
		int moves = 0;
		boolean gameOver = false;
		boolean winner = false;
		for (; !gameOver; gameOver = winner || ++moves == 400) {
			turn = (turn + 1) % 2;
			winner = playTurn(turn, verbose);
		}

		return winner ? turn : 2;
	}

	private boolean playTurn(int turn, boolean verbose) throws Exception {
		if (verbose) {
			printBoard();
		}
		int m[] = player[turn].move();
		Piece sourcePiece = board.getPiece(m[0], m[1]);
		Move move = new Move(m);
		Piece targetPiece = makeMove(move, turn == 0);
		player[(turn + 1) % 2].tellMove(m);
		if (targetPiece != null) {
			player[turn].fight(targetPiece.toString().substring(0, 2));
			player[(turn + 1) % 2].fight(sourcePiece.toString().substring(0, 2));
		}
		return isWinner(targetPiece);
	}

	protected void printBoard() {
		System.out.println(board.toString());
	}

	private void showPlayerPositions(int position[][][]) {
		player[0].viewPositions(position[1]);
		player[1].viewPositions(position[0]);
	}

	private void startGame() throws Exception {
		board = new Board();
		startPlayers();
	}

	private void startPlayers() throws Exception {
		int position[][][] = new int[2][4][10];
		for (int p = 0; p < 2; p++) {
			player[p].start(p == 0);
			for (int i = 0; i < Constants.PIECE_TYPE.length; i++) {
				int coords[] = player[p].position(Constants.PIECE_TYPE[i]);
				Cell cell = new Cell(coords);
				checkStartingCell(cell, p);
				board.set(cell, new Piece(i, p == 0), p == 0);
				if (p == 0) {
					position[p][coords[0]][coords[1]] = 1;
				} else {
					position[p][(coords[0] - 10) + 4][coords[1]] = 1;
				}
			}

		}

		showPlayerPositions(position);
	}
}
