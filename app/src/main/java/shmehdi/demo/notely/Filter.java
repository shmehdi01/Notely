package shmehdi.demo.notely;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rizz on 09-02-2018.
 */

public class Filter {

    public static final String HEARTED = "Hearted";
    public static final String FAVOURITE = "Favourite";
    public static final String STORY = "Story";
    public static final String POEMS = "Poems";

    private static List<NoteModel> filteredNotes = new ArrayList<>();

    public static List<NoteModel> getFavList(List<NoteModel> listNotes){
        NoteModel noteModel;
        for(int i=0;i<listNotes.size();i++){
            noteModel = listNotes.get(i);
            if(noteModel.getFav().equalsIgnoreCase("yes")){
                filteredNotes.add(noteModel);

            }
        }
        return filteredNotes;
    }

    public static List<NoteModel> removeFavList(List<NoteModel> listNotes){
        NoteModel noteModel;
        for(int i=0;i<listNotes.size();i++){
            noteModel = listNotes.get(i);
            if(noteModel.getFav().equalsIgnoreCase("yes")){
                filteredNotes.remove(noteModel);

            }
        }
        return filteredNotes;
    }

    public static List<NoteModel> removeStarredList(List<NoteModel> listNotes){
        NoteModel noteModel;
        for(int i=0;i<listNotes.size();i++){
            noteModel = listNotes.get(i);
            if(noteModel.getStar().equalsIgnoreCase("yes")){
                filteredNotes.remove(noteModel);
            }
        }
        return filteredNotes;
    }

    public static List<NoteModel> getStarredList(List<NoteModel> listNotes){
        NoteModel noteModel;
        for(int i=0;i<listNotes.size();i++){
            noteModel = listNotes.get(i);
            if(noteModel.getStar().equalsIgnoreCase("yes")){
                filteredNotes.add(noteModel);
            }
        }
        return filteredNotes;
    }


    public static List<NoteModel> getCategoryList(List<NoteModel> listNotes,String tag){
        NoteModel noteModel;
        for(int i=0;i<listNotes.size();i++){
            noteModel = listNotes.get(i);
            if(noteModel.getCategory().equalsIgnoreCase(tag)){
                filteredNotes.add(noteModel);
            }
        }
        return filteredNotes;
    }
    public static List<NoteModel> removeCategoryList(List<NoteModel> listNotes,String tag){
        NoteModel noteModel;
        for(int i=0;i<listNotes.size();i++){
            noteModel = listNotes.get(i);
            if(noteModel.getCategory().equalsIgnoreCase(tag)){
                filteredNotes.remove(noteModel);
            }
        }
        return filteredNotes;
    }

}
