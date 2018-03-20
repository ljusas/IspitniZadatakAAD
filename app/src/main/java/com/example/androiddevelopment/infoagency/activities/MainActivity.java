package com.example.androiddevelopment.infoagency.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.androiddevelopment.infoagency.db.model.News;
import com.example.androiddevelopment.infoagency.dialog.AboutDialog;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper databaseHelper;
    private DatePickerDialog.OnDateSetListener notesDate;
    String chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"ljusa@sbb.rs"});
                Email.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.subject));
                Email.putExtra(Intent.EXTRA_TEXT, getString(R.string.developer) + "");
                startActivity(Intent.createChooser(Email, getString(R.string.feedback)));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ListView listView = findViewById(R.id.list_news);

        try {
            List<News> list = getDatabaseHelper().getNewsDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    News ac = (News) listView.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra("newsID", ac.getNewsId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_news) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.input_dialog);

            final EditText newsName = dialog.findViewById(R.id.news_name);
            final EditText newsDescription = dialog.findViewById(R.id.news_description);
            final EditText newsAuthor = dialog.findViewById(R.id.news_author);
            final TextView newsDate = dialog.findViewById(R.id.news_date);

            newsDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog1 = new DatePickerDialog(
                            MainActivity.this,
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


            Button ok = dialog.findViewById(R.id.button_news_add);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = newsName.getText().toString();
                    String description = newsDescription.getText().toString();
                    String author = newsAuthor.getText().toString();


                    News news = new News();
                    news.setName(name);
                    news.setDescription(description);
                    news.setAuthor(author);
                    news.setDate(chosenDate);


                    try {
                        getDatabaseHelper().getNewsDao().create(news);
                        refresh();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MainActivity.this, R.string.news_saved, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_all_news) {
            Toast.makeText(this, R.string.all_news_are, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_info) {
            AlertDialog dialog = new AboutDialog(MainActivity.this).prepareDialog();
            dialog.show();

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        ListView listview = findViewById(R.id.list_news);

        if (listview != null){
            ArrayAdapter<News> adapter = (ArrayAdapter<News>) listview.getAdapter();
            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<News> list = getDatabaseHelper().getNewsDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
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
