/**
 * 
 */
package com.asys.views;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.asys.constants.Constant;
import com.asys.constants.ImageFetcher;
import com.asys.editor.model.AndGate;
import com.asys.editor.model.CGate;
import com.asys.editor.model.Element;
import com.asys.editor.model.ElementCreationManager;
import com.asys.editor.model.EnvironmentGate;
import com.asys.editor.model.InputGate;
import com.asys.editor.model.NandGate;
import com.asys.editor.model.NorGate;
import com.asys.editor.model.NotGate;
import com.asys.editor.model.OrGate;
import com.asys.editor.model.OutputGate;
import com.asys.editor.model.XorGate;

/**
 * @author ryan
 *
 */
public class Repository extends JPanel {
	
	public Repository(){
		init();
	}
	private void init(){
		this.setLayout(new GridLayout(4,3));
		ImageFetcher.ElementImageFetcher eif = new ImageFetcher.ElementImageFetcher();
		InputGate input = new InputGate();
		this.add(new GateButton(eif.getImage(input), input));
		OutputGate output = new OutputGate();
		this.add(new GateButton(eif.getImage(output), output));
		EnvironmentGate env = new EnvironmentGate();
		this.add(new GateButton(eif.getImage(env), env));
		NotGate not = new NotGate();
		this.add(new GateButton(eif.getImage(not), not));
		AndGate and = new AndGate();
		this.add(new GateButton(eif.getImage(and), and));
		OrGate or = new OrGate();
		this.add(new GateButton(eif.getImage(or), or));
		NandGate nand = new NandGate();
		this.add(new GateButton(eif.getImage(nand), nand));
		NorGate nor = new NorGate();
		this.add(new GateButton(eif.getImage(nor), nor));
		XorGate xor = new XorGate();
		this.add(new GateButton(eif.getImage(xor), xor));
		CGate c = new CGate();
		this.add(new GateButton(eif.getImage(c), c));
	}
	
	class GateButton extends JButton implements ActionListener{
		BufferedImage buf;
		Element elt;
		
		GateButton(BufferedImage buf, Element elt){
			this.setBackground(Constant.GATE_BUTTON_BACKGROUND_CLR);
			this.buf = buf;
			this.elt = elt;
			this.setIcon(new ImageIcon(buf));
			this.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			ElementCreationManager.getInstance().setCreation(elt.copy());
			ModeManager.getInstance().setMode(Mode.ELEMENT_CREATION_MODE);
		}
	}
}
