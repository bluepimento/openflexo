 * 
 * @author Robert Greig
	/**
	 * The underlying file
	 */
	private File file;
	/**
	 * Construct a new file mode from a file.
	 */
	public FileMode(File file) {
		this.file = file;
	}
	/**
	 * Returns a CVS-compatible file mode string
	 */
	@Override
		// TODO: really implement this!
		return "u=rw,g=r,o=r"; // NOI18N
	}