package m.nischal.melody;

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


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import static m.nischal.melody.Helper.GeneralHelpers.DebugHelper.LumberJack;

public class MediaPlayerPresenter {

    private static boolean bound;
    private static IMelodyPlayer mService;

    private static ArrayMap<Integer, Context> activitiesMap = new ArrayMap<>();
    private static ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = IMelodyPlayer.Stub.asInterface(iBinder);
            bound = true;
            LumberJack.i("Bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (activitiesMap.isEmpty()) {
                mService = null;
                bound = false;
            }
            LumberJack.i("UNBound");
        }
    };

    @CheckResult
    public static Token bindToService(Context context) {
        Intent intent = new Intent(context, MediaPlayerService.class);
        context.startService(intent);
        context.bindService(intent,
                mConnection, Context.BIND_AUTO_CREATE);
        Token token = new Token(context);
        activitiesMap.put(token.getToken(), context);
        LumberJack.i("connection established with token ", token.getToken());
        LumberJack.d("connected with, ", activitiesMap.size());
        return token;
    }

    public static boolean unbindFromService(Token token) {
        Context context = activitiesMap.get(token.getToken());
        if (context == null)
            return false;
        context.unbindService(mConnection);
        activitiesMap.remove(token.getToken());
        LumberJack.i("connection terminated from token ", token.getToken());
        if (activitiesMap.isEmpty()) kill();
        return true;
    }

    private static void kill() {
        try {
            mService.killService();
        } catch (RemoteException e) {
            report(e);
        }
    }

    public static boolean setup(@NonNull String path) {
        LumberJack.d("setting up");
        try {
            mService.setDataSource(path);
            return true;
        } catch (RemoteException e) {
            report(e);
            return false;
        }
    }

    public static boolean play_pause() {
        LumberJack.d("play/pause");
        try {
            mService.play();
            return true;
        } catch (RemoteException e) {
            report(e);
            return false;
        }
    }

    private static void report(Exception e) {
        e.printStackTrace();
        LumberJack.e(e);
    }

    public static class Token {
        private final int token;

        private Token(Context context) {
            this.token = context.hashCode();
        }

        public int getToken() {
            return token;
        }
    }


}
