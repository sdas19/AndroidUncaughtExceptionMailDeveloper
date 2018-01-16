package com.example.sd.uncaughtexceptionmail;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


//Class to be used when uncaught exception handler will be initialized through activity
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

private Context context;

public MyUncaughtExceptionHandler(Context ctx) {
context=ctx;
}

@Override
public void uncaughtException(Thread t, Throwable e) {
Log.e("Exception,Thread",e.getMessage().toString()+","+t.getName().toString());
Intent homeIntent = new Intent(Intent.ACTION_MAIN);
homeIntent.addCategory( Intent.CATEGORY_HOME );
homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
context.startActivity(homeIntent);
}
}
