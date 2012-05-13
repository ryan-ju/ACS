/**
 * 
 */
package com.asys.views;

import java.awt.Component;
import java.text.ParseException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.asys.constants.CommandName;
import com.asys.constants.Constant;
import com.asys.constants.ElementPropertyKey;
import com.asys.editor.model.Command;
import com.asys.editor.model.Element;
import com.asys.editor.model.Executor;
import com.asys.editor.model.Property;
import com.asys.editor.model.SelectionManager;
import com.asys.editor.model.SelectionManagerListener;
import com.asys.model.components.exceptions.InvalidPropertyKeyException;

/**
 * @author ryan
 * 
 */
public class PropertyViewer extends JScrollPane {
	JTable table;
	PropertyTableModel ptm;

	public PropertyViewer() {
		init();
	}

	private void init() {
		ptm = new PropertyTableModel();
		table = new JTable(ptm);
		PropertyTableCellEditor cell_editor = new PropertyTableCellEditor();
		SelectionManager.getInstance().addListener(ptm);
		table.setDefaultRenderer(Object.class, cell_editor);
		table.setDefaultEditor(Object.class, cell_editor);
		this.getViewport().add(table);
	}

	public PropertyTableModel getPropertyTableModel() {
		assert ptm != null;
		return ptm;
	}

