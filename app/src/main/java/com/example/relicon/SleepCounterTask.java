package com.example.relicon;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SleepCounterTask extends AsyncTask {
    private final Context context;
    private final GoogleSignInAccount account;
    private final Handler handler;
    private DataSet dataSet;

    public SleepCounterTask(Context context, GoogleSignInAccount account, Handler handler) {
        this.context = context;
        this.account = account;
        this.handler = handler;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Task<DataReadResponse> task = Fitness
                .getHistoryClient(context, account)
                .readData(new DataReadRequest.Builder().read(DataType.TYPE_SLEEP_SEGMENT).setTimeRange(11, 12, TimeUnit.DAYS).build())
                .addOnCompleteListener(task1 -> dataSet = Objects.requireNonNull(task1.getResult()).getDataSet(DataType.TYPE_SLEEP_SEGMENT));
                            //непоняточки
        try {
            Tasks.await(task);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        Message m = new Message();
        Bundle b = new Bundle();
        b.putString("sleep", dataSet.toString());
        m.setData(b);

        handler.handleMessage(m);
    }
}
