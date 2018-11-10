package gj.stratego.player.pratesi2;

import java.util.ArrayList;

public class CircularQueueArrayList {
	private int front, rear, max;
	private ArrayList<Position> queue;

	public CircularQueueArrayList(int m) {
		max = m;
		front = rear = 0;
		queue = new ArrayList<Position>(max);
	}

	public void empty() {
		queue.clear();
	}

	public boolean is_Empty() {
		return queue.isEmpty();
	}

	private int increase(int index) {
		return (index + 1) % max;
	}

	public void push(Position x) {
		rear = increase(rear);
		queue.add(rear, x);
//		queue.add(x);
//		rear = increase(rear);
	}

	public Position top() {
		return queue.get(front);
	}

	public Position pop() {
		int x = front;
		front = increase(front);
		return queue.remove(x);
	}

	public String toString() {
		return queue.toString();
	}

	public boolean contains(Position x) {
		for (Position value : queue) {
			if (value.row == x.row && value.col == x.col) {
				return true;
			}
		}
		return false;
	}

}
