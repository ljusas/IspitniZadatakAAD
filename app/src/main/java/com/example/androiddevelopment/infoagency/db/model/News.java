package com.example.androiddevelopment.infoagency.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by androiddevelopment on 20.3.18..
 */
@DatabaseTable(tableName = News.TABLE_NAME_USERS)
public class News {

    public static final String TABLE_NAME_USERS = "news";

    public static final String FIELD_NAME_ID     = "id";
    public static final String FIELD_NAME_NAME   = "name";
    public static final String FIELD_NAME_DESCRIPTION   = "description";
    public static final String FIELD_NAME_AUTHOR   = "author";
    public static final String FIELD_NAME_DATE   = "date";
    public static final String FIELD_NAME_COMENT = "coment";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int newsId;

    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String name;

    @DatabaseField(columnName = FIELD_NAME_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = FIELD_NAME_AUTHOR)
    private String author;

    @DatabaseField(columnName = FIELD_NAME_DATE)
    private String date;

    @ForeignCollectionField(columnName = News.FIELD_NAME_COMENT, eager = true)
    private ForeignCollection<Coment> coments;

    public News() {
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int agentId) {
        this.newsId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public ForeignCollection<Coment> getComents() {
        return coments;
    }

    public void setComents(ForeignCollection<Coment> coments) {
        this.coments = coments;
    }

    @Override
    public String toString() {
        return "News title: " + name;
    }
}
