package idez.xmppteste;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class XMPPMainActivity extends Activity {
	
	private SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ((Button)findViewById(R.id.buttonConectar)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Conexao().execute();
			}
		});
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		case R.id.itemContacts:
			startActivity(new Intent(this, BuddiesActivity.class));
			break;
		}
		return true;
	}
	
	class Conexao extends AsyncTask<String, String, String[]> {
		
		@Override
		protected void onPreExecute() {
	        ((ProgressBar)findViewById(R.id.progressBar1)).setVisibility(View.VISIBLE);
	        ((Button)findViewById(R.id.buttonConectar)).setEnabled(false);
			((TextView)findViewById(R.id.textStatus)).setText("Conectando...");
		}

		@Override
		protected String[] doInBackground(String... params) {
			return ((XMPPApplication) getApplication()).conectar();
		}

		@Override
		protected void onPostExecute(String[] result) {
	        ((ProgressBar)findViewById(R.id.progressBar1)).setVisibility(View.INVISIBLE);
	        Toast.makeText(XMPPMainActivity.this, result[1], Toast.LENGTH_LONG).show();
			switch (Integer.parseInt(result[0])) {
			case 1:
				Intent i = new Intent(XMPPMainActivity.this, BuddiesActivity.class);
				i.putExtra("teste", "TESTANDOOOO");
				startActivity(i);
				break;
			case 2:
		        ((Button)findViewById(R.id.buttonConectar)).setEnabled(true);
			default:
				break;
			}
		}
		
	}
}