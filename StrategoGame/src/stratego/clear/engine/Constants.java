package stratego.clear.engine;

public class Constants {

	public static final int ROWS = 10;
	public static final int START_ROWS = 4;
	public static final int COLUMNS = 10;
	protected static final int TWO_FIELDS = 3;
	public static final int MAX_MOVES = 200;
	protected static final String PIECE_TYPE[] = { "FL", "FB", "SB", "MA", "GE", "FM", "SM", "FS", "SS", "SP" };
	protected static final int PIECE_VALUE[] = { 0, 0, 0, 10, 9, 3, 3, 2, 2, 1 };
	protected static final int FORBIDDEN[][] = { { 4, 2 }, { 4, 3 }, { 4, 6 }, { 4, 7 }, { 5, 2 }, { 5, 3 }, { 5, 6 },
			{ 5, 7 } };

	public Constants() {
	}

}
