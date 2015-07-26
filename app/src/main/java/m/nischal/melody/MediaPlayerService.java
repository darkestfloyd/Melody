package m.nischal.melody;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.IOException;
import java.util.List;

import m.nischal.melody.Util.BusEvents;
import m.nischal.melody.Helper.NotificationHelper;
import m.nischal.melody.Util.RxBus;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static m.nischal.melody.Helper.GeneralHelpers.DebugHelper.LumberJack;

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener {

    public static final String RX_BUS_PLAYER_STATE = "m.nischal.melody.PLAYER_STATE_CHANGED";
    public static final String PLAYER_FINISHED = "m.nischal.melody.SONG_COMPLETED";
    public static final int STATE_PLAYING = 0;
    public static final int STATE_PAUSED = 1;
    public static final int STATE_COMPLETED = 2;

    private final static MediaPlayer mPlayer = new MediaPlayer();
    private static boolean foreground = false;

    private RxBus rxBus;
    private NotificationHelper notificationHelper;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    private final IMelodyPlayer.Stub mBinder = new IMelodyPlayer.Stub() {

        @Override
        public void setDataSource(List<String> details) throws RemoteException {

            //if (!foreground)
                makeForeground(details);
            //TODO else part to update notification


            /*LumberJack.d("path: " + details.get(0));
            LumberJack.d("title: " + details.get(1));
            LumberJack.d("album: " + details.get(2));
            LumberJack.d("artist: " + details.get(3));*/
            setSourceForPlayer(details);
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

    private void setSourceForPlayer(List<String> details) {

        mPlayer.reset();

        Subscription sc = Observable.just(details.get(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getPathObserver());
        subscriptions.add(sc);

        if (mPlayer.isPlaying())
            playerStateChanged(STATE_PLAYING);
    }

    private Observer<String> getPathObserver() {
        return new Observer<String>() {
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
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if (action != null) {
            actionPerformed(action);
        } else {
            mPlayer.setOnCompletionListener(this);
            rxBus = RxBus.getBus();
            notificationHelper = NotificationHelper.getInstance(this);
            subscribeToBus();
        }
        return START_STICKY;
    }

    private void subscribeToBus() {
        Subscription sc = rxBus
                .toObservable()
                .subscribe(new Observer<BusEvents>() {
                    @Override
                    public void onCompleted() {
                        LumberJack.d("onComplete called/Service#subscribe");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LumberJack.e("onError called/Service#subscribe");
                        LumberJack.e(e);
                    }

                    @Override
                    public void onNext(BusEvents busEvents) {
                        LumberJack.d("onNext called/Service#subscribe");
                        LumberJack.d("boolean: ", busEvents instanceof BusEvents.NewSongAddedToQueue && !mPlayer.isPlaying());
//                        if (busEvents instanceof BusEvents.NewSongAddedToQueue && !mPlayer.isPlaying())
//                            setSourceForPlayer(nextSongPath());
                    }
                });
        subscriptions.add(sc);
    }

    private void changePlayerState() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            stopForeground(false);
            rxBus.putValue(RX_BUS_PLAYER_STATE, STATE_PAUSED);
        } else {
            mPlayer.start();
            startForeground(1, notificationHelper.getNotification());
            rxBus.putValue(RX_BUS_PLAYER_STATE, STATE_PLAYING);
        }
        playerStateChanged(-1);
    }

    private void playerStateChanged(int newState) {
        if (newState != -1)
            rxBus.putValue(RX_BUS_PLAYER_STATE, newState);
        rxBus.publish(new BusEvents.MediaStateChanged());
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
        subscriptions.unsubscribe();
        stopForeground(true);
    }

    private void makeForeground(List<String> details) {
        startForeground(1, notificationHelper.buildNormal(details));
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
        playerStateChanged(STATE_COMPLETED);
        sendBroadcast(new Intent(PLAYER_FINISHED));
    }
}
