package com.aqua.anroid.policynoticeapp.Calendar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.aqua.anroid.policynoticeapp.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "calendar";
    public static final String channelNm = "eventcalendar";
    private NotificationManager notiManager;

    public NotificationHelper(Context base) {
        super(base);

        //안드로이드 버전이 오레오 이상이면 채널 생성
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel channel = new NotificationChannel(channelID, channelNm, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.design_default_color_on_primary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if(notiManager == null){
            notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notiManager;
    }
    public NotificationCompat.Builder getChannelNotification(){
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("알람")
                .setContentText("알람매니저 실행 중")
                .setSmallIcon(R.drawable.logo_image);
    }
}
