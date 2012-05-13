/**
 * 
 */
package com.asys.simulator;

/**
 * @author ryan
 *
 */
public abstract class GatePort {
	private final String gate_port_id;
	private final String gate_id;
	private final int port_index;
	
	
	public GatePort(String gate_port_id, String gate_id, int port_index){
		this.gate_port_id=gate_port_id;
		this.gate_id=gate_id;
		this.port_index=port_index;
	}
	
	public String getGatePortId(){
		return gate_port_id;
	}
	
	public String getGateId(){
		return gate_id;
	}
	
	public int getPortIndex(){
		return port_index;
	}
	
	public abstract GatePortType getGatePortType();
	
	class GateInport extends GatePort{
		
		public GateInport(String gate_port_id, String gate_id, int port_index) {
			super(gate_port_id, gate_id, port_index);
		}

		@Override
		public final GatePortType getGatePortType() {
			return GatePortType.GATE_INPORT;
		}
		
		
	}
	
	class GateOutport extends GatePort{

		public GateOutport(String gate_port_id, String gate_id, int port_index) {
			super(gate_port_id, gate_id, port_index);
		}

		@Override
		public final GatePortType getGatePortType() {
			return GatePortType.GATE_OUTPORT;
		}
		
	}
}
