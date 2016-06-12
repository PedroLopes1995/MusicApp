package pt.ipleiria.simplecontactsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> musics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("appMusics", 0);

        //Se nao encontrar a key, vai criar um conjunto vazio
        Set<String> musicsSet = sp.getStringSet("musicsKey", new HashSet<String>());


        musics = new ArrayList<String>(musicsSet);






//         musics = new ArrayList<String>();
//
//         musics.add("Simon Viklund | The Mark | PAYDAY 2 Official Soundtrack | 2013 | 3★");
//         musics.add("ACDC | Thunderstruck | The Razors Edge | 1990 | 5★");
//         musics.add("Darude | Sandstorm | Before the Storm |2000 | 2★");
//         musics.add("Shinedown | Oblivion | Threat to Survival | 2015 | 4★");
//         musics.add("Linkin Park | Numb | Meteora | 2003 | 1★");

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_list_item_1, musics);

        SimpleAdapter adapter = createSimpleAdapter(musics);

        ListView listView = (ListView) findViewById(R.id.listView_musics);
        listView.setAdapter(adapter);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_search);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.spinner_search, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);


        //
        // NEW
        //

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                //codigo que é executado quando se clica
                //num item da listView
                Toast.makeText(MainActivity.this, "Clicou no item" + position, Toast.LENGTH_SHORT).show();


                //Apaga
                musics.remove(position);


                //Refresh
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                //        android.R.layout.simple_list_item_1, oontactos);

                SimpleAdapter adapter = createSimpleAdapter(musics);

                ListView listView = (ListView) findViewById(R.id.listView_musics);
                listView.setAdapter(adapter);
                return false;
            }
        });

    }


    //listview_item
    private SimpleAdapter createSimpleAdapter(ArrayList<String> musics) {
        List<HashMap<String, String>> simpleAdapterData = new ArrayList<HashMap<String, String>>();

        for (String m : musics) {
            HashMap<String, String> hashMap = new HashMap<>();

            String[] split = m.split(" \\| ");

            hashMap.put("artist", split[0]);
            hashMap.put("song", split[1]);
            hashMap.put("album", split[2]);
            hashMap.put("year", split[3]);
            hashMap.put("rating", split[4]);

            simpleAdapterData.add(hashMap);
        }

        String[] from = {"artist", "song", "album", "year", "rating"};
        int[] to = {R.id.textView_artist, R.id.textView_song, R.id.textView_album, R.id.textView_year, R.id.textView_rating};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), simpleAdapterData, R.layout.listview_item, from, to);
        return simpleAdapter;
    }


    //OnStop. Antes da app ser destroida
    @Override
    protected void onStop() {
        super.onStop();

        Toast.makeText(MainActivity.this, "A guardar musicas", Toast.LENGTH_SHORT).show();

        // -------------------------------------------------------- mode pode ser 0, aka Mode_private
        SharedPreferences sp = getSharedPreferences("appMusics", MODE_PRIVATE);

        //sp.edit     alt+enter     primeira opcao
        SharedPreferences.Editor edit = sp.edit();

        //criar conjunto dos contactos
        HashSet musicsSet = new HashSet(musics);

        edit.putStringSet("musicsKey", musicsSet);

        edit.commit();

    }


    //
    // END NEW
    //



    public void onClick_search(View view) {
        EditText et = (EditText) findViewById(R.id.editText_search);
        Spinner sp = (Spinner) findViewById(R.id.spinner_search);
        ListView lv = (ListView) findViewById(R.id.listView_musics);

        ArrayList<String> searchedMusics = new ArrayList<>();

        String termo = et.getText().toString();

        String selectedItem = (String) sp.getSelectedItem();

        if(termo.equals("")){
            SimpleAdapter adapter = createSimpleAdapter(musics);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, R.string.Showing_all, Toast.LENGTH_SHORT).show();
        }

        else if (selectedItem.equals(getString(R.string.All))) {
            for (String m : musics) {

                if (m.contains(termo)) {
                    searchedMusics.add(m);
                }
            }
        }

        else if (selectedItem.equals(getString(R.string.Artist))) {
            for (String m : musics) {
                String[] split = m.split("\\|");
                String artist = split[0];

                if (artist.contains(termo)) {
                    searchedMusics.add(m);
                }
            }
        }
        else if (selectedItem.equals(getString(R.string.Song))){
            for (String m : musics) {
                String[] split = m.split("\\|");
                String song = split[1];

                if (song.contains(termo)) {
                    searchedMusics.add(m);
                }
            }
        }
        else if (selectedItem.equals(getString(R.string.Album))){
            for (String m : musics) {
                String[] split = m.split("\\|");
                String song = split[2];

                if (song.contains(termo)) {
                    searchedMusics.add(m);
                }
            }
        }
        else if (selectedItem.equals(getString(R.string.Year))){
            for (String m : musics) {
                String[] split = m.split("\\|");
                String year = split[3];

                if (year.contains(termo)) {
                    searchedMusics.add(m);
                }
            }
        }
        else if (selectedItem.equals(getString(R.string.Rating))){
            for (String m : musics) {
                String[] split = m.split("\\|");
                String rating = split[4];

                if (rating.contains(termo)) {
                    searchedMusics.add(m);
                }
            }
        }

        boolean vazia = searchedMusics.isEmpty();

        if (!vazia == false) {
            SimpleAdapter adapter = createSimpleAdapter(musics);
            lv.setAdapter(adapter);

        Toast.makeText(MainActivity.this, R.string.Searched_songs, Toast.LENGTH_SHORT).show();
        }
        else {
            SimpleAdapter adapter = createSimpleAdapter(musics);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, R.string.No_songs_found, Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick_add(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add, null));


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(MainActivity.this, R.string.New_added, Toast.LENGTH_SHORT).show();

                AlertDialog al= (AlertDialog) dialog;

                EditText etArtist =
                        (EditText) al.findViewById(R.id.editText_artist);
                EditText etSong =
                        (EditText) al.findViewById(R.id.editText_song);
                EditText etAlbum =
                        (EditText) al.findViewById(R.id.editText_album);
                EditText etYear =
                        (EditText) al.findViewById(R.id.editText_year);
                RatingBar etRating =
                        (RatingBar) al.findViewById(R.id.editText_rating);

                String artist =etArtist.getText().toString();
                String song =etSong.getText().toString();
                String album =etAlbum.getText().toString();
                String year =etYear.getText().toString();
                String rating =String.valueOf(etRating.getRating());
                if(rating.endsWith(".0")) rating = rating.replace(".0" , "");


                String newMusic = artist + " | " + song + " | " + album + " | " + year + " | " + rating + "★";

                musics.add(newMusic);

                SimpleAdapter adapter = createSimpleAdapter(musics);
                ListView listView = (ListView) findViewById(R.id.listView_musics);
                listView.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "Novo contacto adicionado.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.Cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(MainActivity.this, R.string.Cancel_text, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setTitle(R.string.New_song_text);

        builder.setMessage(R.string.Enter_new_text);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}