package com.example.flightdirectory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText usernameField, passwordField;
    Button loginBtn, passwordBtn;

    DBHelper dbHelper;
    SQLiteDatabase database;

    String adminUser = "admin";
    String adminPassword = "admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        passwordBtn = findViewById(R.id.passwordBtn);
        passwordBtn.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        usernameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    usernameField.setHint("");
                else
                    usernameField.setHint("Логин");
            }
        });

        passwordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    passwordField.setHint("");
                else
                    passwordField.setHint("Пароль");
            }
        });

        // Добавление админа
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_NAME, adminUser);
        contentValues.put(DBHelper.KEY_PASSWORD, adminPassword);

        database.insert(DBHelper.TABLE_USERS, null, contentValues);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.loginBtn:
                Cursor logCursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

                boolean logged = false;
                if(logCursor.moveToFirst()){
                    int usernameIndex = logCursor.getColumnIndex(DBHelper.KEY_NAME);
                    int passwordIndex = logCursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                    do{
                        if(usernameField.getText().toString().equals(adminUser) && passwordField.getText().toString().equals(adminPassword)) {
                            startActivity(new Intent(this, base_data.class));
                            logged = true;
                            break;
                        }
                        if(usernameField.getText().toString().equals(logCursor.getString(usernameIndex)) && passwordField.getText().toString().equals(logCursor.getString(passwordIndex))){
                            startActivity(new Intent(this, Shop.class));
                            logged = true;
                            break;
                        }
                    }while (logCursor.moveToNext());
                }
                logCursor.close();
                if(!logged) Toast.makeText(this, "Введённая комбинация логина и пароля не была найдена", Toast.LENGTH_LONG).show();
                break;
            case R.id.passwordBtn:
                Cursor signCursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

                boolean finded = false;
                if(signCursor.moveToFirst()){
                    int usernameIndex = signCursor.getColumnIndex(DBHelper.KEY_NAME);
                    do{
                        if(usernameField.getText().toString().equals(signCursor.getString(usernameIndex))){
                            Toast.makeText(this, "Введённый логин уже зарегистрирован", Toast.LENGTH_LONG).show();
                            finded = true;
                            break;
                        }
                    }while (signCursor.moveToNext());
                }
                if(!finded){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_NAME, usernameField.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD, passwordField.getText().toString());
                    database.insert(DBHelper.TABLE_USERS, null, contentValues);
                    Toast.makeText(this, "Вы успешно зарегистрированы", Toast.LENGTH_LONG).show();
                }
                signCursor.close();
                break;
        }
    }
}