import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TestUnmodifiableList {
	public static void main(String[] args) {
		// create array list
		List<Character> list = new ArrayList<Character>();
		List<Character> listTemp = new ArrayList<Character>();
		
		listTemp.add('u');

		// populate the list
		list.add('X');
		list.add('Y');

		System.out.println("Initial list: " + list);

		// make the list unmodifiable
		List<Character> immutablelist = Collections.unmodifiableList(list);
		
		
		//returns collection of list of characters
		Collection<List<Character>> unmodifiableCollection = Collections.unmodifiableCollection(Arrays.asList(list));

		// try to modify the list
		//immutablelist.add('Z');
		System.out.println("Immutablelist: " + immutablelist);
		
		unmodifiableCollection.add(listTemp);
		
		System.out.println(unmodifiableCollection);
		list.set(0, 'C');
		System.out.println(list);
	}
}
