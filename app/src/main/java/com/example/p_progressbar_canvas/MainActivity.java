package com.example.p_progressbar_canvas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.tickprogress.SpringProgressView;
import com.example.tickprogress.SpringProgressView2;
import com.example.tickprogress.TickProgress;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TickProgress tickProgress;
    private TextView textView;
    private SpringProgressView progressView;
    private SpringProgressView2 progressView1;
    private Random random = new Random(System.currentTimeMillis());
    private int i = 0;
    private int currentCount = 0;// 进度当前值
    private Thread runnable1;
    private Boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tickProgress = findViewById(R.id.tickProgress);
        tickProgress.setProgress(50f);
//        tickProgress.setProgress(50);

//        textView = (TextView) findViewById(R.id.textview);
        progressView = (SpringProgressView) findViewById(R.id.spring_progress_view);
        progressView.setMaxCount(1000.0f);
        progressView1 = (SpringProgressView2) findViewById(R.id.spring_progress_view2);
        progressView1.setMaxCount(1000.0f);

//        findViewById(R.id.click).setOnClickListener(this);
//        findViewById(R.id.click_stop).setOnClickListener(this);
        progressView1.setCurrentCount(130);


        Handler handle = new Handler() {

            public void handleMessage(android.os.Message msg) {
                int i = (Integer) msg.obj;
                switch (msg.what) {

                    case 1:

                        progressView.setCurrentCount(i);
                        progressView1.setCurrentCount(i);
                        textView.setText("最大值：" + progressView.getMaxCount()
                                + "   当前值：" + progressView.getCurrentCount());
                        textView.setText("最大值：" + progressView1.getMaxCount()
                                + "   当前值：" + progressView1.getCurrentCount());
                        break;

                    default:
                        break;
                }
            }

            ;
        };


    }

    @Override
    public void onClick(View v) {

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.click:
//                i = currentCount;
//                flag = true;
//                runnable1 = new Thread(runnable);
//                runnable1.start();
//                break;
//            case R.id.click_stop:
//                flag = false;
//                // if (runnable1 != null && runnable1.isAlive()) {
//                // // Log.e("readCacheThread", "thread interrupt_1");
//                // runnable1.interrupt();
//                // // Log.e("status", ""+readCacheThread.isInterrupted());
//                // }
//                break;
//            default:
//                break;
//        }
//
//    }
}