package com.example.androiddevelopment.infoagency.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddevelopment.infoagency.R;
import com.example.androiddevelopment.infoagency.db.DatabaseHelper;
import com.example.androiddevelopment.infoagency.db.model.Coment;
import com.example.androiddevelopment.infoagency.db.model.News;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private News a;
    private DatePickerDialog.OnDateSetListener notesDate;
    String chosenDate;
    Integer i = 0, d = 0;
    String likes, dislikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        int newsID = getIntent().getExtras().getInt("newsID");

        try {
            a = getDatabaseHelper().getNewsDao().queryForId(newsID);

            TextView name1 = findViewById(R.id.tv_newsname);
            name1.setText(a.getName());

            TextView description1 = findViewById(R.id.tv_newsdescription);
            description1.setText(a.getDescription());

            TextView author1 = findViewById(R.id.tv_newsauthor);
            author1.setText(a.getAuthor());

            TextView date1 = findViewById(R.id.tv_newsdate);
            date1.setText(a.getDate());

            Button like = (Button)findViewById(R.id.button_like);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i++;
                }
            });

            Button dislike = (Button) findViewById(R.id.button_dislike);
            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    d++;
                }
            });

            likes = "Likes " + i;
            TextView likes1 = findViewById(R.id.tv_likes);
            likes1.setText(likes);

            dislikes = "Dislikes " + d;
            TextView dislikes1 = findViewById(R.id.tv_dislikes);
            dislikes1.setText(dislikes);



        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = findViewById(R.id.list_coment);
        try {
            List<Coment> list = getDatabaseHelper().getComentDao().queryBuilder()
                    .where()
                    .eq(Coment.FIELD_NAME_NEWS, a.getNewsId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Coment m = (Coment) listView.getItemAtPosition(position);

                    Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                    intent.putExtra("comentID", m.getComentId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_coment){
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.input_coment);

            final EditText comentTitle = dialog.findViewById(R.id.coment_title);
            final EditText comentDescription = dialog.findViewById(R.id.coment_description);
            final EditText comentAuthor = dialog.findViewById(R.id.coment_author);
            final TextView comentDate = dialog.findViewById(R.id.coment_date);

            comentDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog1 = new DatePickerDialog(
                            SecondActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            notesDate,
                            year,month,day);
                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog1.show();
                }
            });

            notesDate = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    chosenDate = String.format("%s/%d/%d", year, month, day);
                }
            };

            Button ok = dialog.findViewById(R.id.button_coment_add);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = comentTitle.getText().toString();
                    String description = comentDescription.getText().toString();
                    String author = comentAuthor.getText().toString();

                    Coment coment = new Coment();
                    coment.setTitle(name);
                    coment.setDescription(description);
                    coment.setAuthor(author);
                    coment.setDate(chosenDate);
                    coment.setNews(a);


                    try {
                        getDatabaseHelper().getComentDao().create(coment);
                        refreshComent();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(SecondActivity.this, R.string.coment_saved, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            Button cancel = dialog.findViewById(R.id.button_coment_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    Toast.makeText(SecondActivity.this, R.string.coment_no_save, Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();



        }


        return super.onOptionsItemSelected(item);
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private void refreshComent() {
        ListView listview = findViewById(R.id.list_coment);

        if (listview != null){
            ArrayAdapter<Coment> adapter = (ArrayAdapter<Coment>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Coment> list = getDatabaseHelper().getComentDao().queryBuilder()
                            .where()
                            .eq(Coment.FIELD_NAME_NEWS, a.getNewsId())
                            .query();

                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}
