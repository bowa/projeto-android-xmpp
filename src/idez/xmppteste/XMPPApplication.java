package idez.xmppteste;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPConnection;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
		new ConectarXmpp().doInBackground( prefs.getString("username", ""), 
				                           prefs.getString("password", ""), 
				                           prefs.getString("host", "talk.google.com"), 
				                           prefs.getString("port", "5223"));
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
	
	class ConectarXmpp extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			if (xmppConnection != null)
				if (xmppConnection.isConnected())
					return "Já existe uma conexão aberta.";
			
			String username = params[0];
			String password = params[1];
			String host     = params[2];
			Integer port    = Integer.parseInt(params[3]);
			
			ConnectionConfiguration config = new ConnectionConfiguration(host, port);
			config.setSecurityMode(SecurityMode.required);
			config.setSASLAuthenticationEnabled(prefs.getBoolean("sas", false));
			xmppConnection = new XMPPConnection(config);
			try {
				xmppConnection.connect();
				xmppConnection.login(username, password);
			} catch (Exception e) {
				Toast.makeText(XMPPApplication.this, e.getMessage(), Toast.LENGTH_LONG).show();
				return e.getMessage();
			}
			
			return "Conexão realizada com sucesso.";
		}
		
	}
}
