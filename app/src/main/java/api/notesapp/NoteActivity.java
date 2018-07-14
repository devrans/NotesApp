package api.notesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;


public class NoteActivity extends AppCompatActivity{
    private int tapped;
    private EditText editText;
    private SharedPreferences sharedPreferences;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.addNote);
        {

            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text_layout);
        editText = (EditText) findViewById(R.id.editText);
        sharedPreferences = getSharedPreferences("api.notesapp", Context.MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        tapped = intent.getIntExtra("tappedItemList", MainActivity.noteList.size());
        if (tapped != -1 && MainActivity.noteList.size() != 0)
        editText.setText(MainActivity.noteList.get(tapped));

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (tapped != -1 && MainActivity.noteList.size() != 0 )
        MainActivity.noteList.set(tapped, editText.getText().toString());
        MainActivity.arrayAdapter.notifyDataSetChanged();
        try {
            sharedPreferences.edit().putString("list", ObjectSerializer.serialize(MainActivity.noteList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
