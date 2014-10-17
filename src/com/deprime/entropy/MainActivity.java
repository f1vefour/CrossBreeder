//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 2 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

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
	boolean bisp;
	boolean tb;
	boolean ab;

	Switch cbreeder;
	Switch dnsenable;
	Switch ioenable;
	Switch bispenable;
	Switch tbenable;
	Switch abenable;

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
		bispenable = (Switch) findViewById(R.id.bispToggle);
		tbenable = (Switch) findViewById(R.id.tbToggle);
		abenable = (Switch) findViewById(R.id.abToggle);

		cb = spref.getBoolean("CBreeder", false);
		dns = spref.getBoolean("DNS", false);
		io = spref.getBoolean("IO", false);
		bisp = spref.getBoolean("BISP", false);
		tb = spref.getBoolean("TB", false);
		ab = spref.getBoolean("AB", false);
		
		cbreeder.setChecked(cb);
		dnsenable.setChecked(dns);
		ioenable.setChecked(io);
		bispenable.setChecked(bisp);
		tbenable.setChecked(tb);
		abenable.setChecked(ab);
	}

	public void runCB(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			//if(ShellInterface.isSuAvailable()) { ShellInterface.runCommand("/etc/CrossBreeder/ENABLE_CROSSBREEDER"); }
			if (ShellInterface.isSuAvailable())
			{ String cbOn = ShellInterface.getProcessOutput("/etc/CrossBreeder/ENABLE_CROSSBREEDER"); 
				Toast.makeText(getApplicationContext(), cbOn, Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("CBreeder", true); // value to store
				editor.commit();}
		}
		else
		{
			// handle toggle off
			if (ShellInterface.isSuAvailable())
			{ String cbOff = ShellInterface.getProcessOutput("/etc/CrossBreeder/DISABLE_CROSSBREEDER"); 
				Toast.makeText(getApplicationContext(), cbOff, Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("CBreeder", false); // value to store
				editor.commit();}
		}    
	}

	public void runDNS(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			if (ShellInterface.isSuAvailable())
			{ String dnsOn = ShellInterface.getProcessOutput("/etc/CrossBreeder/INSTALL_DNS_CLIENT"); 
				Toast.makeText(getApplicationContext(), dnsOn, Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("DNS", true); // value to store
				editor.commit();}
		}
		else
		{
			// handle toggle off
			if (ShellInterface.isSuAvailable())
			{ String dnsOff = ShellInterface.getProcessOutput("/etc/CrossBreeder/REMOVE_DNS_CLIENT"); 
				Toast.makeText(getApplicationContext(), dnsOff, Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("DNS", false); // value to store
				editor.commit();}
		}
	}

	public void runIO(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			if (ShellInterface.isSuAvailable())
			{ ShellInterface.runCommand("busybox mount -o rw,remount,noatime,nodiratime /system && touch /system/etc/CrossBreeder/START_TWEAKING_IO");
				ShellInterface.runCommand("/system/etc/CrossBreeder/CB_IO_Tweaks.sh");
				ShellInterface.runCommand("busybox mount -o ro,remount,noatime,nodiratime");
				Toast.makeText(getApplicationContext(), "IO tweaks activated", Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("IO", true); // value to store
				editor.commit();}
		}
		else
		{
			// handle toggle off
			if (ShellInterface.isSuAvailable())
			{ ShellInterface.runCommand("busybox mount -o rw,remount,noatime,nodiratime /system && rm /system/etc/CrossBreeder/START_TWEAKING_IO");
				ShellInterface.runCommand("busybox mount -o ro,remount,noatime,nodiratime");
				Toast.makeText(getApplicationContext(), "IO tweaks disabled. You must reboot for this to take effect.", Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("IO", false); // value to store
				editor.commit();}
		}
	}

	public void runBISP(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			if (ShellInterface.isSuAvailable())
			{ ShellInterface.getProcessOutput("/etc/CrossBreeder/ENABLE_BYPASS_ISP"); 
				Toast.makeText(getApplicationContext(), "Bypass ISP enabled. You must reboot for this to take effect.", Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("BISP", true); // value to store
				editor.commit();}
		}
		else
		{
			// handle toggle off
			if (ShellInterface.isSuAvailable())
			{ ShellInterface.getProcessOutput("/etc/CrossBreeder/DISABLE_BYPASS_ISP"); 
			Toast.makeText(getApplicationContext(), "Bypass ISP disabled. You must reboot for this to take effect.", Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("BISP", false); // value to store
				editor.commit();}
		}
	}

	public void runTB(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			if (ShellInterface.isSuAvailable())
			{ ShellInterface.getProcessOutput("/etc/CrossBreeder/INSTALL_TETHER_BOOST"); 
			Toast.makeText(getApplicationContext(), "Tether boost enabled. You must reboot for this to take effect.", Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("TB", true); // value to store
				editor.commit();}
		}
		else
		{
			// handle toggle off
			if (ShellInterface.isSuAvailable())
			{ ShellInterface.getProcessOutput("/etc/CrossBreeder/REMOVE_TETHER_BOOST"); 
			Toast.makeText(getApplicationContext(), "Tether boost disabled. You must reboot for this to take effect.", Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("TB", false); // value to store
				editor.commit();}
		}
	}

	public void runAB(View view)
	{
		if (((Switch) view).isChecked())
		{
			// handle toggle on
			if (ShellInterface.isSuAvailable())
			{ ShellInterface.getProcessOutput("/etc/CrossBreeder/ENABLE_ADBLOCK"); 
			Toast.makeText(getApplicationContext(), "Adblock enabled.", Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("AB", true); // value to store
				editor.commit();}
		}
		else
		{
			// handle toggle off
			if (ShellInterface.isSuAvailable())
			{ ShellInterface.getProcessOutput("/etc/CrossBreeder/DISABLE_ADBLOCK"); 
			Toast.makeText(getApplicationContext(), "Adblock disabled.", Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = spref.edit();
				editor.putBoolean("AB", false); // value to store
				editor.commit();}
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
			new Thread(new Runnable() {
					File fs = new File("/proc/sys/kernel/random/entropy_avail");
					FileReader fr = new FileReader(fs);
					BufferedReader br = new BufferedReader(fr);
					String entropy = br.readLine();
					TextView txtContent = (TextView) findViewById(R.id.txtEntropy);
					ProgressBar entropyBar = (ProgressBar) findViewById(R.id.entBar);
					Integer progress = new Integer(entropy);
					public void run()
					{
						runOnUiThread(new Runnable() {
								public void run()
								{
									txtContent.setText(entropy);
									entropyBar.setMax(4096);
									entropyBar.setProgress(progress);;
								}
							});
					}
				}).start();
	}
	// fix losing state on rotation
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{        
		super.onConfigurationChanged(newConfig);
	}
}

