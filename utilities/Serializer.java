import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Serializer
{
	void serialize(Object o, String fileName) throws IOException
	{
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));
		outputStream.writeObject(o);
		outputStream.close();
	}
		
	Object deserialize(String fileName) throws IOException, ClassNotFoundException
	{	
		Object o;
		FileInputStream inputStream = new FileInputStream(fileName);
		ObjectInputStream reader = new ObjectInputStream(inputStream);

		o = reader.readObject();
		
		return o;
	}
}
