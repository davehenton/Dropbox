package dropbox;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyncMessage extends Message {
	private String[] splitMsg;

	// SYNC [filename] [last modified] [filesize]
	private final static Pattern PATTERN = Pattern.compile("SYNC\\s\\w+\\s(\\d+\\s){2}");

	@Override
	public boolean matches(String msg) {
		Matcher matcher = PATTERN.matcher(msg);
		splitMsg = msg.split(" ");
		return matcher.matches();
	}

	@Override
	public void perform(FileCache cache, Socket socket) {
		String fileName = splitMsg[0];
		long lastModified = Long.valueOf(splitMsg[1]);
		int fileSize = Integer.valueOf(splitMsg[2]);

		// get list of all files in client root directory and
		File folder = new File(cache.getRoot());
		File[] listOfFiles = folder.listFiles();

		boolean found = false;

		try {
			for (File clientFile : listOfFiles) {
				// see if this file exists in the clients directory
				if (clientFile.getName().equals(fileName)) {
					// file is found so now compare when last modified from files in server's cache
					found = true;
					if (clientFile.lastModified() < lastModified) {
						// now need to send download msg to server
						// DOWNLOAD [filename] [offset] [chunk size]
						send("DOWNLOAD " + fileName + " " + 0 + (fileSize >= 512 ? 512 : fileSize), socket);
					}
					break;
				}
			}
			// file is not on clients dir so send download msg
			if (!found) {
				send("DOWNLOAD " + fileName + " " + 0 + (fileSize >= 512 ? 512 : fileSize), socket);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}