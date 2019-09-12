package alda.astar;

public class Bus {

	private final int busNr;
	private int speed;

	public Bus(int busNr, int speed) {
		this.busNr = busNr;
		this.speed = speed;
	}

	public double getTravelTime(double distance) {
		double kmPmin = speed / 60.0;
		return  distance / kmPmin;
	}

	public String toString() {
		return "Bus " + busNr;
	}

}
