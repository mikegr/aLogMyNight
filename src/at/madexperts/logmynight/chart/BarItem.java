package at.madexperts.logmynight.chart;

public class BarItem implements Comparable<BarItem> {
	private String name;
	private int amount;

	public BarItem(String name, int amount) {
		super();
		this.name = name;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int compareTo(BarItem arg0) {
		if(amount == arg0.getAmount())
			return 0;
		else if(amount > arg0.getAmount())
			return -1;
		else
			return 1;
	}

}
