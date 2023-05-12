import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Interpreter {
	static MemoryElement[] memory = new MemoryElement[40];
	static int start = 10;
	static Queue<PCB> readyQueue = new LinkedList<>();
	static Scanner sc = new Scanner(System.in);
	
	
	public static void printMemory() 
	{
		for (int i = 0; i < 40; i++) {
			System.out.println(i);
			System.out.println(memory[i]);
		}
	}
	
	public static void createProcess(String fileName) {
		try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            PCB process1 = new PCB(start + 1);
            memory[start] = process1;
            int len = 0;
            //store variables
            for (int i = 0; i < 3; i ++) {
            	len++;
            	memory[++start] = new Variable("null");
            }
            while (line != null) {
            	len++;
            	Instruction t = new Instruction(line);
            	memory[++start] = t;
                //System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
            process1.setLimit(len);
            readyQueue.add(process1);
            start++;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
	}
	
	public static void executeInstruction (String inst,PCB x) {
		//print, assign, writeFile, readFile, printFromTo
		StringTokenizer st = new StringTokenizer(inst);
		String instName = st.nextToken();
		
		if (instName.equals("assign")) {
			String name = st.nextToken();
			String value = st.nextToken();
			if (value.equals("readFile")) {
				String last = st.nextToken();
				String fileName = "";
				for(int i = 0 ; i < 3 ; i++) {
					if(((Variable)(memory[x.getBase() + i])).getName().equals(last)) {
						fileName = ((Variable)memory[x.getBase() + i]).getValue();
						break;
					}
				}
				value = readFile(fileName);
			}
			Variable var = assign(name, value);
			for(int i = 0 ; i < 3 ; i++) {
				if(((Variable)(memory[x.getBase() + i])).getName().equals("null")
						|| ((Variable)(memory[x.getBase() + i])).getName().equals(var.getName())) {
					memory[x.getBase() + i] = var;
					break;
				}
			}
		}
		else if (instName.equals("print")) {
			String variable = st.nextToken();
			String content = "";
			for(int i = 0 ; i < 3 ; i++) {
				if(((Variable)(memory[x.getBase() + i])).getName().equals(variable)) {
					content = ((Variable)memory[x.getBase() + i]).getValue();
					break;
				}
			}
			print(content);
		}
		
		x.incrementPC();
	}
	
	public static void print (String string) {
		System.out.println(string);
	}
	
	public static Variable assign (String x, String y) {
		if (y.equals("input")) {
			System.out.println("Please enter a value: ");
			y = sc.nextLine();
		}
		Variable temp = new Variable(x);
		temp.setVal(y);
		return temp;
	}
	
	public static void writeFile () {
		
	}
	
	public static String readFile (String fileName) {
		String content = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			StringBuilder stringBuilder = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append("\n");
	        }
	        reader.close();
	        content = stringBuilder.toString();
		}
		catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
		return content;
	}
	
	public static void printFromTo () {
		
	}
	public static void main (String[] args) {
		createProcess("Program_1.txt");
		createProcess("Program_2.txt");
		createProcess("Program_3.txt");
		printMemory();
		executeInstruction(((Instruction)memory[28]).getInstruction(), (PCB)memory[24]);
		executeInstruction(((Instruction)memory[29]).getInstruction(), (PCB)memory[24]);
		executeInstruction(((Instruction)memory[30]).getInstruction(), (PCB)memory[24]);
		printMemory();
	//	System.out.println(readFile("Program_2.txt"));
	}
}
