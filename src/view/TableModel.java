package view;

import javax.swing.table.AbstractTableModel;

import controller.Controller;

public class TableModel extends AbstractTableModel {
	private Controller controller;
	private String[] colNames = { "id", "title", "author", "isbn", "genre" };
	

	// do I have to pass controller from maiframe() through booklistpanel() ?
	public TableModel(Controller controller) {
		this.controller = controller;
	}

	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public int getRowCount() {
		return controller.getDbSize();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex > 0) {
			return true ;
		}
		return false;
	}
	
	public void setValueAt(String value, int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			break;
		case 1:
			controller.getBook(rowIndex).setTitle(value);
			break;
		case 2:
			controller.getBook(rowIndex).setAuthor(value.toString());
			break;
		case 3:
			controller.getBook(rowIndex).setIsbn(new Integer(value.toString()));
			break;
		case 4:
			controller.getBook(rowIndex).setGenre(value.toString());
			break;
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return controller.getBook(rowIndex).getId();
		case 1:
			return controller.getBook(rowIndex).getTitle();
		case 2:
			return controller.getBook(rowIndex).getAuthor();
		case 3:
			return controller.getBook(rowIndex).getIsbn();
		case 4:
			return controller.getBook(rowIndex).getGenre();
		}
		return null;
	}
}
