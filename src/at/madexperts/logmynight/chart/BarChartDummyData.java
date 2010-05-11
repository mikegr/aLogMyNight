package at.madexperts.logmynight.chart;

import java.util.ArrayList;
import java.util.List;

public class BarChartDummyData {
	
	public static List<BarItem> getTestData() {
		List<BarItem> items = new ArrayList<BarItem>();
		
		BarItem item1 = new BarItem("halbe Bier", 300);
		BarItem item2 = new BarItem("große Bier", 200);
		BarItem item3 = new BarItem("Mockito ASDF ASDf ASDF", 100);
		BarItem item4 = new BarItem("Schnaps", 50);
		BarItem item5 = new BarItem("Mineral", 20);
		BarItem item6 = new BarItem("Red Bull", 10);
		BarItem item7 = new BarItem("Cola Bull", 5);
		
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		items.add(item5);
		items.add(item6);
		items.add(item7);
		
		return items;
	}

}
