package shmehdi.demo.notely;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewNote extends AppCompatActivity {

    String[] categoryList = {"--choose category--","Poem","Story"};
    EditText titleEt,noteEt;

    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //find view
        titleEt = findViewById(R.id.titleEt);
        noteEt = findViewById(R.id.noteEt);

        database = new Database(this);



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.cancel){
            finish();
        }
        if(item.getItemId()==R.id.save){
            String title = titleEt.getText().toString();
            if(title.isEmpty()){
                titleEt.setError("Please give a title");
            }
            else {
                showSaveDialog(title);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSaveDialog(final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewNote.this);
        builder.setTitle("Save " + title+ " ?");
        View view = getLayoutInflater().inflate(R.layout.save_confirm,null,false);
        final Spinner categoryOption = view.findViewById(R.id.category_opt);
        categoryOption.setAdapter(new ArrayAdapter<>(NewNote.this,android.R.layout.simple_list_item_1,categoryList));
        builder.setView(view);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String note = noteEt.getText().toString();
                if(note.isEmpty()){
                    Toast.makeText(NewNote.this, "Note is Empty", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }else {
                    String cat = categoryOption.getSelectedItem().toString();
                    if(cat.equalsIgnoreCase("--choose category--"))
                        cat = "none";
                    //Log.d("save",title + " " + note + " " + cat);
                    long id = database.saveNote(title,note,cat);
                    if(id>0){
                        Toast.makeText(NewNote.this, "Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(NewNote.this, "Failed", Toast.LENGTH_SHORT).show();
                    }

                    dialogInterface.dismiss();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


}
