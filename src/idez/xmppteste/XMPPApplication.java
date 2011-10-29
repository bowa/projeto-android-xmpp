package idez.xmppteste;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class XMPPApplication extends Application {
	
	private XMPPConnection xmppConnection = null;
	private ArrayList<ChatActivity> activeChats = new ArrayList<ChatActivity>();
	private SharedPreferences prefs;

	@Override
	public void onCreate() {
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		super.onCreate();
	}

	public void conectar() {
		
		if (this.xmppConnection != null)
			if (this.xmppConnection.isConnected())
				return;
		
		String username = this.prefs.getString("username", "");
		String password = this.prefs.getString("password", "");
		String host     = this.prefs.getString("host", "");
		Integer port    = this.prefs.getInt("port", 0);
		
		ConnectionConfiguration config = new ConnectionConfiguration(host, port);
		config.setSecurityMode(SecurityMode.disabled);
		config.setSASLAuthenticationEnabled(false);
		this.xmppConnection = new XMPPConnection(config);
		try {
			this.xmppConnection.connect();
			this.xmppConnection.login(username, password);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public XMPPConnection getXmppConnection() {
		return xmppConnection;
	}
	public void setXmppConnection(XMPPConnection xmppConnection) {
		this.xmppConnection = xmppConnection;
	}
	public ArrayList<ChatActivity> getActiveChats() {
		return activeChats;
	}
	public void setActiveChats(ArrayList<ChatActivity> activeChats) {
		this.activeChats = activeChats;
	}
}
