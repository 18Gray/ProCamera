package com.eighteengray.procamera.model.database;

import android.database.sqlite.SQLiteDatabase;

public abstract class Upgrade
{
    public abstract void update(SQLiteDatabase db);
}
