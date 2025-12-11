package nl.jessebrand.aoc;

import java.awt.Color;
import java.awt.Graphics;

public final class AStarRenderer {

	private final Grid<Boolean> grid;
	private final Path<Point> path;
	private final int scale;

	public AStarRenderer(final Grid<Boolean> grid, final Path<Point> path, final int scale) {
		this.grid = grid;
		this.path = path;
		this.scale = scale;
	}

	public void render(final Graphics g) {
		for (final Point p : grid) {
			if (grid.get(p)) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.WHITE);
			}
			g.fillRect(p.x() * scale, p.y() * scale, scale, scale);
		}
		for (final Point p : path) {
			if (p == path.first()) {
				g.setColor(Color.BLUE);
			} else if (p == path.last()) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.YELLOW);
			}
			g.fillOval(p.x() * scale, p.y() * scale, scale, scale);
			g.setColor(Color.BLACK);
			g.drawOval(p.x() * scale, p.y() * scale, scale, scale);
		}
	}
	
}
