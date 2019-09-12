package alda.astar;

import java.util.HashMap;
import java.util.LinkedList;

public class Station implements Comparable<Station> {

	private String name;
	private HashMap<Station, Integer> costMap = new HashMap<>();
	private LinkedList<Connection> connections = new LinkedList<>();
	private Point p;
	private Time bestArrival;
	private double f;
	private Station path;
	private Connection bestConnection;
	public Station(String name, int x, int y) {

		this.name = name;
		p = new Point(x, y);
		bestArrival = null;
		f = Double.MAX_VALUE;

	}
	
	public LinkedList<Connection> getCon(){
		return connections;
	}
	
	public void addConnection(Connection c) {
		connections.add(c);
	}

	public String getName() {
		return name;
	}

	public Point getPoint() {
		return p;
	}

	public Time getBestArrival() {
		return bestArrival;
	}

	public void setArrivalInMin(int newTime) {
		int h = newTime/60;
		int min = newTime % 60;
		bestArrival = new Time(h, min);
	}
	public void setArrivalInTime(Time newTime) {
		bestArrival = newTime;
	}
	
	public int getArrivalInMin() {
		return bestArrival.calculateTimeInMin();
	}

	public double getF() {
		return f;
	}
	
	public void setF(double newF) {
		f = newF;
	}
	
	public Connection getBestConnection() {
		return bestConnection;
	}
	
	public void setBestConnection(Connection c) {
		bestConnection = c;
	}
	public void connectStations(Station s, int cost) {
		costMap.put(s, cost);
	}
	
	public int compareTo(Station other) {
		return Double.compare(f, other.f);
	}
	
	public void setPath(Station s) {
		path = s;
	}

	public Station getPath() {
		return path;
	}
	protected class Point {

		private int x;
		private int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

	}

}
