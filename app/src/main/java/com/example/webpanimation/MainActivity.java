package com.example.webpanimation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private String m_Text = "/storage/emulated/0/WhatsApp/Media/WhatsApp Stickers";
    private boolean animationOnly = false;
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<MyListData> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        recyclerView = findViewById(R.id.rvNumbers);
        recyclerView.setHasFixedSize(true);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        setRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                if(!animationOnly){
                    animationOnly = true;
                    setRecyclerView();
                }
                return true;
            case R.id.item2:
                if(animationOnly){
                    animationOnly = false;
                    setRecyclerView();
                }
                return true;
            case R.id.item3:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Whatsapp Sticker File Path");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(m_Text);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        arrayList = null;
                        setRecyclerView();
                    }
                });
                builder.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setRecyclerView() {
        if(arrayList == null)  arrayList = getFilePaths();

        ArrayList<MyListData> newArray = new ArrayList<>();
        if(animationOnly){
            for(MyListData data: arrayList){
                if(data.getImgId()) newArray.add(data);
            }
        }
        else newArray = arrayList;

        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(context, newArray);
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<MyListData> getFilePaths() {
        ArrayList<MyListData> resultIAV = new ArrayList<>();

        File imageDir = new File(m_Text);
        File[] imageList = imageDir.listFiles();
            try {
                for (File imagePath : imageList) {
                    int size = (int) imagePath.length();
                    byte[] bytes = new byte[size];
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(imagePath));
                        buf.read(bytes, 0, bytes.length);
                        buf.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String str = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        str = new String(Arrays.copyOfRange(bytes, 44, 48), StandardCharsets.UTF_8);
                    }

                    if (imagePath.getName().contains(".webp")) {
                        String path = imagePath.getAbsolutePath();
                        resultIAV.add(new MyListData(path, str.contains("ANMF")));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return resultIAV;
    }
}
