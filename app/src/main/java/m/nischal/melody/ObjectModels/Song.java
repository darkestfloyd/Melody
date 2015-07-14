package m.nischal.melody.ObjectModels;

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

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import m.nischal.melody.Adapters.RecyclerViewAdapter;
import m.nischal.melody.Helper.DebugHelper;

/**
 * <code>Song</code>
 * <p>
 * Model class to represent an Song entry from the MediaStore.
 * All objects of <code>Song</code> class are immutable.
 */
public final class Song extends _BaseModel {

    public static final int SONG_ID = 0;
    public static final int SONG_TITLE_COLUMN = 1;
    public static final int SONG_DISPLAY_NAME_COLUMN = 2;
    public static final int SONG_DATE_ADDED_COLUMN = 3;
    public static final int SONG_DATE_MODIFIED_COLUMN = 4;
    public static final int SONG_SIZ_COLUMNE = 5;
    public static final int SONG_ALBUM_COLUMN = 6;
    public static final int SONG_ALBUM_ID_COLUMN = 7;
    public static final int SONG_ARTIST_COLUMN = 8;
    public static final int SONG_ARTIST_ID_COLUMN = 9;
    public static final int SONG_BOOKMARK_COLUMN = 10;
    public static final int SONG_DURATION_COLUMN = 11;
    public static final int SONG_TRACK_NUMBER_COLUMN = 11;
    public static final int SONG_YEAR_COLUMN = 12;
    public static final Uri song_uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public static final String[] projections = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.BOOKMARK,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.YEAR
    };
    public static final String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    final private String song_id, song_title, song_display_name, song_date_added, song_date_modified, song_size,
            song_album, song_album_id, song_artist, song_artist_id, song_bookmark, song_duration, song_track, song_year;

    /**
     * Constructor for Song object.
     *
     * @param song_id            The id for the song.
     * @param song_title         The title of the song.
     * @param song_display_name  The display name of the file.
     * @param song_date_added    The time the file was added to the media provider Units are seconds since 1970.
     * @param song_date_modified The time the file was last modified Units are seconds since 1970.
     * @param song_size          The size of the file in bytes.
     * @param song_album         The album the audio file is from, if any.
     * @param song_album_id      The id of the album the audio file is from, if any.
     * @param song_artist        The artist who created the audio file, if any.
     * @param song_artist_id     The id of the artist who created the audio file, if any.
     * @param song_bookmark      The position, in ms, playback was at when playback for this file was last stopped.
     * @param song_duration      The duration of the audio file, in ms.
     * @param song_track         The track number of this song on the album, if any.
     * @param song_year          The year the audio file was recorded, if any
     */
    private Song(String song_id, String song_title, String song_display_name, String song_date_added, String song_date_modified,
                 String song_size, String song_album, String song_album_id, String song_artist, String song_artist_id,
                 String song_bookmark, String song_duration, String song_track, String song_year) {
        this.song_id = song_id;
        this.song_title = song_title;
        this.song_display_name = song_display_name;
        this.song_date_added = song_date_added;
        this.song_date_modified = song_date_modified;
        this.song_size = song_size;
        this.song_album = song_album;
        this.song_album_id = song_album_id;
        this.song_artist = song_artist;
        this.song_artist_id = song_artist_id;
        this.song_bookmark = song_bookmark;
        this.song_duration = song_duration;
        this.song_track = song_track;
        this.song_year = song_year;
    }

    /**
     * Method to get list of all songs from cursor.
     *
     * @param c Cursor to pull values from.
     * @return ArrayList of the song.
     */
    public static ArrayList<Song> createSongsFromCursor(Cursor c) {
        ArrayList<Song> songs = new ArrayList<>();
        DebugHelper.LumberJack.v("creation of songs in progress.. ");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                String _id = c.getString(SONG_ID);
                String title = c.getString(SONG_TITLE_COLUMN);
                String display_name = c.getString(SONG_DISPLAY_NAME_COLUMN);
                String date_added = c.getString(SONG_DATE_ADDED_COLUMN);
                String date_modified = c.getString(SONG_DATE_MODIFIED_COLUMN);
                String size = c.getString(SONG_SIZ_COLUMNE);
                String album = c.getString(SONG_ALBUM_COLUMN);
                String album_id = c.getString(SONG_ALBUM_ID_COLUMN);
                String artist = c.getString(SONG_ARTIST_COLUMN);
                String artist_id = c.getString(SONG_ARTIST_ID_COLUMN);
                String bookmark = c.getString(SONG_BOOKMARK_COLUMN);
                String duration = c.getString(SONG_DURATION_COLUMN);
                String track = c.getString(SONG_TRACK_NUMBER_COLUMN);
                String year = c.getString(SONG_YEAR_COLUMN);
                songs.add(new Song(_id, title, display_name, date_added, date_modified, size, album, album_id,
                        artist, artist_id, bookmark, duration, track, year));
            } while (c.moveToNext());
        }
        DebugHelper.LumberJack.v("cursor size for songs: " + c.getCount());
        c.close();
        return songs;
    }

    public String getSong_id() {
        return song_id;
    }

    public String getSong_title() {
        return song_title;
    }

    public String getSong_display_name() {
        return song_display_name;
    }

    public String getSong_date_added() {
        return song_date_added;
    }

    public String getSong_date_modified() {
        return song_date_modified;
    }

    public String getSong_size() {
        return song_size;
    }

    public String getSong_album() {
        return song_album;
    }

    public String getSong_album_id() {
        return song_album_id;
    }

    public String getSong_artist() {
        return song_artist;
    }

    public String getSong_artist_id() {
        return song_artist_id;
    }

    public String getSong_bookmark() {
        return song_bookmark;
    }

    public String getSong_duration() {
        return song_duration;
    }

    public String getSong_track() {
        return song_track;
    }

    public String getSong_year() {
        return song_year;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "{[ " + song_title + "], [" + song_id + "]}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void injectIntoHolder(RecyclerViewAdapter.RVViewHolder holder) {
        holder.titleText.setText(song_title);
    }
}
