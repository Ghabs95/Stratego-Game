package gj.stratego.player.pratesi2;

public class CircularBuffer {
	private Position data[];
	private int index;
	private int max;

	public CircularBuffer(Integer number) {
		index = 0;
		max = number;
		data = new Position[max];
	}

	public void add(Position x) {
		data[index] = x;
		index = (index + 1) % max;
	}
	
	public boolean contains(Position x) {
		int i = 0;
		for (Position value : data) {
			if (data[i++] != null && value.row == x.row && value.col == x.col) {
				return true;
			}
		}
		return false;
	}
	
}