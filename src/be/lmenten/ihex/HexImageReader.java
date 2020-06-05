package be.lmenten.ihex;

import java.io.IOException;

public interface HexImageReader
	extends AutoCloseable
{
	public void setEndianness( HexDataEndianness endianness );
	public HexDataEndianness getEndianness();

	// ------------------------------------------------------------------------

	public boolean eof() throws IOException;

	// ------------------------------------------------------------------------

	public int readByte() throws IOException;
	public int readWord() throws IOException;
	public long readDWord() throws IOException;

	// ------------------------------------------------------------------------

	public boolean hasLinearAddress();
	public boolean hasSegmentAddress();

	public int getAddress();
	public int getStartAddress();
}
