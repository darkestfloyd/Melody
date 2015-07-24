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


import java.util.ArrayList;
import java.util.LinkedList;

import m.nischal.melody.ObjectModels.Song;
import rx.Observable;

public class PlayingQueue {

    private static PlayingQueue playingQueue;
    private final LinkedList<Song> queue = new LinkedList<>();
    private int playPosition = -1;

    public static PlayingQueue getQueueInstance() {
        if (playingQueue == null)
            playingQueue = new PlayingQueue();
        return playingQueue;
    }

    public void addToQueue(Song song) {
        queue.push(song);
    }

    public void addToQueueAt(int position, Song song) {
        queue.add(position, song);
    }

    public void removeFromQueue(int position) {
        queue.remove(position);
    }

    public Observable<Song> getQueue() {
        return Observable.from(new ArrayList<Song>(queue));
    }

    public boolean hasNext() {
        return playPosition < queue.size() - 1;
    }

    public boolean hasPrev() {
        return playPosition > 0;
    }

    public Song playNext() {
        return queue.get(++playPosition);
    }

    private Song playPrev() {
        return queue.get(--playPosition);
    }

    public int getSize() {
        return queue.size();
    }

    @Override
    public String toString() {
        return "{[" + queue.hashCode() + "], [" + queue.size() + "], [" + queue.getFirst().toString() + "]}";
    }
}
