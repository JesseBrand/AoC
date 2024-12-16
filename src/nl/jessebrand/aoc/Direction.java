package nl.jessebrand.aoc;

public enum Direction {
	NORTH(0, -1),
	EAST(1, 0),
	SOUTH(0, 1),
	WEST(-1, 0);
	
	private final int xInc;
	private final int yInc;

	private Direction(final int xInc, final int yInc) {
		this.xInc = xInc;
		this.yInc = yInc;
	}
	
	public int getXInc() {
		return xInc;
	}
	
	public int getYInc() {
		return yInc;
	}

	public Direction rotateLeft() {
		return switch(this) {
			case NORTH -> Direction.WEST;
			case EAST -> Direction.NORTH;
			case SOUTH -> Direction.EAST;
			case WEST -> Direction.SOUTH;
		};
	}

	public Direction rotateRight() {
		return switch(this) {
		case NORTH -> Direction.EAST;
		case EAST -> Direction.SOUTH;
		case SOUTH -> Direction.WEST;
		case WEST -> Direction.NORTH;
		};
	}
}
