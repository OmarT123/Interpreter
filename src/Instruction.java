
public class Instruction implements MemoryElement{
	private String inst;
	public Instruction (String inst) {
		this.inst = inst;
	}
	public String toString() {
		return "Instruction: " + inst;
	}
	public String getInstruction() {
		return inst;
	}
}
