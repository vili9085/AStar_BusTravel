package alda.astar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStarGraph {

	HashMap<String, Station> stations = new HashMap<>();

	ArrayList<Station> allStations = new ArrayList<>();

	private int edgesCounter = 0;

	public boolean add(String stationName, int x, int y) {
		if (stations.containsKey(stationName) || stationName == null) {

			return false;
		}
		Station s = new Station(stationName, x, y);

		stations.put(s.getName(), s);
		allStations.add(s);

		return true;
	}

	public int getNumberOfStations() {
		return allStations.size();
	}

	private boolean containsStations(String s1, String s2) {
		if (!stations.containsKey(s1)) {
			System.out.println("Station: " + s1 + " does not exist!");
			return false;
		} else if (!stations.containsKey(s2)) {
			System.out.println("Station: " + s2 + " does not exist!");
			return false;
		}
		return true;
	}

	public boolean connect(String s1, String s2, Bus transport, int h, int min) {

		if (!containsStations(s1, s2)) {
			return false;
		}

		Station ss1 = stations.get(s1);
		Station ss2 = stations.get(s2);

		Time departure = new Time(h, min);
		double cost = calculateDistance(ss1, ss2);

		Connection c1 = new Connection(ss1, ss2, cost, transport, departure);
		Connection c2 = new Connection(ss2, ss1, cost, transport, departure);
		edgesCounter++;

		ss1.addConnection(c1);
		ss2.addConnection(c2);
		return true;

	}

	private double calculateDistance(Station s1, Station s2) {
		double posX = Math.pow(s1.getPoint().getX() - s2.getPoint().getX(), 2.0);
		double posY = Math.pow(s1.getPoint().getY() - s2.getPoint().getY(), 2.0);
		double result = Math.sqrt(Math.abs(posX + posY));
		return result;
	}

	/**
	 * Finds the quickest travel route from station @param startString to
	 * station @param destString taking in to consideration bus departure times and
	 * heuristics based on euclidian distance converted into minutes it takes to
	 * travel the euclidian distance. All stations are placed in a 2D coordinate
	 * space with X and Y axis.
	 * 
	 * @param hour,
	 *            the hour you wish to depart, must be between 00 and 23 otherwise an IllegalArgumentException is thrown.
	 * @param min,
	 *            the minute you wish to depart, must be between 00 and 59 otherwise an IllegalArgumentException is thrown.
	 * @param startString,
	 *            name of station to depart from.
	 * @param destString,
	 *            name of station you wish to travel to.
	 * @return a list of connections containing information about transportation and
	 *         arrival times of quickest travel route from start to destination 
	 *         if start and destination is connected in anyway either
	 *         directly or with intermediate stations. If no valid route exists or
	 *         either of the stations do not exist in the graph an empty list is
	 *         returned. 
	 */

	public List<Connection> aStar(int hour, int min, String startString, String destString) {
		if (!containsStations(startString, destString)) {
			return new LinkedList<Connection>();
		}
		PriorityQueue<Station> stationQ = new PriorityQueue<>();
		Queue<Connection> connectionQ = new LinkedList<>();
		HashSet<Station> known = new HashSet<>();
		Station currentStation = stations.get(startString);
		Station dest = stations.get(destString);
		currentStation.setArrivalInTime(new Time(hour, min));
		currentStation.setF(0);
		stationQ.add(currentStation);

		while (!stationQ.isEmpty()) {
			currentStation = stationQ.remove();
			
			if (currentStation == dest) {
				break;
			}

			known.add(currentStation);
			connectionQ.addAll(currentStation.getCon());

			while (!connectionQ.isEmpty()) {
				Connection currentConnection = connectionQ.remove();
				Station toStation = currentConnection.getS2();

				if (!known.contains(toStation)) {
					double totalMinTilDeparture = currentConnection.calculateTime(currentStation.getBestArrival(),
							currentConnection.getDeparture());

					double cost = totalMinTilDeparture
							+ currentConnection.getTransport().getTravelTime(currentConnection.getCost())
							+ currentStation.getArrivalInMin();
					double h = getHeuristics(toStation, dest, currentConnection.getTransport());

					double f = h + cost;

					if (toStation.getBestArrival() == null) {
						toStation.setArrivalInMin((int) cost);
						toStation.setF(f);
						toStation.setPath(currentStation);

						toStation.setBestConnection(currentConnection);
						stationQ.add(toStation);
					} else if (cost < toStation.getArrivalInMin()) {
						toStation.setArrivalInMin((int) cost);
						toStation.setF(f);
						toStation.setPath(currentStation);

						toStation.setBestConnection(currentConnection);
						stationQ.add(toStation);

					}

				}

			}

		}
		return getListOfConnections(dest);

	}

	private List<Connection> getListOfConnections(Station dest) {
		LinkedList<Connection> list = new LinkedList<>();
		while (dest.getBestConnection() != null) {
			list.addFirst(dest.getBestConnection());
			dest = dest.getBestConnection().getS1();
		}
		return list;
	}

	public void printAstarEdge(Station start, Station dest) {

		if (dest.getBestConnection() != null) {
			if (dest.getBestConnection().getS1() != start) {

				printAstarEdge(start, dest.getBestConnection().getS1());

			}

			System.out.print(dest.getBestConnection() + " Arrival: " + dest.getBestArrival());
		} else {
			System.out.println("No connection");
		}
	}

	public void printAstarNode(Station start, Station dest) {
		Station current = dest;
		if (current != start) {

			printAstarNode(start, current.getPath());
			System.out.print(" to ");
		}
		System.out.print(" " + current.getName() + current.getBestConnection());
	}

	private double getHeuristics(Station current, Station dest, Bus b) {
		double posX = Math.pow(current.getPoint().getX() - dest.getPoint().getX(), 2.0);
		double posY = Math.pow(current.getPoint().getY() - dest.getPoint().getY(), 2.0);
		double result = Math.sqrt(Math.abs(posX + posY));
		return b.getTravelTime(result);
	}

	public static String hoursAndMinutes(int number) {
		return "" + number / 60 + "" + number % 60;
	}

	public static void main(String[] args) {
		Bus b667 = new Bus(667, 100);
		Bus b770 = new Bus(770, 100);
		Bus b990 = new Bus(990, 100);

		AStarGraph g = new AStarGraph();
		g.add("IronForge", 100, 100);
		g.add("StormWind", 150, 150);
		g.add("Orgrimmar", 200, 200);
		g.add("Darnassus", 100, 300);
		g.add("Undercity", 400, 100);
		g.add("ThunderBluff", 400, 150);
		g.connect("IronForge", "StormWind", b667, 12, 20);
		g.connect("IronForge", "Orgrimmar", b770, 12, 00);
		g.connect("StormWind", "Orgrimmar", b667, 12, 05);
		g.connect("IronForge", "ThunderBluff", b990, 12, 02);
		g.connect("ThunderBluff", "Undercity", b990, 12, 02);
		g.connect("Orgrimmar", "Undercity", b770, 12, 00);
		g.connect("StormWind", "Darnassus", b667, 12, 30);
		g.connect("IronForge", "Darnassus", b770, 12, 25);

		

		List<Connection> connections = g.aStar(14, 00, "Undercity", "Darnassus");
		for (Connection c : connections) {
			System.out.print(c + " " + c.getCost());
		}

	}

}
