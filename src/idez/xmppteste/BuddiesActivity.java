package idez.xmppteste;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		XMPPApplication app = (XMPPApplication) getApplication();
		SimpleAdapter sa = new SimpleAdapter(this,app.getBuddies(),R.layout.buddies, new String[] {XMPPApplication.NOMEKEY, XMPPApplication.STATUSKEY}, new int[] {R.id.buddyListNome, R.id.buddyListEmail});
		ListView lv = getListView();
		lv.setAdapter(sa);
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
