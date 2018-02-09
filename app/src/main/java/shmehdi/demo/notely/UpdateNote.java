package shmehdi.demo.notely;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by rizz on 07-02-2018.
 */

public class UpdateNote extends AppCompatActivity {

    EditText titleEt,noteEt;
    NoteModel noteModel;
    Database database;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //find view
        titleEt = findViewById(R.id.titleEt);
        noteEt = findViewById(R.id.noteEt);

        noteModel = (NoteModel) getIntent().getSerializableExtra("note");

        titleEt.setText(noteModel.getTitle());
        noteEt.setText(noteModel.getNote());

        database = new Database(this);


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.undo){
            finish();
        }
        if(item.getItemId()==R.id.save){
            String title = titleEt.getText().toString();
            if(title.isEmpty()){
                titleEt.setError("Please give a title");
            }
            else {
                String note = noteEt.getText().toString();
                if(database.updateNote(noteModel.getNoteID(),title,note)>0)
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
