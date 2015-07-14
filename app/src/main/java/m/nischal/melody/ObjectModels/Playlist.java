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

import m.nischal.melody.Helper.DebugHelper;

/**
 * <code>Playlist</code>
 * <p>
 * Model class to represent an Playlist entry from the MediaStore.
 * All objects of <code>Playlist</code> are immutable.
 */
public final class Playlist extends _BaseModel {

    public static final int PLAYLIST_ID_COLUMN = 0;
    public static final int PLAYLIST_NAME_COLUMN = 1;
    public static final int PLAYLIST_DATA_COLUMN = 2;
    public static final int PLAYLIST_DATE_ADDED_COLUMN = 3;
    public static final int PLAYLIST_DATE_MODIFIED_COLUMN = 4;
    public static final Uri playlist_uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
    public static final String[] projections = {
            MediaStore.Audio.Playlists._ID,
            MediaStore.Audio.Playlists.NAME,
            MediaStore.Audio.Playlists.DATA,
            MediaStore.Audio.Playlists.DATE_ADDED,
            MediaStore.Audio.Playlists.DATE_MODIFIED
    };
    private final String playlist_id, playist_name, playlist_data, playlist_date_addded, playlist_date_modified;

    private Playlist(String playlist_id, String playist_name, String playlist_data, String playlist_date_addded, String playlist_date_modified) {
        this.playlist_id = playlist_id;
        this.playist_name = playist_name;
        this.playlist_data = playlist_data;
        this.playlist_date_addded = playlist_date_addded;
        this.playlist_date_modified = playlist_date_modified;
    }

    /**
     * Method to get list of all playlists from cursor.
     *
     * @param c Cursor to pull values from.
     * @return ArrayList of the playlist.
     */
    public static ArrayList<Playlist> createPlaylistsFromCursor(Cursor c) {
        ArrayList<Playlist> playlists = new ArrayList<>();
        DebugHelper.LumberJack.v("creation of playlists in progress.. ");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                String id = c.getString(PLAYLIST_ID_COLUMN);
                String name = c.getString(PLAYLIST_NAME_COLUMN);
                String data = c.getString(PLAYLIST_DATA_COLUMN);
                String date_added = c.getString(PLAYLIST_DATE_ADDED_COLUMN);
                String date_modeified = c.getString(PLAYLIST_DATE_MODIFIED_COLUMN);
                playlists.add(new Playlist(id, name, data, date_added, date_modeified));
            } while (c.moveToNext());
        }
        DebugHelper.LumberJack.v("cursor size for playlists: " + c.getCount());
        c.close();
        return playlists;
    }

    public String getPlaylist_id() {
        return playlist_id;
    }

    public String getPlayist_name() {
        return playist_name;
    }

    public String getPlaylist_data() {
        return playlist_data;
    }

    public String getPlaylist_date_addded() {
        return playlist_date_addded;
    }

    public String getPlaylist_date_modified() {
        return playlist_date_modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "{[" + playlist_id + "], [" + playist_name + "]}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return playist_name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubTitle() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImagePath() {
        return null;
    }
}
