import java.util.*;

public class PCB implements MemoryElement{
	static private int processCounter = 1;
	private int processID;
	private State state;
	private int PC;
	private int[] memoryBoundary; //base address, limit
	
	public PCB(int base) {
		processID = processCounter++;
		state = State.READY;
		memoryBoundary = new int[2];
		memoryBoundary[0] = base;
		PC = base + 3;
	}
	public void setLimit (int lim) {
		memoryBoundary[1] = lim;
	}
	public String toString() {
		return "My id: " + processID + " State: " + state + " PC: " + PC + " base: " + memoryBoundary[0] + " limit: " + memoryBoundary[1];
	}
	public void incrementPC() {
		PC++;
	}
	public int getBase() {
		return memoryBoundary[0];
	}
	public int getLimit() {
		return memoryBoundary[1];
	}
}
