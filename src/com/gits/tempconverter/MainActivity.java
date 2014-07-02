package com.gits.tempconverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity 
{
	private Datastore datastore;
	private ArrayList<String> conversionHistory;
	private ArrayAdapter<String> adapter;
	
	private Button convertButton;
	private EditText fField;
	private TextView cLabel;
	private TextView conversionLabel;
	private ListView conversionList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		datastore = new Datastore(this);
		convertButton = (Button) findViewById(R.id.conversionButton);
		fField = (EditText) findViewById(R.id.fDegreeField);
		cLabel = (TextView) findViewById(R.id.cDegreeField);
		conversionLabel = (TextView) findViewById(R.id.conversionLabel);
		conversionList = (ListView) findViewById(R.id.conversionList);
		
		conversionHistory = datastore.loadHistory();
		if(conversionHistory == null)
			conversionHistory = new ArrayList<>();
		else if(conversionHistory.isEmpty())
		{
			conversionLabel.setVisibility(View.VISIBLE);
			conversionList.setVisibility(View.GONE);
		}
		else
		{
			conversionLabel.setVisibility(View.GONE);
			conversionList.setVisibility(View.VISIBLE);
		}
		adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, conversionHistory);
		
		conversionList.setAdapter(adapter);
		conversionList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				final int index = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			    builder.setItems(new String[]{"Remove"}, new DialogInterface.OnClickListener() 
			    {
			    	public void onClick(DialogInterface dialog, int which) 
			    	{
			    		conversionHistory.remove(index);
			    		adapter.notifyDataSetChanged();
			    		if(conversionHistory.isEmpty()){
			    			conversionLabel.setVisibility(View.VISIBLE);
			    			conversionList.setVisibility(View.GONE);
			    		}
			    		datastore.saveHistory(conversionHistory);
			        }
			    });
			    builder.create().show();

			}
			
		});
		convertButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String rawInput = fField.getText().toString();
				
				if(rawInput.isEmpty()){
					fField.setError("Can't be empty");
					return;
				}
				
				double fDegrees = 0.0;
				
				try{
					fDegrees = Double.parseDouble(rawInput);
				}catch(Exception e){
					fField.setError("Must be a number");
					return;
				}
				
				double cDegrees = fToC(fDegrees);
				cDegrees = round(cDegrees, 2);
				cLabel.setText(cDegrees + "°C");
				
				conversionHistory.add(rawInput+"°F = "+cDegrees+"°C");
				conversionLabel.setVisibility(View.GONE);
				conversionList.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
				datastore.saveHistory(conversionHistory);
			}
		});
		
	}
	
	private double fToC(double fDegrees){
		return (fDegrees - 32) / 1.8;
	}
	
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
