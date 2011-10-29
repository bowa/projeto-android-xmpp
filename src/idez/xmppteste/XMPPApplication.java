package idez.xmppteste;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPConnection;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
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
						            prefs.getString("host", ""), 
				                    prefs.getString("port", "5222"));
	}
	
	public void desconectar() {
		if (this.xmppConnection != null)
			if (this.xmppConnection.isConnected())
				this.xmppConnection.disconnect();
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
	
	class ConectarXmpp extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			if (xmppConnection != null) {
				if (xmppConnection.isConnected()) {
					return "Já existe uma conexão aberta.";
				}
			}
			publishProgress("Passando parâmetros da conexão...");
			
			String username = params[0];
			String password = params[1];
			String host     = params[2];
			Integer port    = Integer.parseInt(params[3]);
			
			ConnectionConfiguration config = new ConnectionConfiguration(host, port);
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
			config.setSASLAuthenticationEnabled(prefs.getBoolean("sas", false));
			xmppConnection = new XMPPConnection(config);
			try {
				publishProgress("Iniciando conexão...");
				xmppConnection.connect();
				xmppConnection.login(username, password);
			} catch (Exception e) {
				Log.i("TESTE", e.getMessage());
				return e.getMessage();
			}
			return "Conectado.";
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(XMPPApplication.this, result, Toast.LENGTH_LONG);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			Toast.makeText(XMPPApplication.this, values[0], Toast.LENGTH_LONG);
		}
		
		
	}
}
