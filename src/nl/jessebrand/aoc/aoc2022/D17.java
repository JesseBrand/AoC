package nl.jessebrand.aoc.aoc2022;

import static nl.jessebrand.aoc.Utils.out;
import static nl.jessebrand.aoc.Utils.readFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.jessebrand.aoc.Point;

public class D17 {
	
	private static final int DIST_Y = 3;
	private static final int LEVEL_WIDTH = 7;
	private static final long BLOCK_COUNT = 1000000000000L;
//	private static final int MAX_ROWS = 100;
//	private static final int OUTPUT_PER = 1000;
//	private static final int START = 10;
	private static final int DRAW_TOP_LINES = 3;
	
	public static void main(String[] args) throws IOException {
		List<String> lines = readFile("2022/d17");
		
		String moves = lines.get(0);

		Level level = new Level();
		
		
		// it should be a repeating pattern of type + move, not block

		int m = 0;
		int b = 0;
		int blockCount0 = 0, blockCount1 = 0, blockCount2 = 0;
		while (blockCount0 == 0) {
			BlockType blockType = BlockType.values()[(int) (b % 5)];
			Block block = new Block(blockType, 2, level.getRowCount() + DIST_Y);
//			System.out.println("Block " + blockType + " started at " + block.points);
			while (true) {
				char move = moves.charAt(m % moves.length());
				m++;
				if (m >= moves.length()) {
//					m-= moves.length();
//					startBulkHeight = endBulkHeight;
//					endBulkHeight = level.getRowCount();
					out("overflow m; rows = %d; block=%s;", level.getRowCount(), block);
				}
				boolean movedH = applyMove(block, level, move);
				boolean fixed = moveDown(block, level);
				
				if (fixed) {
					if (m >= moves.length()) {
						m-= moves.length();
						blockCount0 = blockCount1;
						blockCount1 = blockCount2;
						blockCount2 = b;
						out("came to rest; reset m = %d; rows = %d; lastBlock=%s; startBlock=%d; endBlock=%d; diff=%d", m, level.getRowCount(), block, blockCount1, blockCount2, blockCount2 - blockCount1);
					}
//					System.out.println(String.format("Moved %c (%b), now stuck at %s", move, movedH, block.points));
					b++;
					break;
				}
//				System.out.println(String.format("Moved %c (%b) and down, now at %s", move, movedH, block.points));
			}

//			System.out.println(level);
//			if ((b + 1) % OUTPUT_PER == 0) {
//				System.out.println(String.format("Height after %d (%d / %d) (%d%%) blocks: %d", (b + 1), b, end, (b + 1) * 100 / end, level.getRowCount()));
//			}
//			if (b == start) {
//			if (b == start) {
//				startBulkHeight = level.getRowCount();
//				System.out.println(String.format("startBulkHeight set to %d after b = %d (block %d)", startBulkHeight, b, b + 1));
//			}
		}

		int blockBulkSize = blockCount1 - blockCount0;
		System.out.println("Number of moves: " + moves.length() +", bulkSize: " + blockBulkSize);
		int bulkCount = (int) (BLOCK_COUNT / blockBulkSize) - 1;
		int start = (int) (BLOCK_COUNT % blockBulkSize) - 1 + blockBulkSize;
		int end = start + blockBulkSize;
		System.out.println(String.format("Will count from move cycles 0-%d (%d), then %d-%d (%d) * %d makes %d", start, start + 1, start + 1, end, end - start, bulkCount, start + 1 + (end - start) * (long) bulkCount));

		level = new Level();
		m = 0;
		b = 0;
		int lineCountStart = 0, lineCountEnd = 0;
		int blockCountStart = 0, blockCountEnd = 0;
		
		out("\nEntering second cycle");
		
		// TODO: tussenstap nodig? als we op zoveel blocks uit willen komen, wat doet dat dan met de moves?
		
		Block block = null;
		for (int i = 0; lineCountEnd == 0 || block != null; i++) {
			m = i % moves.length();
			if (block == null) {
				BlockType blockType = BlockType.values()[(int) (b % 5)];
				block = new Block(blockType, 2, level.getRowCount() + DIST_Y);
			}
			char move = moves.charAt(m);
			boolean movedH = applyMove(block, level, move);
			boolean fixed = moveDown(block, level);
			
			if (fixed) {
				// b = the one we just did
				if (b == start) {
					blockCountStart = b;
					lineCountStart = level.getRowCount();
					out("start set to %d lines after block %d; m=%d", lineCountStart, b, m);
				}
				if (b == end) {
					blockCountEnd = b;
					lineCountEnd = level.getRowCount();
					out("end set to %d lines after block %d; m=%d", lineCountEnd, b, m);
				}
				if (b == 2022) {
					System.out.println("Solution 1: " + level.getRowCount());
				}
				b++;
				block = null;
			}
		}

		out("blockCountStart = %d; startBulkHeight = %d", blockCountStart, lineCountStart);
		out("blockCountEnd = %d; endBulkHeight = %d", blockCountEnd, lineCountEnd);
		out("total blocks = start + (end - start) * count = %d + (%d - %d) (%d) * %d = %d", blockCountStart + 1, blockCountEnd, blockCountStart, blockCountEnd - blockCountStart, bulkCount, blockCountStart + 1 + (blockCountEnd - blockCountStart) * (long) bulkCount);
		int lineBulkSize = lineCountEnd - lineCountStart;
		out("total lines = start + (end - start) * count = %d + (%d - %d) (%d) * %d = %d", lineCountStart, lineCountEnd, lineCountStart, lineCountEnd - lineCountStart, bulkCount, lineCountStart + (lineCountEnd - lineCountStart) * (long) bulkCount);
		
		
		
		
		
//		endBulkHeight = level.getRowCount();
//		System.out.println(String.format("Height after block %d (%d / %d) (%d%%) blocks: %d", (b + 1), b, end, (b + 1) * 100 / end, level.getRowCount()));
//		System.out.println(String.format("endBulkHeight set to %d (diff %d) after b = %d (block %d)", endBulkHeight, endBulkHeight - startBulkHeight, b, b + 1));
		
//		// debug
//		for (int i = 1; i < 10; i++) {
//			for (; b < end + bulkSize * i; b++) {
//				BlockType blockType = BlockType.values()[(int) (b % 5)];
//				Block block = new Block(blockType, 2, level.getRowCount() + DIST_Y);
//				while (true) {
//					char move = moves.charAt(m);
//					m = (m + 1) % moves.length();
//					applyMove(block, level, move);
//					if (moveDown(block, level)) {
//						break;
//					}
//				}
//			}
//			System.out.println(String.format("endBulkHeight set to %d (diff %d) after b = %d (block %d)", level.getRowCount(), level.getRowCount() - endBulkHeight, b, b + 1));
//			out("Last block was %s, last move was %d", BlockType.values()[(int) (b % 5)], m);
//			endBulkHeight = level.getRowCount();
//
//			for (int r = 0; r < DRAW_TOP_LINES; r++) {
//				final boolean[] row = level.getRow(endBulkHeight - r - 1);
//				out(drawLine(row));
//			}
//		}
		
		
//		System.out.println(level);
		System.out.println(String.format("Total = %d + (%d * %d) = %d", lineCountStart, lineCountEnd - lineCountStart, bulkCount, lineCountStart + ((lineCountEnd - lineCountStart) * (long) bulkCount)));
	}

