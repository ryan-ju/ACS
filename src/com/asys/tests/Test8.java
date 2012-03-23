/**
 * 
 */
package com.asys.tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author ryan
 * 
 */
public class Test8 extends JFrame {
	MyTableModel model;

	public Test8() {
		model = new MyTableModel();
		JTable table = new JTable(model);
		MyCellEditor editor = new MyCellEditor();
		table.setDefaultRenderer(String.class, editor);
		table.setDefaultEditor(String.class, editor);
		table.setRowHeight(20);
		this.add(table.getTableHeader(), BorderLayout.WEST);
		this.add(table, BorderLayout.CENTER);

		this.setSize(400, 400);
	}

	class MyTableModel extends AbstractTableModel {
		String[][] data = new String[10][2];
		String[] col_name = new String[] { "Property", "Value" };

		MyTableModel() {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 2; j++) {
					if (j == 0) {
						data[i][j] = "Property " + i;
					} else {
						data[i][j] = "Value " + i;
					}
				}
			}
		}

		@Override
		public int getRowCount() {
			return 10;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int col) {
			return col_name[col];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			if (col == 1) {
				data[row][col] = (String) value;
			}
		}
		
		@Override
		public Class getColumnClass(int col){
			return String.class;
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (col == 1)
				return true;
			else
				return false;
		}

	}

	class MyCellEditor extends AbstractCellEditor implements TableCellEditor,
			TableCellRenderer {
		EditorTextField etf;

		public MyCellEditor() {
			etf = new EditorTextField(0, 0);
			this.addCellEditorListener(etf);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (isSelected) {
				if (column == 0) {
					JLabel btn = new JLabel((String) value);
					btn.setBackground(Color.ORANGE);
					return btn;
				} else {
					JTextField tf = new JTextField((String) value);
					tf.setBackground(Color.LIGHT_GRAY);
					return tf;
				}
			} else {
				if (column == 0) {
					JLabel btn = new JLabel((String) value);
					return btn;
				} else {
					JTextField tf = new JTextField((String) value);
					return tf;
				}
			}
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (column == 1) {
				etf.row = row;
				etf.column = column;
				etf.setText((String) model.getValueAt(row, column));
				return etf;
			} else {
				return null;
			}
		}
	}

	class EditorTextField extends JTextField implements CellEditorListener {
		int row, column;

		public EditorTextField(int x, int y) {
			this.row = x;
			this.column = y;
		}

		@Override
		public void editingStopped(ChangeEvent e) {
			String text = this.getText();
			System.out.println(text);
			model.setValueAt(text, row, column);
		}

		@Override
		public void editingCanceled(ChangeEvent e) {

		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				(new Test8()).setVisible(true);
			}

		});
	}

}
