package parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import components.Node;
import devices.BJT;
import devices.Mosfet;

public class Parser {

	public static void main(String[] args) throws IOException {
		Parser p = new Parser();
		String inputFile = "Circuit.ckt";
		InputStream is = System.in;
		if (inputFile != null) {
			is = new FileInputStream(inputFile);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		try {
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				if (!line.startsWith("*") && line.length()>0) {
					String[] fields = line.split(" ");
					p.putComponents(fields);
				}
			}
		} finally {
			reader.close();
		}
	}

	// Get the component in the form of a graph

	public void putComponents(String[] tokens) throws FileNotFoundException {
		List<Node> list = new LinkedList<>();
		GraphCircuit g = new GraphCircuit();
		Constraint c = new Constraint();
		switch (tokens[0].charAt(0)) {
		case 'M':
		case 'm':
			Mosfet m = new Mosfet(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),
					Integer.parseInt(tokens[3]));
			m.setTerminal();
			list.add(m);
			break;
		case 'Q':
		case 'q':
			BJT b = new BJT(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),
					Integer.parseInt(tokens[3]));
			list.add(b);
			break;
			
		case 'V':
		case 'v':
		case 'I':
		case 'i':
		case '.':
		case '+': break;
		default:
			System.out.println("Invalid device");
		}
		g.createGraph(list);
		c.toFile(c.getRuleBook());
	}

}
