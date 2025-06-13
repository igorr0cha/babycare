package com.mera.babycare

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) : SQLiteOpenHelper(context, "baby_app.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        // Tabela users
        db.execSQL("""
            CREATE TABLE users (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                photo_url TEXT,
                email TEXT,
                password_hash TEXT,
                google_id_token TEXT,
                created_at INTEGER
            );
        """)

        // Tabela babies
        db.execSQL("""
            CREATE TABLE babies (
                id TEXT PRIMARY KEY,
                user_id TEXT NOT NULL,
                name TEXT NOT NULL,
                birth_date INTEGER,
                sex TEXT,
                created_at INTEGER,
                FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
            );
        """)

        // Tabela feedings
        db.execSQL("""
            CREATE TABLE feedings (
                id TEXT PRIMARY KEY,
                baby_id TEXT NOT NULL,
                type TEXT NOT NULL, -- 'amamentação' ou 'fórmula'
                start_time INTEGER,
                end_time INTEGER,
                volume_ml INTEGER,
                notes TEXT,
                created_at INTEGER,
                FOREIGN KEY(baby_id) REFERENCES babies(id) ON DELETE CASCADE
            );
        """)

        // Tabela sleep_sessions
        db.execSQL("""
            CREATE TABLE sleep_sessions (
                id TEXT PRIMARY KEY,
                baby_id TEXT NOT NULL,
                start_time INTEGER,
                end_time INTEGER,
                is_nap INTEGER, -- 0 = false, 1 = true
                notes TEXT,
                created_at INTEGER,
                FOREIGN KEY(baby_id) REFERENCES babies(id) ON DELETE CASCADE
            );
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS sleep_sessions")
        db.execSQL("DROP TABLE IF EXISTS feedings")
        db.execSQL("DROP TABLE IF EXISTS babies")
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }
}