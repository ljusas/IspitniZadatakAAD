package com.example.androiddevelopment.infoagency.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by androiddevelopment on 20.3.18..
 */
@DatabaseTable(tableName = Coment.TABLE_NAME_USERS)
public class Coment {

    public static final String TABLE_NAME_USERS = "coment";

    public static final String FIELD_NAME_ID     = "id";
    public static final String FIELD_NAME_TITLE   = "title";
    public static final String FIELD_NAME_DESCRIPTION   = "description";
    public static final String FIELD_NAME_AUTHOR   = "author";
    public static final String FIELD_NAME_DATE   = "date";
    public static final String FIELD_NAME_NEWS = "news";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int comentId;

    @DatabaseField(columnName = FIELD_NAME_TITLE)
    private String title;

    @DatabaseField(columnName = FIELD_NAME_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = FIELD_NAME_AUTHOR)
    private String author;

    @DatabaseField(columnName = FIELD_NAME_DATE)
    private String date;

    @DatabaseField(columnName = FIELD_NAME_NEWS, foreign = true, foreignAutoRefresh = true)
    private News news;

    public Coment() {
    }

    public int getComentId() {
        return comentId;
    }

    public void setComentId(int agentId) {
        this.comentId = comentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return "Coment: " + title;
    }
}
