package com.example.sd.uncaughtexceptionmail;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;

@SuppressWarnings("rawtypes")
public class SendMailTask extends AsyncTask {
    private static String TAG=SendMailTask.class.getSimpleName();

    private ProgressDialog statusDialog;
    /*private Activity sendMailActivity;

    public SendMailTask(Activity activity) {
        sendMailActivity = activity;

    }*/

    /*protected void onPreExecute() {
        statusDialog = new ProgressDialog(sendMailActivity);
        statusDialog.setMessage("Getting ready...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
    }*/

    @SuppressWarnings("unchecked")
    @Override
    protected Object doInBackground(Object... args) {
        try {
            Log.i("SendMailTask", "About to instantiate GMail...");
            //publishProgress("Processing input....");
            GMail androidEmail = new GMail(args[0].toString(),
                    args[1].toString(),  args[2].toString(), args[3].toString(),
                    args[4].toString());
            //publishProgress("Preparing mail message....");
            androidEmail.createEmailMessage();
            //publishProgress("Sending email....");
            androidEmail.addAttachment();
            androidEmail.sendEmail();
            //publishProgress("Email Sent.");
            Log.i("SendMailTask", "Mail Sent.");

            //Config.mailSuccess="1";


        } catch (Exception e) {
            //publishProgress(e.getMessage());
            Log.e("SendMailTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        deleteFile();
        super.onPostExecute(o);
    }

    private void deleteFile()
    {
        String path = Environment.getExternalStorageDirectory().toString()+"/MyAppLog";
        Log.e(TAG, "Path: " + path);
        File file = new File(path,"Log.txt");
        boolean deleted = file.delete();
        Log.e(TAG,"deleted ->"+deleted);
    }

    /*@Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());

    }

    @Override
    public void onPostExecute(Object result) {
        statusDialog.dismiss();
    }
*/

}