	private static boolean moveDown(Block block, Level level) {
		boolean blocked = false;
		for (Point p : block.getPoints()) {
			if (level.get(p.x(), p.y() - 1)) {
				blocked = true;
				break;
			}
		}
		if (blocked) {
			for (Point p : block.getPoints()) {
				level.set(p.x(), p.y());
			}
		} else {
			List<Point> newPoints = new ArrayList<Point>();
			for (Point p : block.getPoints()) {
				newPoints.add(p.add(0, -1));
			}
			block.updatePoints(newPoints);
		}
		return blocked;
	}

	private static boolean applyMove(Block block, Level level, char move) {
		for (Point p : block.getPoints()) {
			if (move == '<' && level.get(p.x() - 1, p.y())) {
				return false;
			}
			if (move == '>' && level.get(p.x() + 1, p.y())) {
				return false;
			}
		}
		List<Point> newPoints = new ArrayList<Point>();
		for (final Point p : block.getPoints()) {
			newPoints.add(p.add(move == '<' ? -1 : 1, 0));
		}
		block.updatePoints(newPoints);
		return true;
	}

	static class Block {
		
		private final BlockType blockType;

		private List<Point> points;

		/**
		 * x and y are of bottom-left corner
		 */
		public Block(BlockType blockType, int x, int y) {
			this.blockType = blockType;
			points = new ArrayList<>();
			for (Point point : blockType.points) {
				points.add(point.add(x, y));
			}
		}
		
