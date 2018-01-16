package com.tatsunori.cpuclockinfo;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_APPEND;


class CpuClockInfoTask {
    private Context mContext;
    private Handler mHandler;
    private FileOutputStream mOutputStream;

    CpuClockInfoTask(Context context) {
        mContext = context;
        mHandler = new Handler();
    }


    void startLogging(final String path,final long delay) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> list = getClockList();
                String str = list.toString();
                try {
                    mOutputStream = new FileOutputStream(new File(path), true);
                    mOutputStream.write(str.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mHandler.postDelayed(this, delay);

            }
        }, delay);
    }

    void stopLogging() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private List<String> getClockList() {
        List<String> list =  new ArrayList<>();
        searchPdfFile("/sys/devices/system/cpu", list);
        List<String> clockList =  new ArrayList<>();
        for (String str : list) {
            clockList.add(getString(str));
        }
        return clockList;
    }

    private String getString(String path) {
        StringBuilder str = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while((line = reader.readLine()) != null) {
                str.append(line);
            }
            reader.close();
        } catch (Exception e) {
            return null;
        }
        return str.toString();
    }

    private void searchPdfFile(String path, List<String> cpuList) {
        File f = new File(path);
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && file.getName().startsWith("cpu")) {
                    Pattern pattern = Pattern.compile("cpu\\d+");
                    Matcher matcher = pattern.matcher(file.getName());
                    while (matcher.find()) {
                        cpuList.add(file.getAbsolutePath() + "/cpufreq/scaling_cur_freq");
                    }
                }
            }
        }
        Log.d("@@@", "list:" + cpuList);
    }

}
