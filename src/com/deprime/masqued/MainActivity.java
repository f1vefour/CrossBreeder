package com.deprime.masqued;

import android.app.*;
import android.content.res.*;
import android.os.*;
import android.widget.*;
import java.io.*;

public class MainActivity extends Activity
{
	File fs = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super .onCreate(savedInstanceState);
		setContentView(R.layout.main);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

