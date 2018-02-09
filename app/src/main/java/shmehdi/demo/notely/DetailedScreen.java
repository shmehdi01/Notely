package shmehdi.demo.notely;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailedScreen extends AppCompatActivity {

    TextView title,note,date;

    NoteModel noteModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteModel = (NoteModel) getIntent().getSerializableExtra("note");
        //find view
        title = findViewById(R.id.title);
        note = findViewById(R.id.note);
        date = findViewById(R.id.date);

        title.setText(noteModel.getTitle());
        note.setText(noteModel.getNote());
        date.setText("Last updated: " + MyFunction.customDate(noteModel.getDate()) + " at " + noteModel.getTime());


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.edit){
            Intent intent = new Intent(DetailedScreen.this,UpdateNote.class);
            intent.putExtra("note",noteModel);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
