package m.nischal.melody;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.io.IOException;

import m.nischal.melody.ui.MainActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static m.nischal.melody.Helper.GeneralHelpers.DebugHelper.LumberJack;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener {

    public static final String INTENT_EXTRA = "m.nischal.melody.service_intent_extra";
    public static final int INTENT_EXTRA_PLAY = 0;
    public static final int INTENT_EXTRA_PAUSE = 1;
    public static final int INTENT_EXTRA_NEXT = 2;
    public static final int INTENT_EXTRA_PREV = 3;

    private final static MediaPlayer mPlayer = new MediaPlayer();
    private static boolean foreground = false;
    private static boolean removeAfterComplete = false;
    private static Subscription sc;

    private NotificationCompat.Builder notificationBuilder;

    private Notification notification;

    private final IMelodyPlayer.Stub mBinder = new IMelodyPlayer.Stub() {

        @Override
        public void setDataSource(String path) throws RemoteException {

            if (!foreground)
                makeForeground();
            //TODO else part to update notification

            if (mPlayer.isPlaying())
                mPlayer.stop();

            mPlayer.reset();
            sc = Observable.just(path)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {
                            LumberJack.d("onComplete called/in service");
                            try {
                                mPlayer.prepare();
                            } catch (IOException e) {
                                LumberJack.e(e);
                            } finally {
                                mPlayer.start();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LumberJack.e(e);
                        }

                        @Override
                        public void onNext(String s) {
                            LumberJack.d("onNext called/in service with path: " + s);
                            try {
                                mPlayer.setDataSource(getApplicationContext(), Uri.parse(s));
                            } catch (Exception e) {
                                LumberJack.e(e);
                            }
                        }
                    });
        }

        @Override
        public void play() throws RemoteException {
            if (mPlayer.isPlaying())
                mPlayer.pause();
            else mPlayer.start();
        }

        @Override
        public void killService() throws RemoteException {

            if (mPlayer.isPlaying()) return;

            mPlayer.reset();
            stopSelf();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mPlayer.isPlaying();
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int intentExtraValue = intent.getIntExtra(INTENT_EXTRA, -1);
        if (intentExtraValue != -1) {
            LumberJack.d("extra value: ", intentExtraValue);
        } else {
            mPlayer.setOnCompletionListener(this);
            setUpNotification();
        }
        return START_STICKY;
    }

    private void setUpNotification() {

        notificationBuilder =
                new android.support.v7.app.NotificationCompat.Builder(this)
                        .setColor(getResources().getColor(R.color.primary))
                        .setSmallIcon(R.mipmap.ic_launcher);

        /*Intent i1 = new Intent(this, MediaPlayerService.class);
        i1.putExtra(INTENT_EXTRA, 1);
        PendingIntent p1 = PendingIntent.getService(this, 1, i1, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent i2 = new Intent(this, MediaPlayerService.class);
        i2.putExtra(INTENT_EXTRA, 2);
        PendingIntent p2 = PendingIntent.getService(this, 2, i2, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent i3 = new Intent(this, MediaPlayerService.class);
        i3.putExtra(INTENT_EXTRA, 3);
        PendingIntent p3 = PendingIntent.getService(this, 3, i3, PendingIntent.FLAG_UPDATE_CURRENT);*/

        notificationBuilder
                .setContentText("test")
                .setContentTitle("title")
                .setSmallIcon(R.mipmap.ic_launcher);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        sc.unsubscribe();
        stopForeground(true);
    }

    private void makeForeground() {
        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.notification_expanded);
        remoteView.setInt(R.id.notificationView, "setBackgroundColor", getResources().getColor(R.color.primary));
//        remoteView.setOnClickPendingIntent(R.id.notificationAction, PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), 0));

        notification = notificationBuilder
                .setAutoCancel(false)
                .build();

        notification.bigContentView = remoteView;

        startForeground(1, notification);

        foreground = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        LumberJack.d("music completed!");
        stopForeground(false);
        notification = notificationBuilder
                .setAutoCancel(true)
                .build();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(1, notification);
    }
}
