package com.deprime.entropy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class MainActivity extends Activity
{
	File fs = null;
	boolean on;
	boolean dns;
	
	public SharedPreferences spref;
	final String PREF_NAME="preferences";
	Switch cb;
	Switch dnsenable;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super .onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get toggle state
		spref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		cb = (Switch) findViewById(R.id.cbToggle);
		dnsenable = (Switch) findViewById(R.id.dnsToggle);
		on = spref.getBoolean("On", true);
		dns = spref.getBoolean("DNS",true);
		if (on == true) 
		{
			cb.setChecked(true);
		}
		else
		{
			cb.setChecked(false);
		}
		
		if (dns == true) 
		{
			dnsenable.setChecked(true);
		}
		else
		{
			dnsenable.setChecked(false);
		}

	}

	public void runCB(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			try
			{
				Process process = null;
				DataOutputStream os = null;

				process = Runtime.getRuntime().exec("su");
				os = new DataOutputStream(process.getOutputStream());
				os.writeBytes("sh /etc/CrossBreeder/ENABLE_CROSSBREEDER\n");
				os.writeBytes("exit\n");
				os.flush();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("On", true); // value to store
				editor.apply();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			// handle toggle off
			Process process = null;
			DataOutputStream os = null;
			
			try
			{
				process = Runtime.getRuntime().exec("su");
				os = new DataOutputStream(process.getOutputStream());
				os.writeBytes("sh /etc/CrossBreeder/DISABLE_CROSSBREEDER\n");
				os.writeBytes("exit\n");
				os.flush();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("On", false); // value to store
				editor.apply();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}    
	}
	
	public void runDNS(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			try
			{
				Process process = null;
				DataOutputStream os = null;

				process = Runtime.getRuntime().exec("su");
				os = new DataOutputStream(process.getOutputStream());
				os.writeBytes("killall -9 dnsproxy2\n");
				os.writeBytes("sh /etc/CrossBreeder/ENABLE_ADBLOCK\n");
				os.writeBytes("./etc/CrossBreeder/dnsproxy2 -w 127.0.0.1\n");
				os.writeBytes("exit\n");
				os.flush();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("DNS", true); // value to store
				editor.commit();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			// handle toggle off
			Process process = null;
			DataOutputStream os = null;

			try
			{
				process = Runtime.getRuntime().exec("su");
				os = new DataOutputStream(process.getOutputStream());
				os.writeBytes("sh /etc/CrossBreeder/DISABLE_ADBLOCK\n");
				os.writeBytes("exit\n");
				os.flush();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("DNS", false); // value to store
				editor.commit();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}    
	}

	@Override
	public void onPause()
	{
		_handler = null;
		super.onPause();
	}

	private Handler _handler;

	@Override
	public void onResume()
	{
		super.onResume();
		_handler = new Handler();
		Runnable r = new Runnable() {
			public void run()
			{
				if (_handler == _h0)
				{
					tick();
					_handler.postDelayed(this, 1500);
				}
			}

			private final Handler _h0 = _handler;
		};
		r.run();
	}

	private void tick()
	{
		try
		{
			TextView txtContent = (TextView) findViewById(R.id.txtEntropy);
			ProgressBar entropyBar = (ProgressBar) findViewById(R.id.entBar);
			File fs = new File("/proc/sys/kernel/random/entropy_avail");
			FileReader fr = new FileReader(fs);
			BufferedReader br = new BufferedReader(fr);
			String entropy = br.readLine();
			Integer progress = new Integer(entropy);
			txtContent.setText(entropy);
			entropyBar.setMax(4096);
			entropyBar.setProgress(progress);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

