package m.nischal.melody.Helper;

/*The MIT License (MIT)
 *
 *    Copyright (c) 2015 Nischal M
 *
 *    Permission is hereby granted, free of charge, to any person obtaining a copy
 *    of this software and associated documentation files (the "Software"), to deal
 *    in the Software without restriction, including without limitation the rights
 *    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *    copies of the Software, and to permit persons to whom the Software is
 *    furnished to do so, subject to the following conditions:
 *
 *    The above copyright notice and this permission notice shall be included in
 *    all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *    THE SOFTWARE.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rx.Observer;

public class DebugHelper {

    private static final Boolean LOG_ENABLE = true;

    public static <T> Observer<T> getDebugObserver() {
        return new Observer<T>() {
            @Override
            public void onCompleted() {
                LumberJack.d("Debug observer/onComplete called");
            }

            @Override
            public void onError(Throwable e) {
                LumberJack.e("Debug observer/onError called");

            }

            @Override
            public void onNext(T t) {
                LumberJack.d("Debug observer/onNext called with " + t.toString());
            }
        };
    }

    public static void overdose(Context c, String s) {
        LumberJack.d(s);
        Toaster.show(c, s);
    }

    public static class LumberJack {

        public static final String TAG = "m.nischal.melody.TAG";

        public static void d(String s) {
            if (LOG_ENABLE) Log.d(TAG, s);
        }

        public static void e(String s) {
            if (LOG_ENABLE) Log.e(TAG, s);
        }

        public static void v(String s) {
            if (LOG_ENABLE) Log.v(TAG, s);
        }

        public static void d(String s, int i) {
            d(s + String.valueOf(i));
        }

        public static void v(String s, int i) {
            v(s + String.valueOf(i));
        }

        public static void e(String s, int i) {
            e(s + String.valueOf(i));
        }

        public static void d(String s, Boolean b) {
            d(s + String.valueOf(b));
        }

        public static void v(String s, Boolean b) {
            v(s + String.valueOf(b));
        }

        public static void e(String s, Boolean b) {
            e(s + String.valueOf(b));
        }

    }

    public static class Toaster {

        public static final int DURATION = Toast.LENGTH_SHORT;

        public static void show(Context c, String s) {
            Toast.makeText(c, s, DURATION).show();
        }

    }


}
