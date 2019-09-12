package alda.astar;

public class Connection implements Comparable<Connection> {

	private Station s1;
	private Station s2;
	private double cost;
	private Bus transport;
	private Time departure;

	public Connection(Station s1, Station s2, double cost, Bus transport, Time departure) {

		this.s1 = s1;
		this.s2 = s2;
		this.cost = cost;
		this.transport = transport;
		this.departure = departure;

	}

	public int compareTo(Connection other) {

		return cost == other.cost ? 0 : (cost > other.cost ? 1 : -1);
	}

	public String toString() {
		return "[ " + s1.getName() + " to " + s2.getName() + " " + transport + " Arrival time: " + s2.getBestArrival() + " ]";
	}

	public Time getDeparture() {
		return departure;
	}

	public int calculateTime(Time from, Time to) {
		return from.getTimeDif(to);
	}

	public Station getS1() {
		return s1;
	}

	public Station getS2() {
		return s2;
	}

	public double getCost() {
		return cost;
	}

	public Bus getTransport() {
		return transport;
	}

}
