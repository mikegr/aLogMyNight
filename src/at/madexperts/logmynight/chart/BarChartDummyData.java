/*
 	LogMyNight - Android app for logging night activities. 
 	Copyright (c) 2010 Michael Greifeneder <mikegr@gmx.net>, Oliver Selinger <oliver.selinger@gmail.com>
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
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
