package de.fhkoeln.gm.poc1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onButtonClick(View view) {
		switch ( view.getId() ) {
			case R.id.press_me:
				showToast();
				break;
			case R.id.press_me2:
				showDialog();
				break;
			case R.id.press_me3:
				showSelectDialog();
				break;
		}
	}

	private void showSelectDialog() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder
		 	.setTitle(R.string.dialog_title)
		 	.setItems(R.array.dialog_select, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast toast = Toast.makeText(
							MainActivity.this,
							getResources().getStringArray(R.array.dialog_select)[which],
							Toast.LENGTH_SHORT
					);
					toast.show();
					dialog.dismiss();
				}
			})
			.show();
	}
	
	private void showDialog() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder
		 	.setTitle(R.string.dialog_title)
		 	.setIcon(android.R.drawable.ic_dialog_info)
		 	.setMessage(R.string.dialog_message)
		 	.setPositiveButton(R.string.dialog_button, null)
		 	.setNegativeButton(android.R.string.cancel, null)
		 	.show();
		
	}

	private void showToast() {
		Toast toast = Toast.makeText(this, R.string.toast_message, Toast.LENGTH_SHORT );
		toast.setGravity(Gravity.TOP | Gravity.LEFT, 20, 20 );
		toast.show();
	}
	
}
