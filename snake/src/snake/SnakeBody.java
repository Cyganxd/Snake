package snake;

public class SnakeBody {

	private Location currentLocation;
	private String currentDirection;

	public SnakeBody(Location currentLocation) {
		setCurrentLocation(currentLocation);

		setCurrentDirection("right");
		// TODO Auto-generated constructor stub
	}

	public String getCurrentDirection() {
		return currentDirection;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentDirection(String currentDirection) {
		this.currentDirection = currentDirection;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

}
