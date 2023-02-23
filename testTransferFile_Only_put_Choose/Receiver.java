package testTransferFile_Only_put_Choose;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Receiver implements Runnable {
	Socket socket;
	DataInputStream dis = null; // 바이트 기반 입력 스트림 -> readUTF 사용 가능
	FileOutputStream fos = null;
	BufferedOutputStream bos = null;

	String filePath;

	public Receiver(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			dis = new DataInputStream(socket.getInputStream());
			BufferedInputStream bis = new BufferedInputStream(dis); // bis가 while문에 오류가 나면 dis로 고치고 이 문장은 삭제

			filePath = dis.readUTF();

			try {
				System.out.println("파일 수신 작업");
				// 파일명을 전송 받고 파일명 수정
				String fileNm = dis.readUTF();
				System.out.println("파일명 : " + fileNm + "을 전송 받았습니다");

				File file = new File(filePath + "/" + fileNm);

				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				System.out.println(fileNm + "을 생성");

				byte[] data = new byte[1024];
				int len = 0;

				while ((len = bis.read(data)) != -1) {
					bos.write(data, 0, len);
				}

			} catch (IOException e) {
				System.out.println("run() -try Error");
				e.printStackTrace();
			} finally {
				bos.close();
				dis.close();
				fos.close();
			}

		} catch (Exception e) {
			System.out.println("run() fail");
			e.printStackTrace();
		}
	}

}
