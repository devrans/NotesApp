package api.notesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    static ArrayList<String> noteList;
    static ArrayAdapter<String> arrayAdapter;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private boolean isLong = false;

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

            startActivity(intent);
            intent.putExtra("tappedItemList", -1);
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(getApplicationContext(), NoteActivity.class);
        sharedPreferences = getSharedPreferences("api.notesapp", Context.MODE_PRIVATE);



        listView = (ListView) findViewById(R.id.listView);
        noteList = new ArrayList<String>();
        noteList.add("DEFAULT NOTE");

        try {
            noteList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("list", ObjectSerializer.serialize(noteList)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,noteList);
        listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!isLong) {
                        intent.putExtra("tappedItemList", i);
                        startActivity(intent);
                    }

                }
            });



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int idA, long l) {
                isLong = true;
                final int id = idA;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setMessage("DO YOU WANT TO REMOVE THIS NOTE?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                noteList.remove(id);
                                isLong = false;
                                arrayAdapter.notifyDataSetChanged();
                                try {
                                    sharedPreferences.edit().putString("list", ObjectSerializer.serialize(MainActivity.noteList)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isLong = false;

                            }
                        }).show();

                return false;
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();



    }
}
