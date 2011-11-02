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
import android.widget.TextView;

public class ChatActivity extends Activity {
	private Chat currentChat;
	private ChatManager chatManager = ((XMPPApplication) this.getApplication()).getXmppConnection().getChatManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		
		String buddy = "";
		
		Intent buddiesIntent = getIntent();
		if (buddiesIntent != null) {
				buddy = buddiesIntent.getStringExtra("email");				
		}		
		
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
			currentChat.sendMessage(((TextView)findViewById(R.id.editTextMsg)).getText().toString());
		} 
		catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
