package m.nischal.melody.Helper;

/*
 * Copyright 2015 Nischal M
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import m.nischal.melody.MediaPlayerService;
import m.nischal.melody.R;

public class NotificationHelper {

    public static final String ACTION_PLAY_PAUSE = "m.nischal.melody.MediaPlayerService.PLAY_PAUSE";
    public static final String ACTION_NEXT = "m.nischal.melody.MediaPlayerService.NEXT";
    public static final String ACTION_PREV = "m.nischal.melody.MediaPlayerService.PREV";
    private static final Object lock = new Object();
    private final Context context;
    private final NotificationCompat.Builder notificationBuilder;
    private RemoteViews remoteViews;
    private Notification notification;

    private NotificationHelper(Context context) {
        this.context = context;
        this.notificationBuilder =
                new android.support.v7.app.NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setColor(context.getResources().getColor(R.color.primary))
                        .setContentText("test")
                        .setContentTitle("title");
    }

    public static NotificationHelper getInstance(Context context) {
        return new NotificationHelper(context);
    }

    private void setUpRemoteViews() {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_expanded);
        remoteViews.setInt(R.id.notificationView, "setBackgroundColor", context.getResources().getColor(R.color.primary));

        Intent i1 = new Intent(context, MediaPlayerService.class);
        i1.setAction(ACTION_PLAY_PAUSE);
        PendingIntent p1 = PendingIntent.getService(context, 0x2, i1, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent i2 = new Intent(context, MediaPlayerService.class);
        i2.setAction(ACTION_NEXT);
        PendingIntent p2 = PendingIntent.getService(context, 0x2, i2, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent i3 = new Intent(context, MediaPlayerService.class);
        i3.setAction(ACTION_PREV);
        PendingIntent p3 = PendingIntent.getService(context, 0x2, i3, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.action_next, p2);
        remoteViews.setOnClickPendingIntent(R.id.action_prev, p3);
        remoteViews.setOnClickPendingIntent(R.id.action_play_pause, p1);
    }

    public Notification buildNormal() {

        setUpRemoteViews();

        notification = notificationBuilder
                .setAutoCancel(false).build();

        notification.bigContentView = remoteViews;

        return notification;
    }

    public void updateNotification(String state) {
        switch (state) {
            case MediaPlayerService.PLAYER_PLAYING:
                remoteViews.setImageViewResource(R.id.action_play_pause, R.drawable.ic_pause_white_36dp);
                notification.bigContentView = remoteViews;
                notifyChange();
                break;
            case MediaPlayerService.PLAYER_PAUSED:
                remoteViews.setImageViewResource(R.id.action_play_pause, R.drawable.ic_play_arrow_white_36dp);
                notifyAutoDelete();
                break;
        }
    }

    public Notification getNotification() {
        return notification;
    }

    private void notifyChange() {
        synchronized (lock) {
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                    .notify(0x1, notification);
        }
    }

    public void notifyAutoDelete() {
        notification = notificationBuilder
                .setAutoCancel(true)
                .build();
        notification.bigContentView = remoteViews;
        notifyChange();
    }

}
