package shmehdi.demo.notely;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, DrawerLayout.DrawerListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView filterListView, recyclerViewNotes;
    private ImageView closeFilterBtn;
    private TextView emptTxt;
    private Button applyBtn;

    //Menu on ActionBar
    Menu optionMenu;

    Database database;


    String[] listFilter = {"Hearted", "Favourite", "Poems", "Story"};
    NoteAdapter adapter;
    List<NoteModel>listNotes = new ArrayList<>();

    ArrayList<String> selectedFilter = new ArrayList<>();
    List<NoteModel>filteredNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notely");

        //find views
        filterListView = findViewById(R.id.slider_list);
        drawerLayout = findViewById(R.id.drawer_layout);
        closeFilterBtn = findViewById(R.id.clear);
        recyclerViewNotes = findViewById(R.id.noteRcv);
        emptTxt = findViewById(R.id.emptTxt);
        applyBtn = findViewById(R.id.btnApply);

        filterListView.setLayoutManager(new LinearLayoutManager(this));
        filterListView.setAdapter(new FilterAdapter(listFilter));



        drawerToggle = new ActionBarDrawerToggle(this,
                                                    drawerLayout,
                                                    toolbar,
                                                    R.string.app_name,R.string.app_name);

        closeFilterBtn.setOnClickListener(this);

        applyBtn.setOnClickListener(this);

        drawerLayout.addDrawerListener(this);



        database = new Database(this);


    }

    private void setNoteList(List<NoteModel> noteList) {
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(noteList);
        recyclerViewNotes.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SwipeHelper((NoteAdapter) adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerViewNotes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listNotes = database.fetchNotes();
        setNoteList(listNotes);
    }

    @Override
    protected void onStop() {
        super.onStop();
        filteredNotes.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        optionMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.filter){
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
        if(item.getItemId()==R.id.add){
            startActivity(new Intent(MainActivity.this,NewNote.class));
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.clear:
                drawerLayout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.btnApply:
                setNoteList(filteredNotes);
                if(filteredNotes.size()==0){
                    Collections.reverse(listNotes);
                    setNoteList(listNotes);
                }


        }
    }


    private void hideActionMenu(Boolean b) {
        MenuItem item = optionMenu.findItem(R.id.add);
        item.setVisible(!b);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        hideActionMenu(true);
    }


    @Override
    public void onDrawerClosed(View drawerView) {
       hideActionMenu(false);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }


    class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder>{

        String[] listFilter;

        public FilterAdapter(String[] listFilter) {
            this.listFilter = listFilter;
        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.filter_list,parent,false));
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            String text = listFilter[position];
            holder.tv.setText(text);
            holder.checkImg.setImageResource(R.drawable.ic_action_check);


            holder.tv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        holder.makeActive();
                        selectedFilter.add(holder.tv.getText().toString());
                        if(holder.tv.getText().toString().equalsIgnoreCase(Filter.HEARTED)){
                            filteredNotes = Filter.getFavList(listNotes);
                            filteredNotes = new ArrayList<>(new LinkedHashSet<>(filteredNotes));
                        }
                        if(holder.tv.getText().toString().equalsIgnoreCase(Filter.FAVOURITE)){
                            filteredNotes = Filter.getStarredList(listNotes);
                            filteredNotes = new ArrayList<>(new LinkedHashSet<>(filteredNotes));
                        }
                        if(holder.tv.getText().toString().equalsIgnoreCase(Filter.STORY)){
                            filteredNotes = Filter.getCategoryList(listNotes,"Story");
                            filteredNotes = new ArrayList<>(new LinkedHashSet<>(filteredNotes));
                        }
                        if(holder.tv.getText().toString().equalsIgnoreCase(Filter.POEMS)){
                            filteredNotes = Filter.getCategoryList(listNotes,"Poem");
                            filteredNotes = new ArrayList<>(new LinkedHashSet<>(filteredNotes));
                        }

                    }
                    else {
                        holder.makeDeactive();
                        selectedFilter.remove(holder.tv.getText().toString());
                        if(holder.tv.getText().toString().equalsIgnoreCase(Filter.HEARTED)){
                            filteredNotes = Filter.removeFavList(listNotes);
                            filteredNotes = new ArrayList<>(new LinkedHashSet<>(filteredNotes));
                        }
                        if(holder.tv.getText().toString().equalsIgnoreCase(Filter.FAVOURITE)){
                            filteredNotes = Filter.removeStarredList(listNotes);
                            filteredNotes = new ArrayList<>(new LinkedHashSet<>(filteredNotes));
                        }
                        if(holder.tv.getText().toString().equalsIgnoreCase(Filter.STORY)){
                            filteredNotes = Filter.removeCategoryList(listNotes,"Story");
                            filteredNotes = new ArrayList<>(new LinkedHashSet<>(filteredNotes));
                        }
                        if(holder.tv.getText().toString().equalsIgnoreCase(Filter.POEMS)){
                            filteredNotes = Filter.removeCategoryList(listNotes,"Poem");
                            filteredNotes = new ArrayList<>(new LinkedHashSet<>(filteredNotes));
                        }

                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return listFilter.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            CheckBox tv;
            ImageView checkImg;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
                checkImg = itemView.findViewById(R.id.check);
            }

            private void makeActive(){
                checkImg.setImageResource(R.drawable.ic_action_check_active);
                tv.setTextColor(Color.parseColor("#33B5E5"));
            }
            private void makeDeactive(){
                checkImg.setImageResource(R.drawable.ic_action_check);
                tv.setTextColor(Color.parseColor("#FFFFFF"));
            }

        }
    }

    class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>{

        List<NoteModel> noteModels;

        public NoteAdapter(List<NoteModel> noteModels) {
            this.noteModels = noteModels;
            Collections.reverse(noteModels); // To get recent first

            checkIFNoteEmpty(noteModels); //show Empty Note

        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final NoteModel noteModel = noteModels.get(position);
            holder.noteID.setText(noteModel.getNoteID());
            holder.title.setText(noteModel.getTitle().substring(0,1).toUpperCase() + noteModel.getTitle().substring(1).toLowerCase() );
            holder.gist.setText( MyFunction.createGist(noteModel.getNote()));
            holder.datetime.setText(MyFunction.customDate(noteModel.getDate())+ " at " +noteModel.getTime());

            holder.setNoteModel(noteModel);

            if(noteModel.getFav().equalsIgnoreCase("Yes"))
                holder.heart.setImageResource(R.drawable.ic_action_check_fav);
            else holder.heart.setImageResource(R.drawable.ic_action_uncheck_fav);
            if(noteModel.getStar().equalsIgnoreCase("Yes"))
                holder.star.setImageResource(R.drawable.ic_action_check_star);
            else holder.star.setImageResource(R.drawable.ic_action_uncheck_star);

            //add to fav
            holder.heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(noteModel.getFav().equalsIgnoreCase("No")){
                        holder.heart.setImageResource(R.drawable.ic_action_check_fav);
                        database.setFav(noteModel.getNoteID(),"Yes");
                    }
                    if(noteModel.getFav().equalsIgnoreCase("Yes")){
                        holder.heart.setImageResource(R.drawable.ic_action_uncheck_fav);
                        database.setFav(noteModel.getNoteID(),"No");
                    }

                }
            });
            //mark star
            holder.star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(noteModel.getStar().equalsIgnoreCase("No")){
                        holder.star.setImageResource(R.drawable.ic_action_check_star);
                        database.setStar(noteModel.getNoteID(),"Yes");
                    }
                    if(noteModel.getStar().equalsIgnoreCase("Yes")){
                        holder.star.setImageResource(R.drawable.ic_action_uncheck_star);
                        database.setFav(noteModel.getNoteID(),"No");
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return noteModels.size();
        }

        //Remove Note
        public void removeFav(int pos, RecyclerView.ViewHolder viewHolder){
            noteModels.remove(pos);
            TextView noteID =  viewHolder.itemView.findViewById(R.id.note_id);
            database.deleteNote(noteID.getText().toString()); //delete from database
            this.notifyItemRemoved(pos);
            checkIFNoteEmpty(noteModels); //show 'Empty Note' if empty
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title,gist,datetime,noteID;
            ImageView heart,star;
            public MyViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                gist = itemView.findViewById(R.id.gist);
                datetime = itemView.findViewById(R.id.datetime);
                heart = itemView.findViewById(R.id.heart);
                star = itemView.findViewById(R.id.star);
                noteID = itemView.findViewById(R.id.note_id);

                itemView.setOnClickListener(this);
            }

            NoteModel noteModel;
            public void setNoteModel(NoteModel noteModel){
                this.noteModel = noteModel;
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,DetailedScreen.class);
                intent.putExtra("note", (Serializable) noteModel);
                startActivity(intent);
            }
        }
    }

    private void checkIFNoteEmpty(List<NoteModel> noteModels) {
        if(noteModels.size()!=0)
            emptTxt.setVisibility(View.GONE);
        else
            emptTxt.setVisibility(View.VISIBLE);
    }

    class SwipeHelper extends ItemTouchHelper.SimpleCallback{

        NoteAdapter noteAdapter;

        public SwipeHelper(NoteAdapter noteAdapter) {
            super(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT);
            this.noteAdapter = noteAdapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            noteAdapter.removeFav(viewHolder.getAdapterPosition(),viewHolder);

        }

        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            Bitmap icon;
            Paint p = new Paint();
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;


             if(dX<0){
                 p.setColor(Color.parseColor("#D32F2F"));
                 RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                 c.drawRect(background,p);
                 icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_delete);
                 RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                 c.drawBitmap(icon,null,icon_dest,p);
             }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }


}
