package idez.xmppteste;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class XmpptesteActivity extends Activity {
	
	private SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				
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
			startActivity(new Intent(this, ChatActivity.class));
			break;
		case R.id.itemConnect:
			((XMPPApplication) getApplication()).conectar();
			break;
		}
		return true;
	}
}