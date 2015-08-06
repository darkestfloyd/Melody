package m.nischal.melody.Util;

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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import m.nischal.melody.ObjectModels.Song;
import m.nischal.melody.R;
import rx.Observable;

import static m.nischal.melody.Helper.GeneralHelpers.DebugHelper.LumberJack;

public class PlayingQueue {

    private static PlayingQueue playingQueue = new PlayingQueue();
    private final LinkedList<Song> queue = new LinkedList<>();
    private int playPosition = -1;

    private PlayingQueue() {
    }

    public static PlayingQueue getInstance(Context context) {
        return playingQueue;
    }

    public void addToQueue(@NonNull Song song) {
        LumberJack.d("adding to queue: " + song.getSong_title());
        queue.add(song);
    }

    @CheckResult
    public List<String> parseNextSong() {
        return parseSong(this.nextSong());
    }

    @CheckResult
    public List<String> parsePrevSong() {
        return parseSong(this.prevSong());
    }

    @CheckResult
    private List<String> parseSong(Song song) {
        List<String> details = new ArrayList<>();
        details.add(song.getSong_path());
        details.add(song.getSong_title());
        details.add(song.getSong_artist());
        details.add(song.getSong_album());
        details.add(song.getSong_id());
        return details;
    }

    @CheckResult
    public Bitmap getBitmap(Context context) {
        Bitmap b = queue.get(playPosition).image;
        if (b == null)
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_album_black_48dp);
        return b;
    }

    public int getVibrantPaletteColor() {
        Palette p = queue.get(playPosition).colorPalette;
        if (p != null)
            return p.getVibrantColor(Color.GRAY);
        return Color.GRAY;
    }

    public void setPlayPosition(@NonNull int position) {
        playPosition = position;
    }

    public void addToQueueAt(@NonNull int position, @NonNull Song song) {
        queue.add(position, song);
    }

    public void removeFromQueue(@NonNull int position) {
        queue.remove(position);
    }

    @CheckResult
    public Observable<Song> getQueue() {
        return Observable.from(new ArrayList<>(queue));
    }

    @CheckResult
    public boolean hasNext() {
        return playPosition < queue.size() - 1;
    }

    @CheckResult
    public boolean hasPrev() {
        return playPosition > 0;
    }

    @CheckResult
    public Song nextSong() {
        return queue.get(++playPosition);
    }

    @CheckResult
    public String nextSongPath() {
        LumberJack.d("next song path: " + nextSong().getSong_path());
        return nextSong().getSong_path();
    }

    @CheckResult
    public String prevSongPath() {
        return prevSong().getSong_path();
    }

    @CheckResult
    private Song prevSong() {
        return queue.get(--playPosition);
    }

    @CheckResult
    private Song currentSong() {
        return queue.get(playPosition);
    }

    @CheckResult
    public int getSize() {
        return queue.size();
    }

    @CheckResult
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
