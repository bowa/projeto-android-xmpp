package idez.xmppteste;

import java.util.ArrayList;
import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ChatActivity extends Activity {
	private Chat currentChat;
	private ChatManager chatManager;
	private String buddy = "";
	ListView janelaConversa;
	private static String NOMEKEY = "nome";
	private static String MENSAGEMKEY = "mensagem";
	private ArrayList<HashMap<String, String>> mensagens = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter adapter;
	private XMPPApplication application;
	private Handler myHandler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		this.application = (XMPPApplication) this.getApplication();
		this.janelaConversa = (ListView) findViewById(R.id.listView1);
		this.adapter = new SimpleAdapter(	this, 
											this.mensagens, 
											R.layout.chat_list, 
											new String[] {NOMEKEY, MENSAGEMKEY}, 
											new int[] {R.id.chatListWho, R.id.chatListMessage}
											);
		this.janelaConversa.setAdapter(this.adapter);
		
		
		
		chatManager = this.application.getXmppConnection().getChatManager();
		Intent buddiesIntent = getIntent();
		if (buddiesIntent != null) {
			buddy = buddiesIntent.getStringExtra("email");				
		}		
		
		this.iniciarChat(buddy);
		
		Button botaoEnviar = (Button) findViewById(R.id.botaoEnviar);
		botaoEnviar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				myHandler.post(new Runnable() {
					@Override
					public void run() {
						enviarMsg();
					}
				});
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.chat_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemCloseChat:
			this.finish();
			break;
		}
		return true;
	}
	
	public void iniciarChat(String buddy) {
		currentChat = chatManager.createChat(buddy, new MessageListener() {
			@Override
			public void processMessage(Chat chat, Message msg) {
				Log.i("MessageProcess", msg.getBody());
			};
		});
		application.getXmppConnection().addPacketListener(
				new PacketListener() {
					@Override
					public void processPacket(Packet packet) {
		                Message message = (Message) packet;
		                if (message.getBody() != null) {
		                	final String msg = message.getBody();
		                    final String fromName = StringUtils.parseBareAddress(message.getFrom());
		                    if (fromName.equals(ChatActivity.this.buddy)) {
				                    myHandler.post(new Runnable() {
										@Override
										public void run() {
						                    receber_mensagem(fromName, msg);
										}
									});
		                    }
	        			}
					}
				}, new MessageTypeFilter(Message.Type.chat));
	}

	public void receber_mensagem(String who, String msg) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put(NOMEKEY, who);
		hm.put(MENSAGEMKEY, msg);
		mensagens.add(hm);
		adapter.notifyDataSetChanged();
	}
	
	public void enviarMsg() {
		try {
			final String msg = ((TextView)findViewById(R.id.editTextMsg)).getText().toString();
			currentChat.sendMessage(msg);
			((TextView)findViewById(R.id.editTextMsg)).setText("");
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(NOMEKEY, "Eu");
			hm.put(MENSAGEMKEY, msg);
			mensagens.add(hm);
			adapter.notifyDataSetChanged();
		} 
		catch (XMPPException e) {
			e.printStackTrace();
		}
	}
}
