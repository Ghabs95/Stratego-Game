package gj.stratego.player.pratesi2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gj.stratego.player.Player;

public class PratesiPlayer2 implements Player {
	public static boolean isFirst;
	private static final int[][] PIECE_POS_FIRST = { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 0, 2 }, { 2, 1 }, { 2, 3 },
			{ 1, 4 }, { 2, 0 }, { 0, 3 }, { 2, 2 } };
	private static final int[][] PIECE_POS_SECOND = { { 9, 0 }, { 6, 0 }, { 8, 0 }, { 9, 2 }, { 7, 1 }, { 7, 3 },
			{ 8, 4 }, { 7, 0 }, { 9, 3 }, { 7, 2 } };

	private final int[][] FORBIDDEN_CELLS = { { 4, 2 }, { 4, 3 }, { 5, 2 }, { 5, 3 }, { 4, 6 }, { 4, 7 }, { 5, 6 },
			{ 5, 7 } };

	public static final int BOARD_SIZE = 10;
	public static final int UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;

	public static int[] myLastMove, oppLastMove;
	public static int[] myDesiredCell, oppDesiredCell;
	public static int xy[][];
	public static int yz[][];
	public static String[][] board;


	private final int OUT = 10;
	private final int NUM_UNMOVABLE_PIECES = 3;
	private final int GET_VALUE = 0, GET_POWER = 1;
	private final int WON = 1, LOST = -1, TIE = 0;

	private int[] lastWalk;
	private boolean isMyTurn;
	
	private CircularBuffer[] buffer;

	private PieceActions pieceActions;
	
	private List<Integer> numMovPieces = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));

	private String[] posPiece = { "FL", "FB", "SB", "MA", "GE", "FM", "SM", "FS", "SS", "SP" };

	public PratesiPlayer2() {
		board = new String[BOARD_SIZE][BOARD_SIZE];
		xy = new int[BOARD_SIZE][2];
		yz = new int[BOARD_SIZE][2];
		myLastMove = new int[2];
		oppLastMove = new int[4];
		lastWalk = new int[2];
		initQueue();
		pieceActions = new PieceActions();
	}

	@Override
	public void start(boolean isFirst) {
		PratesiPlayer2.isFirst = isMyTurn = isFirst;
		for (int i = 0; i < FORBIDDEN_CELLS.length; i++) {
			int[] a = FORBIDDEN_CELLS[i];
			board[a[0]][a[1]] = " L ";
		}
		xy = isFirst ? copyPieceTable(PIECE_POS_FIRST, xy) : copyPieceTable(PIECE_POS_SECOND, xy);
	}

	private int[][] copyPieceTable(int[][] piecePos, int[][] dst) {
		for (int i = 0; i < piecePos.length; i++) {
			dst[i] = Arrays.copyOf(piecePos[i], piecePos[i].length);
		}
		return dst;
	}

	@Override
	public int[] position(String piece) {
		int i = pieceData(piece, GET_VALUE);
		int[] rst = isFirst ? PIECE_POS_FIRST[i] : PIECE_POS_SECOND[i];
		return rst;
	}

	private void startBoard() {
		int i = 0;
		for (PieceType p : PieceType.values()) {
			int[] a = isFirst ? PIECE_POS_FIRST[i++] : PIECE_POS_SECOND[i++];
			board[a[0]][a[1]] = p.toString() + (isFirst ? "R" : "B");
		}
	}

	public static int[] getMyMove(int[] myMove) {
		Position myCoords = new Position(myMove[0], myMove[1]);
		int dir = myMove[2], steps = myMove[3];
		if (dir == 1) {
			myCoords.row = isFirst ? myCoords.row + steps : myCoords.row - steps;
		} else if (dir == 2) {
			myCoords.row = isFirst ? myCoords.row - steps : myCoords.row + steps;
		} else if (dir == 3) {
			myCoords.col = isFirst ? myCoords.col + steps : myCoords.col - steps;
		} else {
			myCoords.col = isFirst ? myCoords.col - steps : myCoords.col + steps;
		}
		return new int[] { myCoords.row, myCoords.col };
	}

	private void updateMyBoard(int[] old, int[] dst) {
		String temp = board[old[0]][old[1]];
		board[old[0]][old[1]] = null;
		board[dst[0]][dst[1]] = temp;
	}

	private void updateOppBoard(int[] oppMove) {
		Position oppCoords = new Position(oppMove[0], oppMove[1]);
		int dir = oppMove[2], steps = oppMove[3];
		board[oppCoords.row][oppCoords.col] = null;
		if (dir == 1) {
			oppCoords.row = isFirst ? oppCoords.row - steps : oppCoords.row + steps;
		} else if (dir == 2) {
			oppCoords.row = isFirst ? oppCoords.row + steps : oppCoords.row - steps;
		} else if (dir == 3) {
			oppCoords.col = isFirst ? oppCoords.col - steps : oppCoords.col + steps;
		} else {
			oppCoords.col = isFirst ? oppCoords.col + steps : oppCoords.col - steps;
		}
		if (board[oppCoords.row][oppCoords.col] == null)
			board[oppCoords.row][oppCoords.col] = "OPP";
	}

	@Override
	public void viewPositions(int[][] position) {
		int i = isFirst ? 6 : 0;
		int j = 0;
		int end = isFirst ? BOARD_SIZE : position.length;
		for (; i < end; i++) {
			int[] a = position[j++];
			for (int op = 0; op < a.length; op++) {
				if (a[op] == 1) {
					board[i][op] = "OPP";
				}
			}
		}
		startBoard();
	}

	private Position findFirstFreeCell(int[] current, int index) {
		List<Position> neighbours = Pursue.getNeighbour(new Position(current[0], current[1]));
		for (Position p : neighbours) {
			if (board[p.row][p.col] == null && !buffer[index].contains(p)) {
				return p;
			}
		}
		return null;
	}

	private void initQueue() {
		buffer = new CircularBuffer[10];
		for (int i = 0; i < 10; i++) {
			buffer[i] = new CircularBuffer(3);
		}
	}

	@Override
	public int[] move() {
		boolean flag = false;
		isMyTurn = true;
		int[] outArray = { OUT, OUT };
		int[] result = new int[4];
		PieceType[] movablePieces = { PieceType.MA, PieceType.GE, PieceType.FM, PieceType.SM, PieceType.FS,
				PieceType.SS, PieceType.SP };

		while (!flag) {
			int randomMovable = (int) (Math.random() * numMovPieces.size());
			int randomGetMovable = numMovPieces.get(randomMovable);

			PieceType p = movablePieces[randomGetMovable];
			int index = p.getValue();

			if (!Arrays.equals(xy[index], outArray)) {
				int[] currCell = xy[index];
				int[] nextPos = pieceActions.pieceMove(p);

				if (nextPos != null) {
					int[] nextCell = getMyMove(nextPos);

					if ((currCell[0] != nextCell[0] && currCell[1] != nextCell[1])
							|| (currCell[0] == nextCell[0] && currCell[1] != nextCell[1])
							|| (currCell[0] != nextCell[0] && currCell[1] == nextCell[1])) {
						
						if (!buffer[index].contains(new Position(nextCell[0], nextCell[1]))) {
							buffer[index].add(new Position(currCell[0], currCell[1]));
							lastWalk = nextCell.clone();
							xy[index] = nextCell;
							updateMyBoard(currCell, nextCell);
							flag = true;
							result = nextPos.clone();
					
						} else {
							Position newP = findFirstFreeCell(currCell, index);
							if (newP != null) {
								int dir = pieceActions.pathToDirection(newP, currCell);
								buffer[index].add(new Position(currCell[0], currCell[1]));
								lastWalk = newP.toArray();
								xy[index] = newP.toArray();
								updateMyBoard(currCell, newP.toArray());
								flag = true;
								return new int[] { currCell[0], currCell[1], dir, 1 };
							}
						}
					}
				}
			}
		}
		return result;
	}

	public int[] directionToPath(int[] move) {
		int cordX = move[0];
		int cordY = move[1];
		int dir = move[2];
		int steps = move[3];
		if (dir == UP) {
			cordX = isFirst ? cordX - steps : cordX + steps;
		} else if (dir == DOWN) {
			cordX = isFirst ? cordX + steps : cordX - steps;
		} else if (dir == LEFT) {
			cordY = isFirst ? cordY - steps : cordY + steps;
		} else {
			cordY = isFirst ? cordY + steps : cordY - steps;
		}
		return new int[] { cordX, cordY };
	}

	@Override
	public void tellMove(int[] move) {
		isMyTurn = false;
		oppLastMove = move.clone();
		oppDesiredCell = directionToPath(oppLastMove);
		updateOppBoard(move);
	}

	/**
	 * This method call "getFightValue( )" and assesses the results. If I won
	 * then set the corresponding value to OUTconstant in yz matrix If Opponent
	 * won set the corresponding value to OUTconstant in xz matrix If there is a
	 * tie set the corresponding value to OUTconstant in xz and yz matrix
	 * 
	 * @param piece
	 *            Take the opponent piece where are fighting.
	 */

	@Override
	public void fight(String piece) {
		int index = pieceData(piece, GET_VALUE);
		yz[index] = isMyTurn ? myDesiredCell : oppDesiredCell;

		int posMyBoard = posPieceValue(isMyTurn ? myDesiredCell : oppDesiredCell);
		
		String myPiece = posPiece[posMyBoard];
		int val = getFightResult(piece, myPiece);

		if (val == WON) {
			Arrays.fill(yz[index], OUT);

		} else if (val == LOST) {
			int[] a = xy[posPieceValue(isMyTurn ? lastWalk : oppDesiredCell)];
			board[a[0]][a[1]] = "OPP";
			Arrays.fill(xy[posPieceValue(isMyTurn ? lastWalk : oppDesiredCell)], OUT);
			numMovPieces.remove(new Integer(posMyBoard - NUM_UNMOVABLE_PIECES));

		} else {
			int[] a = xy[posPieceValue(isMyTurn ? lastWalk : oppDesiredCell)];
			board[a[0]][a[1]] = null;
			Arrays.fill(xy[posPieceValue(isMyTurn ? lastWalk : oppDesiredCell)], OUT);
			Arrays.fill(yz[index], OUT);
			numMovPieces.remove(new Integer(posMyBoard - NUM_UNMOVABLE_PIECES));
		}
	}

	/**
	 * a This method generate the integer result of the fight. "getFightValue"
	 * look and find where my piece is in the xy matrix, then invoce piecePower
	 * method, that return me the "power" value of the piece.
	 * 
	 * @param oppPiece
	 *            Take the opponent piece where are fighting.
	 * @param myCurrent
	 *            Take my piece coordinates.
	 * @return return the result of the fight: 1 = my piece won, -1 = opponent
	 *         piece won, 0 = tie.
	 */
	private int getFightResult(String oppPiece, String myPiece) {
		int result = TIE;
		int oppPower = pieceData(oppPiece, GET_POWER);
		int myPower = pieceData(myPiece, GET_POWER);

		if (oppPiece.matches("FB|SB") && myPiece.matches("MA|GE|FS|SS|SP")) {
			result = LOST;
		} else if (myPiece.matches("FB|SB") && oppPiece.matches("MA|GE|FS|SS|SP")) {
			result = WON;
		} else if (oppPiece.matches("MA") && myPiece.matches("SP") && isMyTurn) {
			result = WON;
		} else if (oppPiece.matches("SP") && myPiece.matches("MA") && !isMyTurn) {
			result = LOST;
		} else if (oppPower < myPower) {
			result = WON;
		} else if (oppPower > myPower) {
			result = LOST;
		}
		return result;
	}
 
	private int posPieceValue(int[] current) {
		for (int i = 0; i < xy.length; i++) {
			if (Arrays.equals(xy[i], current))
				return i;
		}
		return 0;
	}

	private int pieceData(String piece, int selector) {
		return Arrays.stream(PieceType.values())
				.filter(e -> piece.equals(e.toString()))
				.map(selector == GET_VALUE ? e -> e.getValue() : e -> e.getPower())
				.findFirst()
				.get();
	}

}
