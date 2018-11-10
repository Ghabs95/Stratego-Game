package gj.stratego.player.pratesi;

public enum PieceType {
	FL(0, 0), FB(1, 0), SB(2, 0), MA(3, 10), GE(4, 9), FM(5, 3), SM(6, 3), FS(7, 2), SS(8, 2), SP(9, 1);

	private final int value;
	private final int power;

	PieceType(int value, int power) {
		this.value = value;
		this.power = power;
	}

	public int getValue() {
		return value;
	}

	public int getPower() {
		return power;
	}

	public String val(int n) {
		String[] enumString = { "FL", "FB", "SB", "MA", "GE", "FM", "SM", "FS", "SS", "SP" };
		return enumString[n];
	}

}
