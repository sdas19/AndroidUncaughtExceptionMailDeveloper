package com.example.sd.uncaughtexceptionmail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


/**
 * If no Internet found by the service then this Broadcast receiver is placed which looks for change in internet availibility state.
 */
public class InternetReceiver extends BroadcastReceiver {

    private static String TAG=InternetReceiver.class.getSimpleName();
    Context serviceContext;
    TimerTask timerTask;
    Timer timer=new Timer();
    boolean mailSent=false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "received something");
        serviceContext=context;
        timerTask=new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG,"TImertask inititated");
                if (mailSent==false&& checkForInternet() == true) {
                    Log.e(TAG, "internet found in receiver");
                    new MailTask().execute();
                }
            }
        };
        timer.schedule(timerTask,10000,1000);
    }

    public Boolean checkForInternet() {
        Log.e(TAG,"checking");
        ConnectivityManager cm = (ConnectivityManager) serviceContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        Log.e(TAG,String.valueOf(netInfo != null && netInfo.isConnected()));
        return (netInfo != null && netInfo.isConnected());
    }

    private void sendMail(String subject,String body)
    {
            /*String fromEmail = "dassoumyajit4@gmail.com";
            String fromPassword = "8981110236";
            String toEmails = "dassoumyajit4@gmail.com";
            String adminEmail = "admin@gmail.com";*/
        String emailSubject = subject;
        String adminSubject = "App Registration Mail";
        String emailBody = body;
        String adminBody = "Your message";
        new SendMailTask().execute("dassoumyajit4@gmail.com",
                "8981110236", "dassoumyajit4@gmail.com", emailSubject, emailBody);
        Log.e(TAG,"timertask canceled");
        timerTask.cancel();
        timer.cancel();
        mailSent=true;
        serviceContext.unregisterReceiver(LogService.internetReceiver);
        serviceContext.stopService(MyApplication.logServiceIntent);
    }






    class MailTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            sendMailandDeleteFile();
            return null;
        }

        private void sendMailandDeleteFile() {
            Log.e(TAG, "Send mail and delete file");
            sendMail("App Crashed", "Here is the logfile generated");


        }
    }
}

