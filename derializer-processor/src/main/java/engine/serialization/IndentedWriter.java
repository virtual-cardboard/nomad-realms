package engine.serialization;

import java.io.PrintWriter;

public class IndentedWriter implements AutoCloseable {

	private final PrintWriter out;
	private int indentLevel = 0;
	private boolean atStartOfLine = true;

	public IndentedWriter(PrintWriter out) {
		this.out = out;
	}

	public void indent() {
		indentLevel++;
	}

	public void unindent() {
		if (indentLevel > 0) {
			indentLevel--;
		}
	}

	public void println(String s) {
		print(s);
		println();
	}

	public void println() {
		out.println();
		atStartOfLine = true;
	}

	public void print(String s) {
		String[] lines = s.split("\n", -1);
		for (int i = 0; i < lines.length; i++) {
			if (atStartOfLine && !lines[i].isEmpty()) {
				for (int j = 0; j < indentLevel; j++) {
					out.print("\t");
				}
				atStartOfLine = false;
			}
			out.print(lines[i]);
			if (i < lines.length - 1) {
				out.println();
				atStartOfLine = true;
			}
		}
	}

	@Override
	public void close() {
		out.close();
	}

}
