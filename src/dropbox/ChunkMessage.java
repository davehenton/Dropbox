package dropbox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ChunkMessage implements Message {
	private static final long serialVersionUID = 1L;
	private String filename;
	private long lastModified;
	private long filesize;
	private int offset;
	private String base64EncodedBytes;

	// TODO create correct pattern
	protected final static Pattern PATTERN = Pattern.compile("CHUNK...");

	@Override
	public boolean matches(String msg) {
		Matcher matcher = PATTERN.matcher(msg);
		return matcher.matches();
	}
}