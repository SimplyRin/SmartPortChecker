package net.simplyrin.smartportchecker;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.simplyrin.multiprocess.MultiProcess;
import net.simplyrin.rinstream.RinStream;

/**
 * Created by SimplyRin on 2019/08/29.
 *
 * Copyright (c) 2019 SimplyRin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class Main {

	public static void main(String[] args) {
		new RinStream();
		new Main().run();
	}

	public void run() {
		System.out.print("Enter IP: ");

		Scanner scanner = new Scanner(System.in);
		String ip = scanner.nextLine();
		scanner.close();

		MultiProcess multiProcess = new MultiProcess();
		List<Integer> openedPorts = new ArrayList<>();

		multiProcess.updateMaxThread(20480);

		// 1 -> 65535
		for (int integer = 1; integer <= 65535; integer++) {
			final int port = integer;

			multiProcess.addProcess(() -> {
				boolean open = this.isPortOpen(ip, port);
				if (open) {
					openedPorts.add(port);
				}
				System.out.println("IP: " + ip + ", Port: " + port + ", Open: " + open);
			});
		}

		multiProcess.setFinishedTask(() -> {
			System.out.println("Opened Ports");
			System.out.println("- " + openedPorts.toString());
		});
		multiProcess.start();
	}

	public boolean isPortOpen(String ip, int port) {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), 2500);
			socket.close();
			return true;
		} catch (Exception e) {
		}
		return false;
	}

}
