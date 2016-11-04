package xyz.damonwong.todolist;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    EditText addEdit;
    ImageButton button;
    DbHelper dbOpenHelper;
    ItemsAdapter itemsAdapter;
    List<Items> itemsList=new ArrayList<Items>();
    GestureDetector gdt=new GestureDetector(new GestureListener());
    int itemid=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=(ListView) findViewById(R.id.listView);
        addEdit=(EditText)findViewById(R.id.AddEditText);
        button=(ImageButton)findViewById(R.id.AddBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addEdit.getText().toString().isEmpty()){
                    insertItem(addEdit.getText().toString());
                }else{
                    addEdit.setError("Please enter the content!");
                }
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gdt.onTouchEvent(motionEvent);
                return false;
            }
        });
    }

    private void getItems(){
        itemsList.clear();
        dbOpenHelper=new DbHelper(getApplicationContext(),"ToDo_WongChunWai",null,1);
        Cursor cursor=dbOpenHelper.getAllItems();

        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            int id=cursor.getInt(0);
            String content=cursor.getString(1);
            itemsList.add(new Items(id,content));
            cursor.moveToNext();
        }
        itemsAdapter = new ItemsAdapter(MainActivity.this, itemsList);
        listView.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();
        dbOpenHelper.close();
    }

    private void insertItem(String cont){
        if(!cont.isEmpty()&&cont!=null){
            dbOpenHelper=new DbHelper(getApplicationContext(),"ToDo_WongChunWai",null,1);
            if(dbOpenHelper.insertItem(cont)){
                Toast.makeText(MainActivity.this,"Item Added",Toast.LENGTH_SHORT).show();
                addEdit.setText("");
            }else{
                Toast.makeText(MainActivity.this,"Item Added Error",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MainActivity.this,"Empty Input",Toast.LENGTH_SHORT).show();
        }
        getItems();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                return false; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                int position=listView.pointToPosition((int)e1.getX(), (int)e1.getY());
                Log.e("id", String.valueOf(position));
                itemid=itemsList.get(position).getId();
                dialogDel();
                return false; // Left to right
            }

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            return false;
        }
    }

    private void dialogDel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem(itemid);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Sure to delete "+itemid+"?");
        dialog.setMessage("Sure to delete "+itemid+"?");
        dialog.setIcon(R.drawable.ic_garbage_1);
        dialog.show();
    }

    private void deleteItem(int id){
        dbOpenHelper=new DbHelper(getApplicationContext(),"ToDo_WongChunWai",null,1);
        boolean res=dbOpenHelper.deleteItem(id);
        if(res){
            Toast.makeText(MainActivity.this,"Deleted Item",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this,"Deleted Item Error",Toast.LENGTH_SHORT).show();
        }
        getItems();
    }
}
