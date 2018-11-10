package gj.stratego.player.pratesi;

import java.util.Arrays;

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
		return Arrays.stream(data)
				.anyMatch(e -> e != null && e.row == x.row && e.col == x.col);
	}

} 