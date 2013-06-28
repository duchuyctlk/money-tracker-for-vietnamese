package de.enough.polish.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public final class Serializer {
	private static final byte VERSION = 1;
	private static final byte TYPE_EXTERNALIZABLE = 0;
	private static final byte TYPE_EXTERNALIZABLE_ARRAY = 1;
	private static final byte TYPE_OBJECT_ARRAY = 2;
	private static final byte TYPE_BYTE = 3;
	private static final byte TYPE_SHORT = 4;
	private static final byte TYPE_INTEGER = 5;
	private static final byte TYPE_LONG = 6;
	private static final byte TYPE_FLOAT = 7;
	private static final byte TYPE_DOUBLE = 8;
	private static final byte TYPE_STRING = 9;
	private static final byte TYPE_STRING_BUFFER = 10;
	private static final byte TYPE_CHARACTER = 11;
	private static final byte TYPE_BOOLEAN = 12;
	private static final byte TYPE_DATE = 13;
	private static final byte TYPE_CALENDAR = 14;
	private static final byte TYPE_RANDOM = 15;
	private static final byte TYPE_HASHTABLE = 16;
	private static final byte TYPE_STACK = 17;
	private static final byte TYPE_VECTOR = 18;
	private static final byte TYPE_IMAGE = 19;
	private static final byte TYPE_IMAGE_RGB = 0;
	private static final byte TYPE_IMAGE_BYTES = 1;
	private static final byte TYPE_FONT = 20;
	private static final byte TYPE_COMMAND = 21;
	private static final byte TYPE_BYTE_ARRAY = 22;
	private static final byte TYPE_SHORT_ARRAY = 23;
	private static final byte TYPE_INT_ARRAY = 24;
	private static final byte TYPE_LONG_ARRAY = 25;
	private static final byte TYPE_FLOAT_ARRAY = 26;
	private static final byte TYPE_DOUBLE_ARRAY = 27;
	private static final byte TYPE_CHAR_ARRAY = 28;
	private static final byte TYPE_BOOLEAN_ARRAY = 29;
	private static final byte TYPE_STRING_ARRAY = 30;

	public static void serialize(Object object, DataOutputStream out)
			throws IOException {
		out.writeByte(1);
		boolean isNull = object == null;
		out.writeBoolean(isNull);
		if (!isNull)
			if (object instanceof Externalizable) {
				out.writeByte(0);
				String className = object.getClass().getName();

				out.writeUTF(className);
				((Externalizable) object).write(out);
			} else if (object instanceof Externalizable[]) {
				out.writeByte(1);
				String cn = object.getClass().getName();
				cn = cn.substring(cn.lastIndexOf('[') + 2, cn.length() - 1);

				out.writeUTF(cn);
				Externalizable[] externalizables = (Externalizable[]) (Externalizable[]) object;
				out.writeInt(externalizables.length);
				Hashtable classNames = new Hashtable();
				Class lastClass = null;
				byte lastId = 0;
				byte idCounter = 0;
				for (int i = 0; i < externalizables.length; ++i) {
					Externalizable externalizable = externalizables[i];
					Class currentClass = externalizable.getClass();
					if (currentClass == lastClass) {
						out.writeByte(lastId);
					} else {
						Byte knownId = (Byte) classNames.get(currentClass);
						if (knownId != null) {
							out.writeByte(knownId.byteValue());
						} else {
							out.writeByte(0);
							idCounter = (byte) (idCounter + 1);
							String className = currentClass.getName();

							out.writeUTF(className);
							lastClass = currentClass;
							lastId = idCounter;
							classNames.put(currentClass, new Byte(lastId));
						}
					}
					externalizable.write(out);
				}
			} else if (object instanceof Object[]) {
				out.writeByte(2);
				Object[] objects = (Object[]) (Object[]) object;
				out.writeInt(objects.length);
				for (int i = 0; i < objects.length; ++i) {
					Object obj = objects[i];
					serialize(obj, out);
				}
			} else if (object instanceof Byte) {
				out.writeByte(3);
				out.writeByte(((Byte) object).byteValue());
			} else if (object instanceof Short) {
				out.writeByte(4);
				out.writeShort(((Short) object).shortValue());
			} else if (object instanceof Integer) {
				out.writeByte(5);
				out.writeInt(((Integer) object).intValue());
			} else if (object instanceof Long) {
				out.writeByte(6);
				out.writeLong(((Long) object).longValue());
			} else if (object instanceof Float) {
				out.writeByte(7);
				out.writeFloat(((Float) object).floatValue());
			} else if (object instanceof Double) {
				out.writeByte(8);
				out.writeDouble(((Double) object).doubleValue());
			} else if (object instanceof String) {
				out.writeByte(9);
				out.writeUTF((String) object);
			} else if (object instanceof StringBuffer) {
				out.writeByte(10);
				out.writeUTF(((StringBuffer) object).toString());
			} else if (object instanceof Character) {
				out.writeByte(11);
				out.writeChar(((Character) object).charValue());
			} else if (object instanceof Boolean) {
				out.writeByte(12);
				out.writeBoolean(((Boolean) object).booleanValue());
			} else if (object instanceof Date) {
				out.writeByte(13);
				out.writeLong(((Date) object).getTime());
			} else if (object instanceof Calendar) {
				out.writeByte(14);
				out.writeLong(((Calendar) object).getTime().getTime());
			} else if (object instanceof Random) {
				out.writeByte(15);
			} else if (object instanceof Hashtable) {
				out.writeByte(16);
				Hashtable table = (Hashtable) object;
				out.writeInt(table.size());
				Enumeration enumeration = table.keys();
				while (enumeration.hasMoreElements()) {
					Object key = enumeration.nextElement();
					serialize(key, out);
					Object value = table.get(key);
					serialize(value, out);
				}
			} else if (object instanceof Vector) {
				if (object instanceof Stack)
					out.writeByte(17);
				else {
					out.writeByte(18);
				}
				Vector vector = (Vector) object;
				int size = vector.size();
				out.writeInt(size);
				for (int i = 0; i < size; ++i) {
					serialize(vector.elementAt(i), out);
				}
			} else if (object instanceof Image) {
				out.writeByte(19);

				Image image = (Image) object;
				out.writeByte(0);
				int width = image.getWidth();
				int height = image.getHeight();
				out.writeInt(width);
				out.writeInt(height);
				int[] rgb = new int[width * height];
				image.getRGB(rgb, 0, width, 0, 0, width, height);
				for (int i = 0; i < rgb.length; ++i) {
					out.writeInt(rgb[i]);
				}

			} else if (object instanceof Font) {
				out.writeByte(20);
				Font font = (Font) object;
				out.writeInt(font.getFace());
				out.writeInt(font.getStyle());
				out.writeInt(font.getSize());
			} else if (object instanceof Command) {
				out.writeByte(21);
				Command command = (Command) object;
				out.writeInt(command.getCommandType());
				out.writeInt(command.getPriority());
				out.writeUTF(command.getLabel());
			} else if (object instanceof byte[]) {
				out.writeByte(22);
				byte[] numbers = (byte[]) (byte[]) object;
				out.writeInt(numbers.length);
				out.write(numbers, 0, numbers.length);
			} else if (object instanceof short[]) {
				out.writeByte(23);
				short[] numbers = (short[]) (short[]) object;
				out.writeInt(numbers.length);
				for (int i = 0; i < numbers.length; ++i) {
					short number = numbers[i];
					out.writeShort(number);
				}
			} else if (object instanceof int[]) {
				out.writeByte(24);
				int[] numbers = (int[]) (int[]) object;
				out.writeInt(numbers.length);
				for (int i = 0; i < numbers.length; ++i) {
					int number = numbers[i];
					out.writeInt(number);
				}
			} else if (object instanceof long[]) {
				out.writeByte(25);
				long[] numbers = (long[]) (long[]) object;
				out.writeInt(numbers.length);
				for (int i = 0; i < numbers.length; ++i) {
					long number = numbers[i];
					out.writeLong(number);
				}
			} else if (object instanceof float[]) {
				out.writeByte(26);
				float[] numbers = (float[]) (float[]) object;
				out.writeInt(numbers.length);
				for (int i = 0; i < numbers.length; ++i) {
					float number = numbers[i];
					out.writeFloat(number);
				}
			} else if (object instanceof double[]) {
				out.writeByte(27);
				double[] numbers = (double[]) (double[]) object;
				out.writeInt(numbers.length);
				for (int i = 0; i < numbers.length; ++i) {
					double number = numbers[i];
					out.writeDouble(number);
				}
			} else if (object instanceof char[]) {
				out.writeByte(28);
				char[] characters = (char[]) (char[]) object;
				out.writeInt(characters.length);
				for (int i = 0; i < characters.length; ++i) {
					char c = characters[i];
					out.writeChar(c);
				}
			} else if (object instanceof boolean[]) {
				out.writeByte(29);
				boolean[] bools = (boolean[]) (boolean[]) object;
				out.writeInt(bools.length);
				for (int i = 0; i < bools.length; ++i) {
					boolean b = bools[i];
					out.writeBoolean(b);
				}
			} else if (object instanceof String[]) {
				out.writeByte(30);
				String[] strings = (String[]) (String[]) object;
				out.writeInt(strings.length);
				for (int i = 0; i < strings.length; ++i) {
					String s = strings[i];
					out.writeUTF(s);
				}
			} else {
				throw new IOException("Cannot serialize "
						+ object.getClass().getName());
			}
	}

	public static Object deserialize(DataInputStream in) throws IOException {
		byte version = in.readByte();

		boolean isNull = in.readBoolean();
		if (isNull) {
			return null;
		}
		byte type = in.readByte();
		String className;
		int length;
		int size;
		switch (type) {
		case 0:
			className = in.readUTF();

			Externalizable extern = null;
			try {
				extern = (Externalizable) Class.forName(className)
						.newInstance();
			} catch (Exception e) {
				throw new IOException(e.toString());
			}
			extern.read(in);
			return extern;
		case 1:
			String cn = in.readUTF();

			length = in.readInt();

			Externalizable[] externalizables = new Externalizable[length];

			Class[] classes = new Class[Math.min(length, 7)];

			byte idCounter = 0;
			for (int i = 0; i < externalizables.length; ++i) {
				int classId = in.readByte();
				Class currentClass;
				if (classId == 0) {
					className = in.readUTF();
					try {
						currentClass = Class.forName(className);
					} catch (ClassNotFoundException e) {
						throw new IOException(e.toString());
					}
					if (idCounter > classes.length) {
						Class[] newClasses = new Class[classes.length + 7];
						System.arraycopy(classes, 0, newClasses, 0,
								classes.length);
						classes = newClasses;
					}
					classes[idCounter] = currentClass;
					idCounter = (byte) (idCounter + 1);
				} else {
					currentClass = classes[(classId - 1)];
				}
				try {
					Externalizable externalizable = (Externalizable) currentClass
							.newInstance();
					externalizable.read(in);
					externalizables[i] = externalizable;
				} catch (Exception e) {
					throw new IOException(e.toString());
				}
			}
			return externalizables;
		case 2:
			length = in.readInt();
			Object[] objects = new Object[length];
			for (int i = 0; i < objects.length; ++i) {
				objects[i] = deserialize(in);
			}
			return objects;
		case 3:
			return new Byte(in.readByte());
		case 4:
			return new Short(in.readShort());
		case 5:
			return new Integer(in.readInt());
		case 6:
			return new Long(in.readLong());
		case 7:
			return new Float(in.readFloat());
		case 8:
			return new Double(in.readDouble());
		case 9:
			return in.readUTF();
		case 10:
			return new StringBuffer(in.readUTF());
		case 11:
			return new Character(in.readChar());
		case 12:
			return new Boolean(in.readBoolean());
		case 13:
			return new Date(in.readLong());
		case 14:
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(in.readLong()));
			return calendar;
		case 15:
			return new Random();
		case 16:
			size = in.readInt();
			Hashtable hashtable = new Hashtable(size);
			for (int i = 0; i < size; ++i) {
				Object key = deserialize(in);
				Object value = deserialize(in);
				hashtable.put(key, value);
			}
			return hashtable;
		case 17:
		case 18:
			size = in.readInt();
			Vector vector;
			if (type == 17)
				vector = new Stack();
			else {
				vector = new Vector(size);
			}
			for (int i = 0; i < size; ++i) {
				Object value = deserialize(in);
				vector.addElement(value);
			}
			return vector;
		case 19:
			byte subType = in.readByte();
			if (subType == 0) {
				int width = in.readInt();
				int height = in.readInt();
				int[] rgb = new int[width * height];
				for (int i = 0; i < rgb.length; ++i) {
					rgb[i] = in.readInt();
				}
				return Image.createRGBImage(rgb, width, height, true);
			}

			int bytesLength = in.readInt();
			byte[] buffer = new byte[bytesLength];
			in.readFully(buffer);
			return Image.createImage(buffer, 0, bytesLength);
		case 20:
			int face = in.readInt();
			int style = in.readInt();
			size = in.readInt();
			return Font.getFont(face, style, size);
		case 21:
			int cmdType = in.readInt();
			int priority = in.readInt();
			String label = in.readUTF();
			return new Command(label, cmdType, priority);
		case 22:
			length = in.readInt();
			byte[] byteNumbers = new byte[length];
			in.readFully(byteNumbers);
			return byteNumbers;
		case 23:
			length = in.readInt();
			short[] shortNumbers = new short[length];
			for (int i = 0; i < length; ++i) {
				shortNumbers[i] = in.readShort();
			}
			return shortNumbers;
		case 24:
			length = in.readInt();
			int[] intNumbers = new int[length];
			for (int i = 0; i < length; ++i) {
				intNumbers[i] = in.readInt();
			}
			return intNumbers;
		case 25:
			length = in.readInt();
			long[] longNumbers = new long[length];
			for (int i = 0; i < length; ++i) {
				longNumbers[i] = in.readLong();
			}
			return longNumbers;
		case 26:
			length = in.readInt();
			float[] floatNumbers = new float[length];
			for (int i = 0; i < length; ++i) {
				floatNumbers[i] = in.readFloat();
			}
			return floatNumbers;
		case 27:
			length = in.readInt();
			double[] doubleNumbers = new double[length];
			for (int i = 0; i < length; ++i) {
				doubleNumbers[i] = in.readDouble();
			}
			return doubleNumbers;
		case 28:
			length = in.readInt();
			char[] characters = new char[length];
			for (int i = 0; i < length; ++i) {
				characters[i] = in.readChar();
			}
			return characters;
		case 29:
			length = in.readInt();
			boolean[] bools = new boolean[length];
			for (int i = 0; i < length; ++i) {
				bools[i] = in.readBoolean();
			}
			return bools;
		case 30:
			length = in.readInt();
			String[] strings = new String[length];
			for (int i = 0; i < length; ++i) {
				strings[i] = in.readUTF();
			}
			return strings;
		}
		throw new IOException("Unknown type: " + type);
	}
}