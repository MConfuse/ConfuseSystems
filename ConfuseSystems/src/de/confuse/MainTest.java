package de.confuse;

import java.io.File;
import java.io.IOException;

import de.confuse.confFile.ConfFileField;
import de.confuse.confFile.ConfFileReader;
import de.confuse.confFile.ConfFileWriter;

public class MainTest {

	public MainTest()
	{
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)
	{
		ConfFileWriter writer = new ConfFileWriter(new File("yeeeee"), true);

		ConfFileField field = new ConfFileField("Test");
		field.put("heck", "yea");
		field.put("fuck", "no");
		ConfFileField field2 = new ConfFileField("Scam");
		field2.put("menolike", "scammers");
		writer.addFields(field, field2);
		try
		{
			writer.writeFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			ConfFileReader reader = new ConfFileReader(new File("yeeeee.conff"));
			field = reader.getField("Scam");
			System.out.println(field.getValue("menolie"));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
