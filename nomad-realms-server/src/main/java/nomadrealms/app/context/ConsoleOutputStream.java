package nomadrealms.app.context;

import nomadrealms.render.ui.custom.console.Console;
import java.io.IOException;
import java.io.OutputStream;

public class ConsoleOutputStream extends OutputStream {

	private final Console console;
	private final StringBuilder buffer = new StringBuilder();

	public ConsoleOutputStream(Console console) {
		this.console = console;
	}

	@Override
	public synchronized void write(int b) throws IOException {
		if (b == '\n') {
			console.println(buffer.toString());
			buffer.setLength(0);
		} else if (b == '\r') {
			// ignore
		} else {
			buffer.append((char) b);
		}
	}

}
