package m.nischal.melody;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.IOException;

import m.nischal.melody.Helper.NotificationHelper;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static m.nischal.melody.Helper.GeneralHelpers.DebugHelper.LumberJack;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener {

    public static final String PLAYER_PLAYING = "m.nischal.melody.PLAYING";
    public static final String PLAYER_PAUSED = "m.nischal.melody.PAUSED";

    private final static MediaPlayer mPlayer = new MediaPlayer();
    private static String PLAYER_STATE;
    private static boolean foreground = false;
    private static Subscription sc;

    private NotificationHelper notificationHelper;

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
                                PLAYER_STATE = PLAYER_PLAYING;
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
            changePlayerState();
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

        String action = intent.getAction();

        if (action != null) {
            actionPerformed(action);
        } else {
            mPlayer.setOnCompletionListener(this);
            notificationHelper = NotificationHelper.getInstance(this);
        }
        return START_STICKY;
    }

    private void changePlayerState() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            stopForeground(false);
            PLAYER_STATE = PLAYER_PAUSED;
        } else {
            mPlayer.start();
            startForeground(1, notificationHelper.getNotification());
            PLAYER_STATE = PLAYER_PLAYING;
        }
        notificationHelper.updateNotification(PLAYER_STATE);
    }

    private void actionPerformed(String action) {
        if (action.equals(NotificationHelper.ACTION_PLAY_PAUSE))
            changePlayerState();
        else mPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        sc.unsubscribe();
        stopForeground(true);
    }

    private void makeForeground() {
        startForeground(1, notificationHelper.buildNormal());
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
        notificationHelper.notifyAutoDelete();
    }
}
