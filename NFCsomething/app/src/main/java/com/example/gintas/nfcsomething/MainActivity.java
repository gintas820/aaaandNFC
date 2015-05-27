package com.example.gintas.nfcsomething;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    //Declaring the adapter here, instead of inside the onCreate method
    private NfcAdapter nfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter!=null && nfcAdapter.isEnabled()){
            Toast.makeText(this, "NFC is a thing", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "NFC is not a thing", Toast.LENGTH_LONG).show();
        }*/

        /* ALL THE CODE BELOW DOES IS A MORE ELABORATE VERSION OF THE COMMENTED OUT CODE ABOVE */
        PackageManager pm = this.getPackageManager();
        //See if NFC is available
        if(!pm.hasSystemFeature(PackageManager.FEATURE_NFC)){
            //The case if it's not available
            Toast.makeText(this, "No NFC on device", Toast.LENGTH_LONG).show();
        }
        //check android API level, if it's 4.1 or higher
        else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            //Means there's no android beam
            Toast.makeText(this, "Android beam isnt a thing here", Toast.LENGTH_LONG).show();
        }else{
            //The device shouldn't have a problem
            Toast.makeText(this, "Should work on this device", Toast.LENGTH_LONG).show();
        }

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

    public void sendFile(View view){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //Check if NFC is enabled here, instead of checking in the onCreate method
        if(!nfcAdapter.isEnabled()){
            //Since it's disabled, show settings UI to let user enable it
            Toast.makeText(this, "You should probably enable NFC", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        //Now check if Android Beam is enabled
        else if(!nfcAdapter.isNdefPushEnabled()){
            //Since it's disabled, show settings UI for Android Beam
            Toast.makeText(this, "You should probably, maybe, enable Android Beam", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }else{
            //If both are enabled, file transfer should now be allowed
            //NEED TO PUT A NAME HERE ONCE A FILE HAS BEEN DECIDED ON, PREFERABLE AN IMAGE FOR NOW
            String fileName = "Scapegoat.png";

            //Retrieve path of the user's public pictures directory
            File fileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            //Create a new file using the above directory and name
            File fileToTransfer = new File(fileDirectory, fileName);
            fileToTransfer.setReadable(true, false);

            nfcAdapter.setBeamPushUris(new Uri[]{Uri.fromFile(fileToTransfer)}, this);
        }
    }
}
