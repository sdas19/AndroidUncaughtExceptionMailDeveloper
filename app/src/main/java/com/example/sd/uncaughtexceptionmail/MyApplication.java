package com.example.sd.uncaughtexceptionmail;

import android.app.Application;
import android.content.Intent;
import android.util.Log;


public class MyApplication extends Application {

    public String helloFromMyApplication = "Hello From GlobalApplication";
    public Thread.UncaughtExceptionHandler defaultUEH;
    public static Intent logServiceIntent;
    public static String TAG=MyApplication.class.getSimpleName();


    private static MyApplication singleton;

    public static MyApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton=this;
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        logServiceIntent=new Intent(this,LogService.class);

                                                                                                                    // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);

    }


    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {

                    Log.e(TAG,ex.getMessage().toString()+","+thread.getName().toString());

                                                                                                                    //launch default activity
                    /*Intent intent=new Intent(MyApplication.this,Main2Activity.class);
                    intent.putExtra("error",ex.getMessage().toString());
                    startActivity(intent);*/


                                                                                                                    //launch home to close app
                   /* Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory( Intent.CATEGORY_HOME );
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    System.exit(2);*/

                    String log=ex.getMessage()+" in "+ex.getCause().getStackTrace()[0].getClassName()+" at line no "+String.valueOf(ex.getCause().getStackTrace()[0].getLineNumber());
                    Log.e(TAG,log);
                    logServiceIntent.putExtra("LogText",log);
                    startService(logServiceIntent);
                    Log.e(TAG,"service initiated");
                   /* Intent intent=new Intent("start.log.service");
                    intent.putExtra("LOG",log);
                    startService(intent);*/


                    //savelog(ex.getMessage()+" in "+ex.getCause().getStackTrace()[0].getClassName()+" at line no "+String.valueOf(ex.getCause().getStackTrace()[0].getLineNumber()));

                                                                                                                    // re-throw critical exception further to the os (important)
                    defaultUEH.uncaughtException(thread, ex);

                }
            };

    /*public void savelog(String log)  {
        Log.e("in","yes");
        String state= Environment.getExternalStorageState();
        String filename="Log "+new Date()+".txt";
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

    }*/

}
