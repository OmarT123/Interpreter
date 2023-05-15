import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Interpreter {
	static MemoryElement[] memory = new MemoryElement[40];
	static int start = 0;
	static Queue<PCB> readyQueue = new LinkedList<>();
	static Scanner sc = new Scanner(System.in);

	public static void Scheduler() {
		while (!readyQueue.isEmpty()) {
			PCB currentProcess = readyQueue.remove();
			for (int i = 0; i < 2
					&& currentProcess.getPC() < (currentProcess.getBase() + currentProcess.getLimit()); i++)
				executeInstruction(((Instruction) memory[currentProcess.getPC()]).getInstruction(), currentProcess);
			if (currentProcess.getPC() < currentProcess.getBase() + currentProcess.getLimit()) {
				currentProcess.setState(State.READY);
				readyQueue.add(currentProcess);
				readyQueue.peek().setState(State.RUNNING);
			} else {
				removeProcess(currentProcess);
				if (!readyQueue.isEmpty())
					readyQueue.peek().setState(State.RUNNING);
			}
			printQueue();
		}
	}
	public static void removeProcess(PCB process) {
		process.setState(State.FINISHED);
		for (int i = process.getBase(); i < process.getBase() + process.getLimit(); i++) {
			memory[i] = null;
		}
		memory[process.getBase() - 1] = null;
	}
	public static void printQueue() {
		Queue<PCB> temp = new LinkedList<>();
		while (!readyQueue.isEmpty()) {
			temp.add(readyQueue.peek());
			System.out.println(readyQueue.remove());
		}
		while (!temp.isEmpty())
			readyQueue.add(temp.remove());
		System.out.println("------------");
	}

	public static void printMemory() {
		System.out.println("-----------------------------");
		for (int i = 0; i < 40; i++) {
			System.out.println(i);
			System.out.println(memory[i]);
		}
		System.out.println("-----------------------------");
	}

	public static void createProcess(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();
			PCB process1 = new PCB(start + 1);
			memory[start] = process1;
			int len = 0;
			// store variables
			for (int i = 0; i < 3; i++) {
				len++;
				memory[++start] = new Variable("null");
			}
			while (line != null) {
				len++;
				Instruction t = new Instruction(line);
				memory[++start] = t;
				// System.out.println(line);
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

	public static void executeInstruction(String inst, PCB x) {
		// print, assign, writeFile, readFile, printFromTo
		StringTokenizer st = new StringTokenizer(inst);
		String instName = st.nextToken();

		if (instName.equals("assign")) {
			String name = st.nextToken();
			String value = st.nextToken();
			if (value.equals("readFile")) {
				String last = st.nextToken();
				String fileName = "";
				for (int i = 0; i < 3; i++) {
					if (((Variable) (memory[x.getBase() + i])).getName().equals(last)) {
						fileName = ((Variable) memory[x.getBase() + i]).getValue();
						break;
					}
				}
				value = readFile(fileName);
			}
			Variable var = assign(name, value);
			for (int i = 0; i < 3; i++) {
				if (((Variable) (memory[x.getBase() + i])).getName().equals("null")
						|| ((Variable) (memory[x.getBase() + i])).getName().equals(var.getName())) {
					memory[x.getBase() + i] = var;
					break;
				}
			}
		} else if (instName.equals("print")) {
			String variable = st.nextToken();
			String content = "";
			for (int i = 0; i < 3; i++) {
				if (((Variable) (memory[x.getBase() + i])).getName().equals(variable)) {
					content = ((Variable) memory[x.getBase() + i]).getValue();
					break;
				}
			}
			print(content);
		} else if (instName.equals("printFromTo")) {
			String first = st.nextToken();
			String second = st.nextToken();
			for (int i = 0; i < 3; i++) {
				if (((Variable) (memory[x.getBase() + i])).getName().equals(first))
					first = ((Variable) memory[x.getBase() + i]).getValue();
				else if (((Variable) (memory[x.getBase() + i])).getName().equals(second))
					second = ((Variable) memory[x.getBase() + i]).getValue();
			}
			printFromTo(first, second);
		} else if (instName.equals("writeFile")) {
			String first = st.nextToken();
			String second = st.nextToken();
			for (int i = 0; i < 3; i++) {
				if (((Variable) (memory[x.getBase() + i])).getName().equals(first))
					first = ((Variable) memory[x.getBase() + i]).getValue();
				else if (((Variable) (memory[x.getBase() + i])).getName().equals(second))
					second = ((Variable) memory[x.getBase() + i]).getValue();
			}
			writeFile(first, second);
		}
		x.incrementPC();
	}

	public static void print(String string) {
		System.out.println(string);
	}

	public static Variable assign(String x, String y) {
		if (y.equals("input")) {
			System.out.println("Please enter a value: ");
			y = sc.nextLine();
		}
		Variable temp = new Variable(x);
		temp.setVal(y);
		return temp;
	}

	public static void writeFile(String filename, String data) {
		try (FileWriter writer = new FileWriter(filename)) {
			writer.write(data);
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
	}

	public static String readFile(String fileName) {
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
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
		return content;
	}

	public static void printFromTo(String a, String b) {
		try {
			int x = Integer.parseInt(a);
			int y = Integer.parseInt(b);
			while (x <= y) {
				System.out.println(x++);
			}
		} catch (Exception e) {
			System.err.println("Error wrong input: " + e.getMessage());
		}
	}

	public static void main(String[] arsgs) {
		createProcess("Program_1.txt");
		createProcess("Program_2.txt");
		createProcess("Program_3.txt");
		Scheduler();
		printMemory();
//		executeInstruction(((Instruction)memory[11]).getInstruction(),(PCB)(memory[7]));
//		executeInstruction(((Instruction)memory[12]).getInstruction(),(PCB)(memory[7]));
//		executeInstruction(((Instruction)memory[13]).getInstruction(),(PCB)(memory[7]));
	}
}
