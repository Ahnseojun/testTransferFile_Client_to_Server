package testTransferFile_Only_put_Choose;

import java.awt.Panel;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFileChooser;

public class FileSender implements Runnable {

	String serverFilePath;
	Socket socket;

	enum DialogChoice {
		open
	}

	DataOutputStream dos; // 바이트 기반 출력 스트림 -> writeUTF 사용 가능
	FileInputStream fis;
	BufferedInputStream bis;

	public FileSender(Socket socket, String serverFilePath) {
		this.socket = socket;
		this.serverFilePath = serverFilePath;

		try {
			// 데이터 전송용 스트림 생성
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("복사할 파일을 선택하세요");
		File open = ShowDialog(DialogChoice.open);

		if (open == null) {
			System.out.println("복사할 파일을 선택하지 않았습니다");
			return;
		}
		if (!open.exists()) {
			System.out.println("대상 파일이 존재하지 않습니다");
			return;
		}

		try {
			// 서버에 저장할 경로 전송
			dos.writeUTF(serverFilePath);
			dos.writeUTF(open.getName());

			fis = new FileInputStream(open);
			bis = new BufferedInputStream(fis);

			// 실제 파일전송
			int len;
			byte[] data = new byte[1024];

			while ((len = bis.read(data)) != -1) {
				dos.write(data, 0, len);
			}
			dos.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dos.close();
				bis.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private File ShowDialog(DialogChoice choice) {
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setCurrentDirectory(new File("C:/Users/Seojun/Desktop"));

		int result = 0;
		if (choice == DialogChoice.open) {
			result = chooser.showOpenDialog(new Panel());
		}

		File selectedFile = null;

		if (result == JFileChooser.APPROVE_OPTION) {
			selectedFile = chooser.getSelectedFile();
			System.out.println("Selected filePath : " + selectedFile.getAbsoluteFile());
			System.out.println("Selected fileName : " + selectedFile.getName());
			System.out.println("Saved Path : " + serverFilePath);
		}
		return selectedFile;
	}

}
