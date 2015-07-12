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
import m.nischal.melody.RecyclerViewAdapter;

/**
 * <code>Genres</code>
 * <p>
 * Model class to represent an Genre entry from the MediaStore.
 * All objects of <code>Genres</code> are immutable.
 */
public final class Genre extends _BaseModel {

    public static final int GENRES_ID_COLUMN = 0;
    public static final int GENRES_NAME_COLUMN = 1;
    public static final Uri genres_uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
    public static final String[] projections = {
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME
    };
    private final String genre_id, genre_name;

    /**
     * Constructor for Genres class.
     *
     * @param genre_id   Id of the particular Genre.
     * @param genre_name Name of Genre.
     */

    private Genre(String genre_id, String genre_name) {
        this.genre_id = genre_id;
        this.genre_name = genre_name;
    }

    /**
     * Method to get list of all genres from cursor.
     *
     * @param c Cursor to pull values from.
     * @return ArrayList of the genre.
     */
    public static ArrayList<Genre> createGenersFromCursor(Cursor c) {
        ArrayList<Genre> genres = new ArrayList<>();
        DebugHelper.LumberJack.v("creation of genres in progress.. ");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                String id = c.getString(GENRES_ID_COLUMN);
                String name = c.getString(GENRES_NAME_COLUMN);
                genres.add(new Genre(id, name));
            } while (c.moveToNext());
        }
        DebugHelper.LumberJack.v("cursor size for genres: " + c.getCount());
        c.close();
        return genres;
    }

    public String getGenre_id() {
        return genre_id;
    }

    public String getGenre_name() {
        return genre_name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "{[" + genre_id + "], [" + genre_name + "]}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void injectIntoHolder(RecyclerViewAdapter.RVViewHolder holder) {
        holder.textView.setText(genre_name);
    }
}
