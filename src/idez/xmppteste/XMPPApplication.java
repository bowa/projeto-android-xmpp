package idez.xmppteste;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;

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
		protected String doInBackground(String... arg0) {
			if (xmppConnection != null)
				if (xmppConnection.isConnected())
					return "Conexão aberta.";
			
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			String host     = prefs.getString("host", "");
			Integer port    = Integer.parseInt(prefs.getString("port", "5222"));
			
			ConnectionConfiguration config = new ConnectionConfiguration(host, port);
			config.setSecurityMode(SecurityMode.required);
			config.setSASLAuthenticationEnabled(true);
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
