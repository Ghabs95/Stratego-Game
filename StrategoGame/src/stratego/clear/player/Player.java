package stratego.clear.player;

public interface Player {

	public abstract void start(boolean flag);

	public abstract int[] position(String s);

	public abstract void viewPositions(int ai[][]);

	public abstract int[] move();

	public abstract void tellMove(int ai[]);

	public abstract void fight(String s);
}
