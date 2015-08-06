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


import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import m.nischal.melody.ObjectModels.Song;
import m.nischal.melody.R;
import m.nischal.melody.Util.ObservableContainer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GlideHelper {
    private static final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    private static final Object lock = new Object();
    public static RequestManager glideWrapper;

    public static void initGlide(Context context) {
        glideWrapper = Glide.with(context);
    }

    public static void putInImageView(String path, ImageView imageView) {
        glideWrapper
                .load(path)
                .error(R.drawable.ic_album_black_48dp)
                .into(imageView);
    }

    public static void getBitmap(Song song, ImageView imageView, int pos) {
        Observable.just(song)
                .subscribeOn(Schedulers.io())
                .flatMap(s -> {
                    synchronized (lock) {
                        retriever.setDataSource(s.getSong_path());
                        return Observable.just(retriever.getEmbeddedPicture());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(image -> GlideHelper.glideWrapper
                        .load(image)
                        .asBitmap()
                        .listener(new RequestListener<byte[], Bitmap>() {
                            @Override
                            public boolean onException(Exception e, byte[] model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, byte[] model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                ObservableContainer.getSongArrayListObservable()
                                        .take(pos + 1)
                                        .last()
                                        .subscribe(song -> {
                                            song.image = resource;
                                            song.colorPalette = Palette.from(resource).generate();
                                        });
                                return false;
                            }
                        })
                        .error(R.drawable.ic_album_black_48dp)
                        .into(imageView), GeneralHelpers.DebugHelper.LumberJack::e);
    }
}
