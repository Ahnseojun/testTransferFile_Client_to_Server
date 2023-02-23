package testTransferFile_Only_put_Choose;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SendClient {

	public static void main(String[] args) {
		// Socket Client 소스
		System.out.println("<<Client>>");

		String serverIp = "127.0.0.1"; // 루프백 용 ip
		int port = 8888;

		Socket socket = null;

		try {
			// 서버 연결
			socket = new Socket(serverIp, port);
			System.out.println("서버에 연결되었습니다");

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String serverFilePath = null;
			System.out.println("서버에 저장할 경로 입력 >>");
			serverFilePath = br.readLine();

			Thread fileSender = new Thread(new FileSender(socket, serverFilePath));
			fileSender.start();

		} catch (IOException e) {
			System.out.println("Client Error");
			e.printStackTrace();
			System.exit(0);
		}

	}

}
