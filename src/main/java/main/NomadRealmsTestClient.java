package main;

import java.io.IOException;

import process.DirectHolePunchProcess;

public class NomadRealmsTestClient {

	public static void main(String[] args) throws IOException {
//		new STUNProcess().start();
//		new HolePunchProcess().start();
		new DirectHolePunchProcess().start();
	}

}
