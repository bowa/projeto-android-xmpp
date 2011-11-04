package idez.xmppteste;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BuddiesActivity extends ListActivity {

	private static final String NOMEKEY = "nome";
	private static final String EMAILKEY = "email";
	private static final String STATUSKEY = "status";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SimpleAdapter sa = new SimpleAdapter(this, this.showUsers(), R.layout.buddies, new String[] {NOMEKEY, EMAILKEY}, new int[] {R.id.buddyListNome, R.id.buddyListEmail});
		
		ListView lv = getListView();
		lv.setAdapter(sa);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LinearLayout ll = (LinearLayout) view;
				String email = ((TextView) ll.findViewById(R.id.buddyListEmail)).getText().toString() ;
				Intent i = new Intent(BuddiesActivity.this, ChatActivity.class);
				i.putExtra("email", email);
				startActivity(i);
			}
		});
	}
	
	private ArrayList<HashMap<String, String>> showUsers() {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		if (((XMPPApplication) getApplication()).getXmppConnection() != null) {
			Roster roster = ((XMPPApplication) getApplication()).getXmppConnection().getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(NOMEKEY, entry.getName());
				hm.put(EMAILKEY, entry.getUser());
				result.add(hm);
			}
		}
		
		Comparator<HashMap<String, String>> comparator = new Comparator<HashMap<String, String>>() {                                    
	        @Override
	        public int compare(HashMap<String, String> object1, HashMap<String, String> object2) {
	        	return object1.get(NOMEKEY).compareToIgnoreCase(object2.get(NOMEKEY));
	        }
		};
		
		Collections.sort(result, comparator);
		
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.buddies_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemDisconnect:
			((XMPPApplication) getApplication()).desconectar();
			startActivity(new Intent(null, XMPPApplication.class));
			break;
		case R.id.itemContacts:
			startActivity(new Intent(this, BuddiesActivity.class));
			break;
		}
		return true;
	}

}
