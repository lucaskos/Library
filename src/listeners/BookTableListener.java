package listeners;

import java.util.ArrayList;

public interface BookTableListener {
	public void rowDeleted(int row);
	public void deletedRows(ArrayList<String> removedItems);
}
