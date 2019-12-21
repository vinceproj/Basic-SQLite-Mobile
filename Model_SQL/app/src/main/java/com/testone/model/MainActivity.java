package com.testone.model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Establish the database name; Find the path to the database
        //Open or Create the database; Create a Table if needed
        String dbName = "model.db";
        File dbFile = this.getDatabasePath(dbName);
        final SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS MODEL (firstName Text, lastName Text, modelNum INTEGER)");

        //Create a LinearLayout for display
        LinearLayout root = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        root.setLayoutParams(params);
        root.setBackgroundColor(Color.LTGRAY);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(10,10,10,10);

        //Create top LinearLayout
        LinearLayout top = new LinearLayout(this);
        top.setLayoutParams(params);
        top.setOrientation(LinearLayout.VERTICAL);
        top.setPadding(0,0,0,20);
        LinearLayout topRow = new LinearLayout(this);
        LinearLayout topRowLeft = new LinearLayout(this);
        LinearLayout topRowMid = new LinearLayout(this);
        LinearLayout topRowRight = new LinearLayout(this);
        topRow.setLayoutParams(params);
        topRowLeft.setLayoutParams(params);
        topRowLeft.setOrientation(LinearLayout.VERTICAL);
        topRowMid.setLayoutParams(params);
        topRowMid.setOrientation(LinearLayout.VERTICAL);
        topRowRight.setLayoutParams(params);
        topRowRight.setOrientation(LinearLayout.VERTICAL);

        //Fill LinearLayouts
        TextView tv;
        final EditText addFirstName = new EditText(this);
        final EditText addLastName = new EditText(this);
        final EditText addModelNum = new EditText(this);
        tv = new TextView(this);
        tv.setText("Enter First Name");
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        topRowLeft.addView(tv);
        topRowLeft.addView(addFirstName);
        tv = new TextView(this);
        tv.setText("Enter Last Name");
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        topRowMid.addView(tv);
        topRowMid.addView(addLastName);
        tv = new TextView(this);
        tv.setText("Enter Model Num");
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        topRowRight.addView(tv);
        topRowRight.addView(addModelNum);

        //Add Column LinearLayouts to topRow
        topRow.addView(topRowLeft);
        topRow.addView(topRowMid);
        topRow.addView(topRowRight);

        //Add topRow to top
        top.addView(topRow);

        //Make button to read EditText and add values to DB
        final Button insertDB = new Button(this);
        insertDB.setText("Insert into DB");
        insertDB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try{
                    String firstName = addFirstName.getText().toString();
                    String lastName = addLastName.getText().toString();
                    Integer modelNum = parseInt(addModelNum.getText().toString());
                if(firstName!="" & lastName !="" & modelNum != null){
                    insertDataModel(firstName, lastName, modelNum);
                    //Lines to end and restart activity
                    finish();
                    startActivity(getIntent());
                }
                }
                catch(Exception exception){
                    Toast.makeText(MainActivity.this, "Please check all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        top.addView(insertDB);

        //Make button to drop table and restart activity
        final Button dropDB = new Button(this);
        dropDB.setText("Drop Table");
        dropDB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                db.execSQL("DROP TABLE MODEL");
                //Lines to end and restart activity
                finish();
                startActivity(getIntent());

            }
        });
        top.addView(dropDB);



        //Create bottom LinearLayout
        LinearLayout bottom = new LinearLayout(this);
        bottom.setLayoutParams(params);
        bottom.setOrientation(LinearLayout.VERTICAL);

        //Display values in table using defined function
        displayAllData(bottom);

        //Add top and bottom layouts to page
        root.addView(top);
        root.addView(bottom);
        //Set display
        setContentView(root);

    }

    //Take three arguments; Place arguments into ContentValue; Put ContentValue into table MODEL
    public void insertDataModel(String firstName, String lastName, Integer modelNum){
        //Establish connection to database
        String dbName = "model.db";
        File dbFile = this.getDatabasePath(dbName);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);

        //Create new ContentValues object; set contentValues to equal function arguments
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("modelNum", modelNum);

        //Insert values into database
        db.insert("MODEL", null, contentValues);

    }

    //Take a desired LinearLayout; Use Cursor object to navigate
    //Set found values into a LinearLayout; Nest that LinearLayout into argument
    public void displayAllData(LinearLayout lv){
        //Establish connection to database
        String dbName = "model.db";
        File dbFile = this.getDatabasePath(dbName);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);

        //Create new Cursor object and set it's query value; Select ALL from table MODEL
        Cursor cursor = db.rawQuery("SELECT * FROM MODEL", null);

        //Cursor is 0 indexed for traversal
        while(cursor.moveToNext()){
            //Grab values from cursor
            String firstName = cursor.getString(0);
            String lastName = cursor.getString(1);
            Integer modelNum = cursor.getInt(2);

            //Create LinearLayout for nesting
            LinearLayout modelDisplay = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            modelDisplay.setLayoutParams(params);

            //Create TextView to append to LinearLayout
            TextView tv = new TextView(this);
            tv.setText(firstName + " ");
            tv.setTextColor(Color.BLACK);
            modelDisplay.addView(tv);
            tv = new TextView(this);
            tv.setText(lastName + " ");
            tv.setTextColor(Color.BLACK);
            modelDisplay.addView(tv);
            tv = new TextView(this);
            tv.setText(modelNum.toString());
            tv.setTextColor(Color.BLACK);
            modelDisplay.addView(tv);

            //Append LinearLayout for nesting
            lv.addView(modelDisplay);
        }
    }
}
