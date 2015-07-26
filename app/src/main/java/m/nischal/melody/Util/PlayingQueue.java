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


import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import m.nischal.melody.ObjectModels.Song;
import rx.Observable;

import static m.nischal.melody.Helper.GeneralHelpers.DebugHelper.LumberJack;

public class PlayingQueue {

    private static PlayingQueue playingQueue = new PlayingQueue();
    private final LinkedList<Song> queue = new LinkedList<>();
    private int playPosition = -1;

    private PlayingQueue() {
    }

    public static PlayingQueue getInstance() {
        return playingQueue;
    }

    public void addToQueue(@NonNull Song song) {
        LumberJack.d("adding to queue: " + song.getSong_title());
        queue.add(song);
    }

    public List<String> parseNextSong() {
        Song song = this.nextSong();
        return parseSong(song);
    }

    public List<String> parsePrevSong() {
        Song song = this.prevSong();
        return parseSong(song);
    }

    private List<String> parseSong(Song song) {
        List<String> details = new ArrayList<>();
        details.add(song.getSong_path());
        details.add(song.getSong_title());
        details.add(song.getSong_album());
        details.add(song.getSong_artist());
        return details;
    }

    public void setPlayPosition(@NonNull int position) {
        playPosition = position;
    }

    @CheckResult
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

    public void printAll() {
        LumberJack.d("queue size: ", queue.size());
        LumberJack.d("playPosition: ", playPosition);
    }
}
