package gj.stratego.player.pratesi2;

public class BoardPrinter {
	public BoardPrinter() {
	}

	public static String toString(String[][] board) {
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
