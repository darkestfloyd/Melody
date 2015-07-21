package m.nischal.melody;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.IOException;

import m.nischal.melody.Helper.GeneralHelpers;
import m.nischal.melody.Helper.RxBus;

public class MediaPlayerService extends Service {

    private final static MediaPlayer mPlayer = new MediaPlayer();

    private final IMelodyPlayer.Stub mBinder = new IMelodyPlayer.Stub() {

        @Override
        public void setDataSource(String path) throws RemoteException {

            if (mPlayer.isPlaying())
                mPlayer.stop();

            try {
                mPlayer.reset();
                mPlayer.setDataSource(getApplicationContext(), Uri.parse(path));
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void play() throws RemoteException {
            if (mPlayer.isPlaying())
                mPlayer.pause();
            else mPlayer.start();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}
