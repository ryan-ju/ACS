/**
 * 
 */
package com.asys.editor.model;

/**
 * @author ryan
 *
 */
public interface ElementVisitor {
	public void visit(InputGate input);
	public void visit(OutputGate output);
	public void visit(EnvironmentGate env);
	public void visit(NotGate not);
	public void visit(AndGate and);
	public void visit(OrGate or);
	public void visit(NandGate nand);
	public void visit(NorGate nor);
	public void visit(XorGate xor);
	public void visit(CGate c);
	public void visit(Fanout fanout);
}