		public List<Point> getPoints() {
			return points;
		}
		
		void updatePoints(List<Point> points) {
			this.points = points;
		}
		
		@Override
		public String toString() {
			return String.format("Block[type=%s, points=%s]", blockType, points);
		}
	}
	
	static enum BlockType {
		HOR(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0)),
		PLUS(new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2)),
		INVL(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(2, 1), new Point(2, 2)),
		VERT(new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3)),
		SQUARE(new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1));
		
		private Point[] points;

		BlockType(Point...points) {
			this.points = points;
		}
		
		public Point[] getPoints() {
			return points;
		}
	}
	
	static class Level {
		
		private final List<boolean[]> rows;
		
		private int inc = 0;
		
		Level() {
			rows = new ArrayList<>();
//			addRow();
		}
		
		boolean get(int x, long row) {
			if (row < 0 || x < 0 || x >= LEVEL_WIDTH) {
				return true; // sides and bottom blocked
			}
			if (row >= getRowCount()) {
				return false; // top open
			}
			return getRow(row)[x];
		}
		
		private boolean[] getRow(long row) {
			return rows.get((int) (row - inc));
		}

		void set(int x, long row) {
			if (row < 0 || x < 0 || x >= LEVEL_WIDTH) {
				throw new IllegalArgumentException("Cannot set outside bounds: " + x + ", " + row);
			}
			while (getRowCount() <= row) { 
				addRow();
			}
			getRow(row)[x] = true;
		}
		
		private void addRow() {
			rows.add(new boolean[LEVEL_WIDTH]);
//			while (rows.size() > MAX_ROWS) {
//				rows.remove(0);
//				inc++;
//			}
		}
		
		private int getRowCount() {
			return rows.size() + inc;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("\n");
			if (inc > 0) {
				sb.append("skipped ").append(inc).append(" rows\n");
			}
			for (int i = rows.size() - 1; i >=0; i--) {
				final boolean[] row = rows.get(i);
				sb.append(drawLine(row)).append("\n");
			}
			sb.append("+-------+");
			return sb.toString();
		}
	}

	static String drawLine(boolean[] row) {
		final StringBuilder sb = new StringBuilder();
		sb.append("|");
		for (boolean b : row) {
			sb.append(b ? "#" : ".");
		}
		sb.append("|");
		return sb.toString();
	}
}

//      70000000
// 1000000000000

// a
// 2680 too low
// 3117 too low
// 3202 too low

// b
// 1575879496604 too high
// 1575859676964 too high
// 1575820037682 too high
// 1575839857322 too high (not sub)
// 1575811217521 wrong
// 1575811217522 wrong
// 1575811217520 wrong
// 1575811209487 RIGHT

