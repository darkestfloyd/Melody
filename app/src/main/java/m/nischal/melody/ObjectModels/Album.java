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

/**
 * <h>Album</h>
 * <p>
 *     Model class to represent an Album entry from the MediaStore.
 *     All objects of Album are immutable.
 */
public class Album {

    /**
     * <h>YearPair</h>
     * <p>
     *     Helper class to wrap the first and last years for an album object.
     *
     */
    private class YearPair{
        final private String album_first_year, album_last_year;

        /**
         * Constructor for YearPair
         * @param album_first_year The year in which the earliest songs on this album were released.
         * @param album_last_year The year in which the latest songs on this album were released.
         */
        private YearPair(String album_first_year, String album_last_year) {
            this.album_first_year = album_first_year;
            this.album_last_year = album_last_year;
        }

        public String getAlbum_first_year() {
            return album_first_year;
        }

        public String getAlbum_last_year() {
            return album_last_year;
        }
    }

    final private String album_id, album, album_art, album_artist, album_number_of_songs, album_number_of_songs_for_artist, album_key;
    final private YearPair album_years;

    /**
     * Constructor for Album object.
     * @param album_id The id for the album.
     * @param album_key A non human readable key calculated from the ALBUM, used for searching, sorting and grouping.
     * @param album The album on which the audio file appears, if any.
     * @param album_art Cached album art.
     * @param album_artist The artist whose songs appear on this album.
     * @param album_first_year The year in which the earliest songs on this album were released.
     * @param album_last_year The year in which the latest songs on this album were released.
     * @param album_number_of_songs The number of songs on this album.
     * @param album_number_of_songs_for_artist Indicates the number of songs on the album by the given artist.
     */
    public Album(String album_id, String album_key, String album, String album_art, String album_artist, String album_first_year, String album_last_year, String album_number_of_songs, String album_number_of_songs_for_artist) {
        this.album_id = album_id;
        this.album_key = album_key;
        this.album = album;
        this.album_art = album_art;
        this.album_artist = album_artist;
        this.album_number_of_songs = album_number_of_songs;
        this.album_number_of_songs_for_artist = album_number_of_songs_for_artist;
        this.album_years = new YearPair(album_first_year, album_last_year);
    }

    public String getAlbum_id() {
        return album_id;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public String getAlbum_artist() {
        return album_artist;
    }

    public YearPair getAlbum_first_year() {
        return album_years;
    }

    public String getAlbum_number_of_songs() {
        return album_number_of_songs;
    }

    public String getAlbum_number_of_songs_for_artist() {
        return album_number_of_songs_for_artist;
    }
}
