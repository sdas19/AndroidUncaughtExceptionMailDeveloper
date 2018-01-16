package com.example.sd.uncaughtexceptionmail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private static final int STORAGE_PERMISSION_CODE =1 ;
    private static final int PERMISSION_CODE=2;
    String[] permissionsRequired = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    public static Intent contextIntent;
    //public static Intent backgroundServiceintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("helloMyApplication: ",MyApplication.getInstance().helloFromMyApplication);
        //backgroundServiceintent=new Intent(MyApplication.getInstance().getApplicationContext(),LogService.class);
                                                                                                            //to use uncaughtexceptionhandler created with activity
        //Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(MainActivity.this));
        if(isAllowed()){
            //If permission is already having then showing the toast
            Toast.makeText(MainActivity.this,"You already have the permission", Toast.LENGTH_LONG).show();
            letsGetException();
            //Existing the method with return
            return;
        }

        //If the app has not the permission then asking for the permission
        //requestStoragePermission();
        requestPermission();



    }

    /*@Override
    protected void onResume() {
        super.onStart();
        startService(backgroundServiceintent);
    }*/



    private boolean isWriteStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                                                                //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
                                                                //If permission is not granted returning false
        return false;
    }

    private boolean isAllowed() {
        int writeResult = ContextCompat.checkSelfPermission(this, permissionsRequired[0]);
        int readResult = ContextCompat.checkSelfPermission(this, permissionsRequired[1]);
        //If permission is granted returning true
        if (writeResult == PackageManager.PERMISSION_GRANTED && readResult == PackageManager.PERMISSION_GRANTED)
            return true;
        //If permission is not granted returning false
        return false;
    }



    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,permissionsRequired,PERMISSION_CODE);
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request

        /*if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can write the storage",Toast.LENGTH_LONG).show();
                //letsGetException();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }*/
        if(requestCode == PERMISSION_CODE){

            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            //If permission is granted
            /*if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
                //letsGetException();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }*/

            if(allgranted){
               Toast.makeText(MainActivity.this,"All permissions acquired",Toast.LENGTH_LONG).show();
                letsGetException();
            }/* else if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permissionsRequired[1])){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this,permissionsRequired,PERMISSION_CODE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }*/
        }
    }

    private void letsGetException() {
                                                                                                            //exception handled locally to avoid crashing
        try
        {
            int y=5/0;
        }catch (ArithmeticException e)
        {
            Log.e("exceptionlocallyhandled",e.getMessage().toString());
        }
                                                                                                            //exception will be handled by Uncaught Exception Handler
        int k=7/0;
    }
}
