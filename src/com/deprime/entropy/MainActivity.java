package com.deprime.entropy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

import com.deprime.entropy.ShellInterface;

public class MainActivity extends Activity
{
	public SharedPreferences spref;
	final String PREF_NAME="preferences";

	File fs = null;
	boolean cb;
	boolean dns;
	boolean io;

	Switch cbreeder;
	Switch dnsenable;
	Switch ioenable;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super .onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get toggle state
		spref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		cbreeder = (Switch) findViewById(R.id.cbToggle);
		dnsenable = (Switch) findViewById(R.id.dnsToggle);
		ioenable = (Switch) findViewById(R.id.ioToggle);
		cb = spref.getBoolean("CBreeder", true);
		dns = spref.getBoolean("DNS", true);
		io = spref.getBoolean("IO", true);

		if (cb == true) 
		{
			cbreeder.setChecked(true);
		}
		else
		{
			cbreeder.setChecked(false);
		}
		if (dns == true) 
		{
			dnsenable.setChecked(true);
		}
		else
		{
			dnsenable.setChecked(false);
		}
		if (io == true) 
		{
			ioenable.setChecked(true);
		}
		else
		{
			ioenable.setChecked(false);
		}
	}

	public void runCB(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			//if(ShellInterface.isSuAvailable()) { ShellInterface.runCommand("/etc/CrossBreeder/ENABLE_CROSSBREEDER"); }
			if (ShellInterface.isSuAvailable())
			{ String cbOn = ShellInterface.getProcessOutput("/etc/CrossBreeder/ENABLE_CROSSBREEDER"); 
				Toast.makeText(getApplicationContext(), cbOn, Toast.LENGTH_LONG).show();}
			SharedPreferences.Editor editor = spref.edit();
			editor.putBoolean("CBreeder", true); // value to store
			editor.commit();
		}
		else
		{
			// handle toggle off
			if (ShellInterface.isSuAvailable())
			{ String cbOff = ShellInterface.getProcessOutput("/etc/CrossBreeder/DISABLE_CROSSBREEDER"); 
				Toast.makeText(getApplicationContext(), cbOff, Toast.LENGTH_LONG).show();}
			SharedPreferences.Editor editor = spref.edit();
			editor.putBoolean("CBreeder", false); // value to store
			editor.commit();
		}    
	}

	public void runDNS(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			if (ShellInterface.isSuAvailable())
			{ String dnsOn = ShellInterface.getProcessOutput("/etc/CrossBreeder/ENABLE_ADBLOCK"); 
				Toast.makeText(getApplicationContext(), dnsOn, Toast.LENGTH_LONG).show();}
			SharedPreferences.Editor editor = spref.edit();
			editor.putBoolean("DNS", true); // value to store
			editor.commit();
		}
		else
		{
			// handle toggle off
			if (ShellInterface.isSuAvailable())
			{ String dnsOff = ShellInterface.getProcessOutput("/etc/CrossBreeder/DISABLE_ADBLOCK"); 
				Toast.makeText(getApplicationContext(), dnsOff, Toast.LENGTH_LONG).show();}
			SharedPreferences.Editor editor = spref.edit();
			editor.putBoolean("DNS", false); // value to store
			editor.commit();
		}
	}

	public void runIO(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			if (ShellInterface.isSuAvailable())
			{ String ioOn = ShellInterface.getProcessOutput("/etc/CrossBreeder/ENABLE_IO_TWEAKS"); 
				Toast.makeText(getApplicationContext(), ioOn, Toast.LENGTH_LONG).show();}
			SharedPreferences.Editor editor = spref.edit();
			editor.putBoolean("IO", true); // value to store
			editor.commit();
		}
		else
		{
			// handle toggle off
			if (ShellInterface.isSuAvailable())
			{ String ioOff = ShellInterface.getProcessOutput("/etc/CrossBreeder/DISABLE_IO_TWEAKS"); 
				Toast.makeText(getApplicationContext(), ioOff, Toast.LENGTH_LONG).show();}
			SharedPreferences.Editor editor = spref.edit();
			editor.putBoolean("IO", false); // value to store
			editor.commit();
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
	// fix losing state on rotation
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{        
		super.onConfigurationChanged(newConfig);
	}
}

