package gj.stratego.player.pratesi.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gj.stratego.player.pratesi.PratesiPlayer;

@SuppressWarnings("serial")
public class StrategoPanel extends JPanel implements MouseListener {
	// private Board board;
	private int rows;
	private static int size;
	private Font font;
	public static boolean isFirst = PratesiPlayer.isFirst;
	ImageIcon icon;
	JLabel thumb;
	private Image bg;
	private static ImageIcon mySkins[] = new ImageIcon[10];
	private static ImageIcon oppSkins[] = new ImageIcon[10];
	private final static Color RED_PLAYER = new Color(255, 0, 0, 70);
	private final static Color BLUE_PLAYER = new Color(0, 0, 255, 70);
	
	public final static int[][] PIECE_POS_SECOND = PratesiPlayer.PIECE_POS_SECOND;
	public final static int[][] PIECE_POS_FIRST = PratesiPlayer.PIECE_POS_FIRST;
	public final int[][] FORBIDDEN_CELLS = PratesiPlayer.FORBIDDEN_CELLS;

	public StrategoPanel(int rows, int size) throws IOException {
		this.rows = rows;
		this.size = size;
		// board = new Board(rows);
		setPreferredSize(new Dimension(size * (rows + 2), size * (rows + 2)));
		setBackground(Color.LIGHT_GRAY);
		addMouseListener(this);
		imagePanel("/gj/stratego/player/pratesi/image/grid.jpg");
		loadPieces("/gj/stratego/player/pratesi/image/", !isFirst);
		loadPieces("/gj/stratego/player/pratesi/image/", isFirst);

	}

	private void imagePanel(String path) throws IOException {
		URL in;
		in = this.getClass()
				.getResource(path);
		bg = ImageIO.read(in);

	}

	private void loadPieces(String path, boolean isFirst) throws IOException {
		for (int p = 0; p < mySkins.length; p++) {
			URL in = this.getClass()
					.getResource(path + p + ".png");
			if (isFirst)
				oppSkins[p] = new ImageIcon(in);
			else
				mySkins[p] = new ImageIcon(in);
		}
	}

	public void reset() {
		// board = new Board(rows);
		repaint();
	}

	private void drowForbiddenCells(Graphics g) {
		g.setColor(new Color(0, 255, 0, 100));
		for (int i = 0; i < FORBIDDEN_CELLS.length; i++) {
			int[] temp = FORBIDDEN_CELLS[i];
			g.fillRect(size * (temp[1] + 1), size * (temp[0] + 1), size, size);
		}
	}

	private static void drowStartPieces(Graphics g, int[][] position, boolean isFirst) {
		setColour(g, isFirst);
		drowP(g, position);
		if (isFirst) {
			g.setColor(RED_PLAYER);
			drowP(g, PIECE_POS_FIRST);
		} else {
			g.setColor(BLUE_PLAYER);
			drowP(g, PIECE_POS_SECOND);
		}
		g.setColor(Color.black);
	}

	private static void drowP(Graphics g, int[][] position) {
		for (int p = 0; p < mySkins.length; p++) {
			int[] temp = position[p];
			g.drawImage(oppSkins[p].getImage(), size * (temp[1] + 1), size * (temp[0] + 1), size, size, null);
			g.fillRect(size * (temp[1] + 1), size * (temp[0] + 1), size, size);
		}
	}

	private static void setColour(Graphics g, boolean isFirst) {
		if(isFirst){
			g.setColor(new Color(0, 0, 255));
		} else {
			g.setColor(new Color(255, 0, 0));
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int row, col;
		g.setColor(Color.WHITE);
		g.fillRect(size, size, rows * size, rows * size);
		g.setColor(Color.BLACK);
		font = new Font("Verdana", 1, 18);
		g.setFont(font);

		// BackGroung image
		g.drawImage(bg, size, size, this.getWidth() - size * 2, this.getHeight() - size * 2, null);

		// Forbidden cells
		drowForbiddenCells(g);

		// Drown pieces, if isFirst -> true, false;
		drowStartPieces(g, PratesiPlayer.oppYZ, false);

		// Numbers
		for (int i = 0; i < rows; i++) {
			int x = (i + 1) * size;
			g.drawString(Integer.toString(i), x + 20, size - 10);
		}
		for (int j = 0; j < rows; j++) {
			int y = (j + 1) * size;
			g.drawString(Integer.toString(j), size - 20, y + 30);
		}

		// Lines
		for (row = 0; row <= rows; row++) {
			int y = (row + 1) * size;
			g.drawLine(size, y, size * (rows + 1), y);
		}
		for (col = 0; col <= rows; col++) {
			int x = (col + 1) * size;
			g.drawLine(x, size, x, size * (rows + 1));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
