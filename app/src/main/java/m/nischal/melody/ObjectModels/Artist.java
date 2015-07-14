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
 * <code>Artist</code>
 * <p>
 * Model class to represent an Artist entry from the MediaStore.
 * All objects of <code>Artist</code> are immutable.
 */

public final class Artist extends _BaseModel {

    public static final int ARTIST_ID = 0;
    public static final int ARTIST = 1;
    public static final int ARTIST_NUM_ALBUMS = 2;
    public static final int ARTIST_NUM_TRACKS = 3;
    public static final Uri artist_uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    public static final String[] projections = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
    };
    private final String artist_id, artist, artist_num_album, artist_num_tracks;

    /**
     * Constructor to create a new Artist object.
     *
     * @param artist_id         Id of the artist.
     * @param artist            The artist who created the audio file, if any
     * @param artist_num_album  The number of albums in the database for this artist
     * @param artist_num_tracks The number of albums in the database for this artist
     */
    private Artist(String artist_id, String artist, String artist_num_album, String artist_num_tracks) {
        this.artist_id = artist_id;
        this.artist = artist;
        this.artist_num_album = artist_num_album;
        this.artist_num_tracks = artist_num_tracks;
    }

    /**
     * Method to get list of all artists from cursor.
     *
     * @param c Cursor to pull values from.
     * @return ArrayList of the artist.
     */
    public static ArrayList<Artist> createArtistsFromCursor(Cursor c) {
        ArrayList<Artist> artists = new ArrayList<>();
        DebugHelper.LumberJack.v("creation of artists in progress.. ");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                String artist_id = c.getString(ARTIST_ID);
                String artist = c.getString(ARTIST);
                String artist_num_albums = c.getString(ARTIST_NUM_ALBUMS);
                String artist_num_tracks = c.getString(ARTIST_NUM_TRACKS);
                artists.add(new Artist(artist_id, artist, artist_num_albums, artist_num_tracks));
            } while (c.moveToNext());
        }
        DebugHelper.LumberJack.v("cursor size for artists: " + c.getCount());
        c.close();
        return artists;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public String getArtist() {
        return artist;
    }

    public String getArtist_num_album() {
        return artist_num_album;
    }

    public String getArtist_num_tracks() {
        return artist_num_tracks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "{[" + artist_id + "], [" + artist + "]}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return artist;
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
