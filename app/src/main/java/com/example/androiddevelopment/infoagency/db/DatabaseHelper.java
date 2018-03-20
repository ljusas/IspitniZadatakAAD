package com.example.androiddevelopment.infoagency.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.androiddevelopment.infoagency.db.model.Coment;
import com.example.androiddevelopment.infoagency.db.model.News;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;



public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "ormlite.db";

    private static final int    DATABASE_VERSION = 1;

    private Dao<News, Integer> NewsDao = null;
    private Dao<Coment, Integer> ComentDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Coment.class);
            TableUtils.createTable(connectionSource, News.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Coment.class, true);
            TableUtils.dropTable(connectionSource, News.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<News, Integer> getNewsDao() throws SQLException {
        if (NewsDao == null) {
            NewsDao = getDao(News.class);
        }

        return NewsDao;
    }

    public Dao<Coment, Integer> getComentDao() throws SQLException {
        if (ComentDao == null) {
            ComentDao = getDao(Coment.class);
        }

        return ComentDao;
    }

    @Override
    public void close() {
        NewsDao = null;
        ComentDao = null;

        super.close();
    }
}
