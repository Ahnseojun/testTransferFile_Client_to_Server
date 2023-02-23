package testTransferFile_Only_put_Choose;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ReceiveServer {

	public static void main(String[] args) throws SocketException {
		System.out.println("<<Server>>");
		ServerSocket serverSocket = null;
		Socket socket = null;
		int port = 8888;

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("서버가 시작되었습니다");

			// 클라이언트 대기
			while (true) {
				System.out.println("새로운 Client의 연결 요청을 기다립니다");
				// 연결되면 통신용 소켓 생성
				socket = serverSocket.accept();
				System.out.println("Client Connected : " + socket.getRemoteSocketAddress());

				Thread receiver = new Thread(new Receiver(socket));
				receiver.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
