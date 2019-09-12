package alda.astar;

public class Time implements Comparable<Time> {
	private int h;
	private int min;

	public Time(int h, int min) {
		if((h < 0 || h >23)|| (min < 0 || min > 59)) {
			throw new IllegalArgumentException("Time must be correct!");
		}
		this.h = h;
		this.min = min;
	}

	public int compareTo(Time other) {

		if (h < other.h) {
			return -1;
		} else if (h > other.h) {
			return 1;
		} else {
			if (min < other.min) {
				return -1;
			} else if (min > other.min) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public int calculateTimeInMin() {
		return h * 60 + min;
	}

	public int getTimeDif(Time other) {
		int diff;
		if (min > other.min) {
			diff = Math.abs(60 - (min - other.min));
		} else {
			diff = other.min - min;
		}
		return diff;

	}

	public String toString() {
		if (h < 10 && min < 10) {
			return "0" + h + ":0" + min;
		} else if (h < 10) {
			return "0" + h + ":" + min;
		} else if (min < 10) {
			return h + ":0" + min;
		} else {
			return h + ":" + min;
		}
	}

}
