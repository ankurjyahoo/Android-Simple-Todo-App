package com.yahoo.learn.android.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
//import android.view.Menu;
//import android.view.MenuItem;

public class TodoActivity extends Activity {
	private static final int EDIT_REQUEST_CODE = 35;
	protected static final String EXTRA_EDITED_TEXT = "EditItemString";
	protected static final String EXTRA_EDITED_POS = "EditItemPos";
	
	
	private ArrayList<String> todoItems; 
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		lvItems = (ListView) findViewById(R.id.lvTodoList);
		etNewItem = (EditText) findViewById(R.id.etNewItem);

		initializeItems();
//		populateItems();
		todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
		lvItems.setAdapter(todoAdapter);
		
		lvItems.setOnItemLongClickListener(new EditItemListener());
		lvItems.setOnItemClickListener(new DeleteListener());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent result) {
		if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
			String editedText = result.getExtras().getString(EXTRA_EDITED_TEXT);
			int itemPosition = result.getExtras().getInt(EXTRA_EDITED_POS);
			
			todoItems.set(itemPosition, editedText);
			todoAdapter.notifyDataSetChanged();
			persistItems();
		}
	}
	
	public void addNewItem(View v) {
		todoAdapter.add(etNewItem.getText().toString());
		etNewItem.setText("");
		
		persistItems();
	}

	
//	private void populateItems() {
//		todoItems = new ArrayList<String>();
//
//		todoItems.add("Watch Intro Video");
//		todoItems.add("Setup Environment");
//		todoItems.add("Create Project");
//	}

	
	private void initializeItems() {
		File filesDir = getFilesDir();
		File todoListFile = new File(filesDir, "todo.txt");
		try {
			todoItems = new ArrayList<String>(FileUtils.readLines(todoListFile));
		} catch (Exception ex) {
			todoItems = new ArrayList<String>();
		}
		
	}
	
	private void persistItems() {
		File filesDir = getFilesDir();
		File todoListFile = new File(filesDir, "todo.txt");
		try {
			FileUtils.writeLines(todoListFile, todoItems);
		} catch (IOException ex) {
			ex.printStackTrace();
		}	
	}


	private final class EditItemListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			todoItems.remove(position);
			todoAdapter.notifyDataSetChanged();
//			todoAdapter.remove(todoItems.get(position)); // Doesnt work with duplicate items

			persistItems();
			return true;
		}
	}


	private final class DeleteListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(TodoActivity.this, EditItemActivity.class);
			intent.putExtra(EXTRA_EDITED_TEXT, todoItems.get(position));
			intent.putExtra(EXTRA_EDITED_POS, position);
			startActivityForResult(intent, EDIT_REQUEST_CODE);
		}
	}
}
