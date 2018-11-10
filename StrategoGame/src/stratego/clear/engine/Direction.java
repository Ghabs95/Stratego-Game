package stratego.clear.engine;

public class Direction {

	private int dir;

	protected Direction(int dir) {
		this.dir = dir;
	}

	protected int getDir() {
		return dir;
	}

	public String toString() {
		String dirName[] = { "forward", "backward", "left", "right" };
		return dirName[dir - 1];
	}
}
