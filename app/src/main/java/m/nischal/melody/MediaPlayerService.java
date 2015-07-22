package m.nischal.melody;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static m.nischal.melody.Helper.GeneralHelpers.DebugHelper.LumberJack;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private final static MediaPlayer mPlayer = new MediaPlayer();
    private static boolean foreground = false;
    private static boolean removeAfterComplete = false;
    private static Subscription sc;
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

            if (mPlayer.isPlaying()) {
                removeAfterComplete = true;
                return;
            }

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
        mPlayer.setOnCompletionListener(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        sc.unsubscribe();
        stopForeground(true);
    }

    private void makeForeground() {
        notification = new NotificationCompat.Builder(this)
                .setColor(getResources().getColor(R.color.primary_dark))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Text")
                .setContentTitle("Hello world!")
                .setAutoCancel(true)
                .build();

        startForeground(1, notification);

        foreground = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (removeAfterComplete)
            stopForeground(true);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(1);
    }
}
