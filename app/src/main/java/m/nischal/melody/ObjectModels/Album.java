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

import static m.nischal.melody.Helper.GeneralHelpers.DebugHelper;

/**
 * <code>Album</code>
 * <p>
 * Model class to represent an Album entry from the MediaStore.
 * All objects of <code>Album</code> are immutable.
 */
public final class Album extends _BaseModel {

    public static final int ALBUM_ID = 0;
    public static final int ALBUM_COLUMN = 1;
    public static final int ALBUM_ART_COLUMN = 2;
    public static final int ARTIST_COLUMN = 3;
    public static final int FIRST_YEAR_COLUMN = 4;
    public static final int LAST_YEAR_COLUMN = 5;
    public static final int NUMBER_OF_SONGS_COLUMN = 6;
    public static final Uri album_uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    public static final String[] album_projections = {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.FIRST_YEAR,
            MediaStore.Audio.Albums.LAST_YEAR,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
    };
    final private String album_id, album_name, album_art, album_artist, album_number_of_songs, album_first_year, album_last_year;

    /**
     * Constructor for Album object.
     *
     * @param album_id              The id for the album_name.
     * @param album_name            The album_name on which the audio file appears, if any.
     * @param album_art             Cached album_name art.
     * @param album_artist          The artist whose songs appear on this album_name.
     * @param album_first_year      The year in which the earliest songs on this album_name were released.
     * @param album_last_year       The year in which the latest songs on this album_name were released.
     * @param album_number_of_songs The number of songs on this album_name.
     */
    private Album(String album_id, String album_name, String album_art, String album_artist
            , String album_first_year, String album_last_year, String album_number_of_songs) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.album_art = album_art;
        this.album_artist = album_artist;
        this.album_number_of_songs = album_number_of_songs;
        this.album_first_year = album_first_year;
        this.album_last_year = album_last_year;
    }

    /**
     * Method to get list of all albums from cursor.
     *
     * @param c Cursor to pull values from.
     * @return ArrayList of the album_name.
     */
    public static ArrayList<Album> createAlbumsFromCursor(Cursor c) {
        ArrayList<Album> albums = new ArrayList<>();
        DebugHelper.LumberJack.v("creation of albums in progress.. ");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                String album = c.getString(ALBUM_COLUMN);
                String album_art = c.getString(ALBUM_ART_COLUMN);
                String album_artist = c.getString(ARTIST_COLUMN);
                String album_fy = c.getString(FIRST_YEAR_COLUMN);
                String album_ly = c.getString(LAST_YEAR_COLUMN);
                String album_num = c.getString(NUMBER_OF_SONGS_COLUMN);
                String album_id = c.getString(ALBUM_ID);
                albums.add(new Album(album_id, album, album_art, album_artist, album_fy, album_ly, album_num));
            } while (c.moveToNext());
        }
        DebugHelper.LumberJack.v("cursor size for albums: " + c.getCount());
        c.close();
        return albums;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "{[" + album_name + "], [" + album_id + "], [" + album_artist + "]}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return album_name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubTitle() {
        return album_artist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImagePath() {
        return album_art;
    }

    public String getAlbum_first_year() {
        return album_first_year;
    }

    public String getAlbum_last_year() {
        return album_last_year;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public String getAlbum_artist() {
        return album_artist;
    }

    public String getAlbum_number_of_songs() {
        return album_number_of_songs;
    }

}
