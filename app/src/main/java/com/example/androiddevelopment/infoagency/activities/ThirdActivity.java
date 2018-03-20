package com.example.androiddevelopment.infoagency.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.androiddevelopment.infoagency.R;
import com.example.androiddevelopment.infoagency.db.DatabaseHelper;
import com.example.androiddevelopment.infoagency.db.model.Coment;
import com.example.androiddevelopment.infoagency.db.model.News;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

public class ThirdActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private Coment a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int comentID = getIntent().getExtras().getInt("comentID");

        try {
            a = getDatabaseHelper().getComentDao().queryForId(comentID);

            TextView name1 = findViewById(R.id.tv_coment_title);
            name1.setText(a.getTitle());

            TextView description1 = findViewById(R.id.tv_coment_description);
            description1.setText(a.getDescription());

            TextView author1 = findViewById(R.id.tv_coment_author);
            author1.setText(a.getAuthor());

            TextView date1 = findViewById(R.id.tv_coment_date);
            date1.setText(a.getDate());

        } catch (SQLException e) {
            e.printStackTrace();
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
