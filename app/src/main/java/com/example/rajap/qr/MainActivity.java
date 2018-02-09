package com.example.rajap.qr;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView sv;
    static int current_score=0;
    public DatabaseHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb= new DatabaseHelper(this);
    }
    @Override
    public void handleResult(Result result) {
        String datastr=new String(result.getText());
        String []output=datastr.split(" ");
        boolean test = mydb.insertData(output[0],output[1]);
        if(test==true) {
            Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
            cal_data();

            //current_score+=Integer.parseInt(output[1]);
        }
            else {Toast.makeText(this,"QR CODE AlREADY SCANNED!SEARCH FOR OTHER QR CODES!!!", Toast.LENGTH_LONG).show();
        cal_data();}
        Log.i("Main","Inside HandleResult"+result.getText());
        Toast.makeText(this,"Score:"+current_score,Toast.LENGTH_LONG).show();
        sv.stopCamera();
        setContentView(R.layout.activity_main);
    }
public void cal_data(){
    current_score=0;
    Cursor res= mydb.getAllData();
    if(res.getCount()==0){
       // Toast.makeText(this,"Nothing",Toast.LENGTH_LONG).show();
        return;
    }
    StringBuffer buffer=new StringBuffer();
    while(res.moveToNext()){
        //buffer.append("ID "+res.getString(0)+"\n");
        buffer.append("Score "+res.getString(1)+"\n\n");
        current_score+=Integer.parseInt(res.getString(1));
        //Toast.makeText(MainActivity.this,buffer.toString(),Toast.LENGTH_SHORT).show();
    }
}
    @Override
    protected void onPause() {
        super.onPause();
        sv.stopCamera();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        sv.startCamera();
    }

    public void StartCamera(View view) {
        sv=new ZXingScannerView(this);
        sv.setResultHandler(this);
        setContentView(sv);
        sv.startCamera();
        //sv.resumeCameraPreview(this);
    }
}