	class PropertyTableModel extends AbstractTableModel implements
			SelectionManagerListener {
		ArrayList<Entry<ElementPropertyKey, Object>> list;
		boolean[] editabilityMap;
		Element elt;

		public PropertyTableModel() {
			list = new ArrayList<Entry<ElementPropertyKey, Object>>();
		}

		@Override
		public int getRowCount() {
			return list.size();
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int col) {
			if (col == 0)
				return "Property";
			if (col == 1)
				return "Value";
			return null;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return list.get(rowIndex);
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (col == 0) {
				return false;
			} else if (col == 1) {
				return editabilityMap[row];
			} else {
				return false;
			}
		}

		@Override
		public void update() {
			SelectionManager sm = SelectionManager.getInstance();
			list.clear();
			if (sm.getGroupManager().getElementSize() == 1) {
				elt = sm.getGroupManager().getElements().get(0);
				Property prop = elt.getProperty();
				editabilityMap = new boolean[4];
				try {
					list.add(new SimpleEntry<ElementPropertyKey, Object>(
							ElementPropertyKey.NUM_INPORT, prop
									.getProperty(ElementPropertyKey.NUM_INPORT)));
					editabilityMap[0] = elt.canChangeNumIPs();
					list.add(new SimpleEntry<ElementPropertyKey, Object>(
							ElementPropertyKey.NUM_OUTPORT,
							prop.getProperty(ElementPropertyKey.NUM_OUTPORT)));
					editabilityMap[1] = elt.canChangeNumOPs();
					list.add(new SimpleEntry<ElementPropertyKey, Object>(
							ElementPropertyKey.MIN_DELAY, prop
									.getProperty(ElementPropertyKey.MIN_DELAY)));
					editabilityMap[2] = true;
					list.add(new SimpleEntry<ElementPropertyKey, Object>(
							ElementPropertyKey.MAX_DELAY, prop
									.getProperty(ElementPropertyKey.MAX_DELAY)));
					editabilityMap[3] = true;
				} catch (InvalidPropertyKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.fireTableDataChanged();
		}
	}

	class PropertyTableCellEditor extends AbstractCellEditor implements
			TableCellEditor, TableCellRenderer {
		PropertyEditorTextField petf = null;

		public PropertyTableCellEditor() {
			
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			SimpleEntry<ElementPropertyKey, Object> entry = (SimpleEntry<ElementPropertyKey, Object>) value;
			if (column == 0) {
				JTextField label_tf = new JTextField();
				switch (entry.getKey()) {
				case NUM_INPORT:
					label_tf.setText("Number of inports");
					break;
				case NUM_OUTPORT:
					label_tf.setText("Number of outports");
					break;
				case MIN_DELAY:
					label_tf.setText("Minimum delay");
					break;
				case MAX_DELAY:
					label_tf.setText("Maximum delay");
					break;
				}
				if (isSelected) {
					label_tf.setBackground(Constant.PROPERTY_VIEWER_LABEL_FOCUS_BG_CLR);
				} else {
					label_tf.setBackground(Constant.PROPERTY_VIEWER_LABEL_NORMAL_BG_CLR);
				}
				label_tf.setBorder(BorderFactory.createEmptyBorder());
				return label_tf;
			} else if (column == 1) {
				PropertyViewerTextField pvtf = new PropertyViewerTextField(
						entry);
				if (isSelected) {
					pvtf.setBackground(Constant.PROPERTY_VIEWER_TEXT_FOCUS_BG_CLR);
				} else {
					if (ptm.isCellEditable(row, column)) {
						pvtf.setBackground(Constant.PROPERTY_VIEWER_TEXT_EDITABLE_NORMAL_BG_CLR);
					} else {
						pvtf.setBackground(Constant.PROPERTY_VIEWER_TEXT_INEDITABLE_NORMAL_BG_CLR);
					}
				}
				return pvtf;
			} else {
				return null;
			}
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (petf == null) {
				petf = new PropertyEditorTextField((SimpleEntry<ElementPropertyKey, Object>) value);
				this.addCellEditorListener(petf);
			} else {
				petf.setValue((SimpleEntry<ElementPropertyKey, Object>) value);
			}
			return petf;
		}

	}

	class PropertyViewerTextField extends JFormattedTextField {

		PropertyViewerTextField(Object value) {
			super(new ViewerFormatterFactory(), value);
			this.setBorder(BorderFactory.createEmptyBorder());
		}

	}

	class ViewerFormatterFactory extends AbstractFormatterFactory {

		@Override
		public AbstractFormatter getFormatter(JFormattedTextField tf) {
			return new ViewerFormatter();
		}

		/**
		 * This formatter converts Entry<ElementPropertyKey, Object> to String.
		 * The String returned is determined by the value of the Object and the
		 * sub type of ElementPropertyType.
		 * 
		 * @author ryan
		 * 
		 */
		class ViewerFormatter extends AbstractFormatter {

			@Override
			public Object stringToValue(String text) throws ParseException {
				return null;
			}

			@Override
			public String valueToString(Object value) throws ParseException {
				Entry<ElementPropertyKey, Object> entry = (Entry<ElementPropertyKey, Object>) value;
				switch (entry.getKey()) {
				case NUM_INPORT:
					return Integer.toString((Integer) entry.getValue());
				case NUM_OUTPORT:
					return Integer.toString((Integer) entry.getValue());
				case MIN_DELAY:
					return convert((Long) entry.getValue());
				case MAX_DELAY:
					return convert((Long) entry.getValue());
				}
				return null;
			}

			/**
			 * Convert time to nanoseconds
			 * 
			 * @param time
			 *            - in picoseconds
			 * @return
			 */
			private String convert(long time) {
				String time_str = Long.toString(time);
				if (time_str.length() > 3) {
					String int_part = time_str.substring(0,
							time_str.length() - 3);
					String dec_part = time_str.substring(time_str.length() - 3);
					return int_part + "." + dec_part + "ns";
				} else {
					String full_time_str = "00" + time_str;
					return "0."
							+ full_time_str
									.substring(full_time_str.length() - 3)
							+ "ns";
				}
			}

		}
	}

	class PropertyEditorTextField extends JFormattedTextField implements
			CellEditorListener {

		public PropertyEditorTextField(Entry<ElementPropertyKey, Object> entry) {
			super(new EditorFormatterFactory(), entry);
		}

		public Entry<ElementPropertyKey, Object> getLocalValue() {
			return (Entry<ElementPropertyKey, Object>) this.getValue();
		}

		@Override
		public void editingStopped(ChangeEvent e) {
			ElementPropertyKey editing_key = ((Entry<ElementPropertyKey, Object>) this
					.getValue()).getKey();
			String str = this.getText();
			int num;
			Command cmd;
			switch (editing_key) {
			case NUM_INPORT:
				num = Integer.parseInt(str);
				cmd = Command.getInstance();
				cmd.setCommandName(CommandName.CHANGE_NUMBER_OF_INPORT);
				cmd.setParams(new Object[] { num });
				Executor.getInstance().execute(cmd);
				break;
			case NUM_OUTPORT:
				num = Integer.parseInt(str);
				cmd = Command.getInstance();
				cmd.setCommandName(CommandName.CHANGE_NUMBER_OF_OUTPORT);
				cmd.setParams(new Object[] { num });
				Executor.getInstance().execute(cmd);
				break;
			case MIN_DELAY:
				break;
			case MAX_DELAY:
				break;
			}
			ptm.update();
		}

		@Override
		public void editingCanceled(ChangeEvent e) {
			// Do nothing
		}
	}

	class EditorFormatterFactory extends AbstractFormatterFactory {

		@Override
		public AbstractFormatter getFormatter(JFormattedTextField tf) {
			Entry<ElementPropertyKey, Object> entry = ((PropertyEditorTextField) tf)
					.getLocalValue();
			if (entry.getValue() instanceof Integer) {
				return new IntegerEditorFormatter();
			} else if (entry.getValue() instanceof Long) {
				return new LongEditorFormatter();
			} else {
				return null;
			}
		}

		class IntegerEditorFormatter extends AbstractFormatter {

			@Override
			public Object stringToValue(String text) throws ParseException {
				PropertyEditorTextField petf = (PropertyEditorTextField) this.getFormattedTextField();
				Entry<ElementPropertyKey, Object> entry = petf.getLocalValue();
				try{
					int i = Integer.parseInt(text);
					entry.setValue(i);
				}catch (NumberFormatException e){
					throw new ParseException("Cannot parse int \""+text+"\"", 0);
				}
				return entry;
			}

			@Override
			public String valueToString(Object value) throws ParseException {
				return ((Entry<ElementPropertyKey, Object>) value).getValue()
						.toString();
			}

		}

		class LongEditorFormatter extends AbstractFormatter {

			@Override
			public Object stringToValue(String text) throws ParseException {
				PropertyEditorTextField petf = (PropertyEditorTextField) this.getFormattedTextField();
				Entry<ElementPropertyKey, Object> entry = petf.getLocalValue();
				try{
					long i = Long.parseLong(text);
					entry.setValue(i);
				}catch (NumberFormatException e){
					throw new ParseException("Cannot parse long \""+text+"\"", 0);
				}
				return entry;
			}

			@Override
			public String valueToString(Object value) throws ParseException {
				return ((Entry<ElementPropertyKey, Object>) value).getValue()
						.toString();
			}

		}

	}

}
