package idez.xmppteste;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class XMPPApplication extends Application {

	private ArrayList<ChatActivity> activeChats = new ArrayList<ChatActivity>();
	private XMPPConnection xmppConnection = null;
	private SharedPreferences prefs;

	@Override
	public void onCreate() {
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		super.onCreate();
	}

	public String[] conectar() {
		if (xmppConnection != null) {
			if (xmppConnection.isConnected()) {
				Log.i("Conexão", "Conexão aberta!");
				this.desconectar();
			}
		}
		Log.i("Conexão", "Passando parâmetros da conexão...");
		String username = prefs.getString("username", "");
		String password = prefs.getString("password", "");
		String service = prefs.getString("service", "gmail.com");
		String host = prefs.getString("host", "talk.google.com");
		Integer port = Integer.parseInt(prefs.getString("port", "5222"));

		ConnectionConfiguration config = new ConnectionConfiguration(host, port, service);
		switch (Integer.parseInt(prefs.getString("security", "0"))) {
		case 0:
			config.setSecurityMode(SecurityMode.disabled);
			break;
		case 1:
			config.setSecurityMode(SecurityMode.enabled);
			break;
		case 2:
			config.setSecurityMode(SecurityMode.required);
			break;
		}
		config.setSASLAuthenticationEnabled(prefs.getBoolean("sasl", true));
		xmppConnection = new XMPPConnection(config);
		try {
			Log.i("Conexão", "Iniciando conexão...");
			xmppConnection.connect();
			xmppConnection.login(username, password, "smack");
			Log.i("Conexão", "Conectado!");
			return new String[] {"1", "Conectado!"};
		} catch (Exception e) {
			Log.i("Conexão", e.getMessage());
			return new String[] {"2", e.getMessage()};
		}
	}

	public void desconectar() {
		if (this.xmppConnection != null)
			if (this.xmppConnection.isConnected()) {
				Log.i("Conexão", "Desconectando!");
				this.xmppConnection.disconnect();
				Toast.makeText(this, "Desconectado.", Toast.LENGTH_LONG).show();
			}
	}

	public ArrayList<String> showUsers() {
		ArrayList<String> result = new ArrayList<String>();
		if (this.xmppConnection != null) {
			Roster roster = this.xmppConnection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				result.add(entry.getName());
			}
		}
		return result;
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
