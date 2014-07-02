package com.gits.tempconverter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;

public class Datastore 
{
	public final static String PREFS_NAME = "conversion_app";
	public final static String HISTORY_FILE = "c_a_history";
	private Context _context;
	
	public Datastore(Context context)
	{
		_context = context;
	}
	
	public boolean saveHistory(ArrayList<String> conversionHistory)
	{
		//Serialize and save
		FileOutputStream fos;
		try 
		{
			//Save acronyms
			fos = _context.openFileOutput(HISTORY_FILE, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(conversionHistory);
			os.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return false;
		}
		catch( IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public ArrayList<String> loadHistory()
	{
		ArrayList<String> history = null;
		
		FileInputStream fis;
		try 
		{
			//Load acronyms
			fis = _context.openFileInput(HISTORY_FILE);
			ObjectInputStream is = new ObjectInputStream(fis);
			history = (ArrayList<String>) is.readObject();
			is.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		return history;
	}
}
