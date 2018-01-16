package com.example.sd.uncaughtexceptionmail;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * This service first generated the log file,puts the file in Internal memory's application's related folder and
 * looks for internet connection availibility.
 *
 * If it gets internet connection then it calls SendMailTask which sends mail to the developer gmail account with the attached Log file
 * and deletes the Log file from the internal memory's application's related folder and destroys the service.
 *
 * But if it doesnot get internet connection then it registers a Normal Broadcast receiver which looks for the change in internet state.
 *
 * When the Broadcast receiver gets any broadcast it checks for available internet connection.
 * If the broadcast receiver gets the internet connection it sends the mail with the attached file and deleted the LogFile and destroys
 * the service.
 */

public class LogService extends Service {

    private static String TAG=LogService.class.getSimpleName();
    String app_Log;
    String filename;
    boolean  putReceiver=false;
    public static InternetReceiver internetReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"oncreate");
        internetReceiver=new InternetReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"start");
        app_Log=intent.getStringExtra("LogText");
        savelog(app_Log);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"service destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void savelog(String log)  {
        Log.e("in","yes");
        String state= Environment.getExternalStorageState();
        filename="Log.txt";
        if(Environment.MEDIA_MOUNTED.equals(state))
        {
            File root=Environment.getExternalStorageDirectory();
            File dir=new File(root.getAbsolutePath()+"/MyAppLog");
            if(!dir.exists())
            {
                dir.mkdir();
            }
            Toast.makeText(this,"directory"+ dir.getAbsolutePath().toString(),Toast.LENGTH_LONG).show();
            Log.e("directory",dir.getAbsolutePath().toString());
            File Log = new File(dir, filename);
            try
            {
                FileOutputStream fop=new FileOutputStream(Log);
                fop.write(log.getBytes());
                fop.close();
            }catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this,"No memory",Toast.LENGTH_LONG).show();
        }

        if(checkForInternet())
        {
            Log.e(TAG,"internet found");
            new MailTask().execute();
        }
        else
        {
            putReceiver=true;
            Log.e(TAG,"internet not found");
            putReceiverToCheckInternet();
        }
    }

    public Boolean checkForInternet() {
        Log.e(TAG,"checking");
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    private void putReceiverToCheckInternet() {
        Log.e(TAG,"receiver started");
        /*Intent intent=new Intent(this,InternetReceiver.class);
        sendBroadcast(intent);*/
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetReceiver,intentFilter);

    }


    class MailTask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            sendMailandDeleteFile();
            return null;
        }

        private void sendMailandDeleteFile() {
            Log.e(TAG,"Send mail and delete file");
            sendMail("App Crashed","Here is the logfile generated");

        }


        private void sendMail(String subject,String body)
        {

            String emailSubject = subject;
            String adminSubject = "App Registration Mail";
            String emailBody = body;
            String adminBody = "Your message";

            /*
            * Put the email id and password of sender gmail account and the email id of the receipient's email id
            * */

            new SendMailTask().execute("SENDER's EMAIL",
                    "SENDER's password", "RECEPIENT EMAIL ", emailSubject, emailBody);
            stopSelf();
        }

    }



}
