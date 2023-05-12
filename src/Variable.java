
public class Variable implements MemoryElement{
	String val;
	String name;
	
	public Variable (String name) {
		this.name = name;
	}
	public String toString() {
		return "Name: " + name + " Value: " + val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return val;
	}
}
