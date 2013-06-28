package de.enough.polish.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract interface Externalizable extends Serializable {
	public abstract void write(DataOutputStream paramDataOutputStream)
			throws IOException;

	public abstract void read(DataInputStream paramDataInputStream)
			throws IOException;
}