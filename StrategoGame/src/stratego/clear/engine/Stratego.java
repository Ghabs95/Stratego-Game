package stratego.clear.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import gj.stratego.exception.StrategoException;
import gj.stratego.player.Player;

// Referenced classes of package gj.stratego.engine:
//            GameManager

public class Stratego {

	public Stratego() {
	}

	@SuppressWarnings("unchecked")
	private Player createPlayer(String name)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException  {
		@SuppressWarnings("rawtypes")
		Constructor constructor;
		@SuppressWarnings("rawtypes")
		Class playerClass = Class.forName(name);
		constructor = playerClass.getConstructor(new Class[0]);
		return (Player) constructor.newInstance(new Object[0]);
		// ClassNotFoundException e;
		//
		// System.err.println((new StringBuilder("The class
		// ")).append(name).append(" does not exist").toString());
		// System.exit(-1);
		// break MISSING_BLOCK_LABEL_198;
		//
		// System.err.println((new StringBuilder("The class
		// ")).append(name).append(" does not include the correct
		// constructor").toString());
		// System.exit(-1);
		// break MISSING_BLOCK_LABEL_198;
		//
		// System.err.println((new StringBuilder("The invocation of the
		// constructor of the class ")).append(name).append(" generated an
		// exception").toString());
		// System.exit(-1);
		// break MISSING_BLOCK_LABEL_198;
		//
		// System.err.println((new StringBuilder("The invocation of the
		// constructor of the class ")).append(name).append(" caused an illegal
		// access error").toString());
		// System.exit(-1);
		// break MISSING_BLOCK_LABEL_198;
		//
		// System.err.println((new StringBuilder("The instantiation of an object
		// of the class ")).append(name).append(" caused an error").toString());
		// System.exit(-1);
		// return null;
	}

	private void play(String firstPlayer, String secondPlayer, int gameNumber, boolean verbose) throws Exception {
		Player player[] = new Player[2];
		player[0] = createPlayer(firstPlayer);
		player[1] = createPlayer(secondPlayer);
		String playerName[] = new String[2];
		playerName[0] = firstPlayer.substring(firstPlayer.lastIndexOf('.') + 1);
		playerName[1] = secondPlayer.substring(secondPlayer.lastIndexOf('.') + 1);
		GameManager gameManager = new GameManager(player);
		int result[] = new int[3];
		int game = 1;
		System.out.println(
				(new StringBuilder(String.valueOf(playerName[0]))).append(" vs ").append(playerName[1]).toString());
		System.out.println((new StringBuilder("Beginning of ")).append(gameNumber).append(" games").toString());
		for (; game <= gameNumber; game++) {
			int winner = gameManager.playGame(verbose);
			result[winner]++;
			System.out.print((winner + 1) % 3);
			if (game % 50 == 0) {
				System.out.println();
			}
		}

		if ((game - 1) % 50 != 0) {
			System.out.println();
		}
		System.out.println((new StringBuilder("End of ")).append(gameNumber).append(" games").toString());
		System.out.println("RESULTS");
		System.out.println(
				(new StringBuilder("Player ")).append(playerName[0]).append(": ").append(result[0]).toString());
		System.out.println(
				(new StringBuilder("Player ")).append(playerName[1]).append(": ").append(result[1]).toString());
		System.out.println((new StringBuilder("Ties: ")).append(result[2]).toString());
		System.out.println();
	}

	private void run(String args[]) {
		int gameNumber = Integer.parseInt(args[0]);
		boolean verbose = Boolean.parseBoolean(args[3]);
		try {
			play(args[1], args[2], gameNumber, verbose);
			play(args[2], args[1], gameNumber, verbose);
		} catch (Exception e) {
			if (e instanceof StrategoException) {
				System.err.println((new StringBuilder(String.valueOf(e.getClass().getSimpleName())))
						.append(" was generated.\n").append((StrategoException) e).toString());
			} else {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}

	public static void main(String args[]) {
		Stratego f = new Stratego();
		f.run(args);
	}
}
