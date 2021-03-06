package dropbox.messages;

import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dropbox.Client;
import dropbox.FileCache;

public class FilesMessage extends Message {

	// FILES [number of files]
	private static final Pattern PATTERN = Pattern.compile("FILES\\s\\d+");
	private Client client;

	public FilesMessage(Client client) {
		this.client = client;
	}

	@Override
	public boolean matches(String msg) {
		Matcher matcher = PATTERN.matcher(msg);
		return matcher.matches();
	}

	@Override
	public void perform(FileCache cache, Socket socket, String msg) {
		String[] splitMsg = msg.split(" ");
		client.createArray(Integer.valueOf(splitMsg[1]));
	}
}
