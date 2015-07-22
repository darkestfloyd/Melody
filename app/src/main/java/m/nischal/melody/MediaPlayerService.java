package m.nischal.melody;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static m.nischal.melody.Helper.GeneralHelpers.DebugHelper.LumberJack;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private final static MediaPlayer mPlayer = new MediaPlayer();
    private static boolean foreground = false;
    private static Subscription sc;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private final IMelodyPlayer.Stub mBinder = new IMelodyPlayer.Stub() {

        @Override
        public void setDataSource(String path) throws RemoteException {

            if (!foreground)
                makeForeground();

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
                        }

                        @Override
                        public void onError(Throwable e) {
                            LumberJack.e(e);
                        }

                        @Override
                        public void onNext(String s) {
                            LumberJack.d("onNext called/in service with path: " + s);
                            try {
                                mPlayer.setDataSource(s);
                                mPlayer.prepare();
                                mPlayer.start();
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
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) mPlayer.stop();
                mPlayer.reset();
                mPlayer.release();
            }
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LumberJack.i("service stopped!");
        sc.unsubscribe();
    }

    private void makeForeground() {
        //TODO
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
