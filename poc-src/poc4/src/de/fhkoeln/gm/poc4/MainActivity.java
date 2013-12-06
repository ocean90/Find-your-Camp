package de.fhkoeln.gm.poc4;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.fhkoeln.gm.poc4.db.UserTable;
import de.fhkoeln.gm.poc4.model.User;

public class MainActivity extends Activity {

	private UserTable userTable;

	private SQLiteDatabase userDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		userTable = new UserTable(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		userTable.close();

		Toast.makeText( this, "Datenbank geschlossen", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		userDatabase = userTable.getReadableDatabase();

		Toast.makeText( this, "Datenbank geöffnet", Toast.LENGTH_SHORT).show();


		updateNameList();

	}

	private void updateNameList() {
		//Cursor userCursor = userDatabase.rawQuery( "SELECT * FROM user", null);
		Cursor userCursor = userDatabase.query("user", null, null, null, null, null, "name");

		final List<User> users = new ArrayList<User>();

		userCursor.moveToFirst();
		while (!userCursor.isAfterLast()) {
		  User user = cursorToUser(userCursor);
		  users.add(user);
		  userCursor.moveToNext();
		}

		userCursor.close();

		ArrayAdapter<User> adapter = new ArrayAdapter<User>(this,
			android.R.layout.simple_list_item_1, users);

		ListView nameListView = (ListView)findViewById(R.id.name_list);

		nameListView.setAdapter(adapter);
	}

	private User cursorToUser(Cursor cursor) {
		User user = new User();
		user.setId(cursor.getInt(0));
		user.setName(cursor.getString(1));
		return user;
	}

	public void onButtonClick(View view) {
		switch ( view.getId() ) {
		case R.id.buttonSave:
			saveNameToDatabase();
			break;
		}
	}

	private void saveNameToDatabase() {
		InputMethodManager inputManager = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		EditText textName = (EditText) findViewById(R.id.textName);
		String name = textName.getText().toString();

		textName.setText("");

		ContentValues values = new ContentValues();
		values.put("name", name);

		userDatabase.insert( "user", null, values);

		updateNameList();

		Toast.makeText( this, "Name gespeichert.", Toast.LENGTH_SHORT).show();

	}

}
