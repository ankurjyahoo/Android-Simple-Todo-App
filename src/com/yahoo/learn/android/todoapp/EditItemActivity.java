package com.yahoo.learn.android.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	private int itemPosition;
	private EditText etEditItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		Intent intent = getIntent();
		String itemText = intent.getExtras().getString(TodoActivity.EXTRA_EDITED_TEXT);
		itemPosition = intent.getExtras().getInt(TodoActivity.EXTRA_EDITED_POS);
		
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		etEditItem.setText(itemText);
	}
	
	
	public void saveItem(View v) {
		Intent resultData = new Intent();
		resultData.putExtra(TodoActivity.EXTRA_EDITED_TEXT, etEditItem.getText().toString());
		resultData.putExtra(TodoActivity.EXTRA_EDITED_POS, itemPosition);
		
		setResult(RESULT_OK, resultData);
		finish();
	}

}
