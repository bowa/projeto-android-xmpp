package idez.xmppteste;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		
		String buddy = "";
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
			this.finish();
			break;
		}
		return true;
	}

	public void iniciarChat(String buddy) {
		currentChat = chatManager.createChat(buddy, new MessageListener() {
			@Override
			public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message msg) {
				((EditText)findViewById(R.id.editTextConversa)).append(msg.getBody());
			};
		});
	}

	public void enviarMsg() {
		try {
			currentChat.sendMessage(((TextView)findViewById(R.id.editTextMsg)).getText().toString());
		} 
		catch (XMPPException e) {
			e.printStackTrace();
		}
	}
}
