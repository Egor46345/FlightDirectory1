package com.example.flightdirectory;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Shop extends AppCompatActivity implements View.OnClickListener {

    Button buy;
    TextView quantity, price;

    DBHelper dbHelper;
    SQLiteDatabase database;

    int kol = 0;
    String count = "Количество:\n" + kol;

    float summa;
    String totalAmount = "Сумма:\n"+ summa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        buy = findViewById(R.id.buy);
        buy.setOnClickListener(this);

        quantity = findViewById(R.id.quantity);
        quantity.setText(valueOf(count));

        price = findViewById(R.id.etPrice);
        price.setText(valueOf(totalAmount));

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }

    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID1);
            int startRouteIndex = cursor.getColumnIndex(DBHelper.KEY_START);
            int endRouteIndex = cursor.getColumnIndex(DBHelper.KEY_END);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputStart = new TextView(this);
                params.weight = 3.0f;
                outputStart.setLayoutParams(params);
                outputStart.setText(cursor.getString(startRouteIndex));
                outputStart.setTextSize(12);
                dbOutputRow.addView(outputStart);

                TextView outputEnd = new TextView(this);
                params.weight = 3.0f;
                outputEnd.setLayoutParams(params);
                outputEnd.setText(cursor.getString(endRouteIndex));
                outputEnd.setTextSize(12);
                dbOutputRow.addView(outputEnd);

                TextView outputPrice = new TextView(this);
                params.weight = 3.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(priceIndex));
                outputPrice.setTextSize(12);
                dbOutputRow.addView(outputPrice);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Добавить в\nкорзину");
                deleteBtn.setTextSize(12);
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy:
                if(kol != 0){
                    Toast.makeText(this, "Вы приобрели " + kol + " билета(ов) по цене " + summa + " рублей", Toast.LENGTH_LONG).show();
                    kol = 0;
                    count = "Количество:\n" + kol;
                    quantity.setText(count);
                    summa = 0;
                    totalAmount = "Сумма:\n"+ summa;
                    price.setText(totalAmount);
                }
                else{
                    Toast.makeText(this, "В корзину не добавлен ни один билет", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                int index = outputDB.indexOfChild(outputDBRow);
                Cursor cursorUpdater = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                if (cursorUpdater != null) {
                    cursorUpdater.moveToPosition(index);
                    summa = summa + cursorUpdater.getFloat(3);
                    totalAmount = "Сумма:\n"+ summa + " рублей";
                }
                price.setText(valueOf(totalAmount));
                kol++;
                count = "Количество:\n" + kol;
                quantity.setText(count);
                break;
        }
    }
}