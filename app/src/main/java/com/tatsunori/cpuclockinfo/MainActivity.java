package com.tatsunori.cpuclockinfo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.start_end_button)
    Button mStartEndButton;

    CpuClockInfoTask mCpuInfoTask;
    boolean mStartFlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mStartFlg = false;

        mCpuInfoTask= new CpuClockInfoTask(this);
    }

    @OnClick(R.id.start_end_button)
    public void onButtonClick() {
        if (mStartFlg) {
            mStartEndButton.setText(R.string.start);
            mCpuInfoTask.stopLogging();
            mStartFlg = false;
        } else {
            String path = getLogFilePath();
            mStartEndButton.setText(R.string.end);
            mCpuInfoTask.startLogging(path, 1000);
            mStartFlg = true;
        }
    }

    private String getLogFilePath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Log/";
        File file = new File(path);
        file.mkdirs();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddmmss");
        path += sdf.format(date) + ".txt";
        return path;
    }
}
