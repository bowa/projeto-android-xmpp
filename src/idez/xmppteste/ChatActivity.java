package idez.xmppteste;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends Activity {
	private Chat currentChat;
	private ChatManager chatManager;
	private boolean ISTALKING;
	private String buddy = "";
	EditText janelaConversa = (EditText) findViewById(R.id.editTextConversa);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		
		chatManager = ((XMPPApplication) this.getApplication()).getXmppConnection().getChatManager();
		Intent buddiesIntent = getIntent();
		if (buddiesIntent != null) {
			buddy = buddiesIntent.getStringExtra("email");				
		}		
		
		this.iniciarChat(buddy);
		
		Button botaoEnviar = (Button) findViewById(R.id.botaoEnviar);
		botaoEnviar.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				enviarMsg();
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
			this.ISTALKING = false;
			this.finish();
			break;
		}
		return true;
	}

	public void iniciarChat(String buddy) {
		ISTALKING = true;
		currentChat = chatManager.createChat(buddy, 
				// THIS CODE NEVER GETS CALLED FOR SOME REASON
				new MessageListener() {
			@Override
			public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message msg) {
			};
		});
	}

	public void enviarMsg() {
		try {
			String msg = ((TextView)findViewById(R.id.editTextMsg)).getText().toString();
			currentChat.sendMessage(msg);
			this.janelaConversa.setText(janelaConversa.getText() + msg, TextView.BufferType.EDITABLE);
		} 
		catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receberMensagem() {
		// Accept only messages from friend
		PacketFilter filter = new AndFilter(new PacketTypeFilter(Message.class), new FromContainsFilter(buddy));
		// Collect these messages
		PacketCollector collector = ((XMPPApplication) getApplication()).getXmppConnection().createPacketCollector(filter);
		while (ISTALKING) {
			Packet packet = collector.nextResult();
//			if (Message.class) {
				Message msg = (Message) packet;
				if (msg.getBody() != null) {
//					Log.i(buddy, msg.getBody());
					this.janelaConversa.setText(janelaConversa.getText() + msg.getBody().toString(), TextView.BufferType.EDITABLE);
				}
//			}
		}

	}
}
