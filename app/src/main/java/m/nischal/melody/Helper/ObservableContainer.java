package m.nischal.melody.Helper;

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

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import m.nischal.melody.ObjectModels.Album;
import m.nischal.melody.ObjectModels.Artist;
import m.nischal.melody.ObjectModels.Genre;
import m.nischal.melody.ObjectModels.Playlist;
import m.nischal.melody.ObjectModels.Song;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Helper class for all model related observables.
 */
public final class ObservableContainer {

    private static Observable<Album> albumObservable;
    private static Observable<Artist> artistObservable;
    private static Observable<Song> songObservable;
    private static Observable<Playlist> playlistObservable;
    private static Observable<Genre> genreObservable;

    public static Observable<Artist> getArtistObservable() {
        return artistObservable;
    }

    public static Observable<Song> getSongObservable() {
        return songObservable;
    }

    public static Observable<Album> getAlbumObservable() {
        return albumObservable;
    }

    public static Observable<Playlist> getPlaylistObservable() {
        return playlistObservable;
    }

    public static Observable<Genre> getGenreObservable() {
        return genreObservable;
    }

    private static Observable<Cursor> getCursorObservable(Context context, QueryObject queryObject) {
        return Observable.defer(() -> Observable.just(context.getContentResolver())
                .subscribeOn(Schedulers.io())
                .flatMap(queryObject::query));
    }

    public static void initAlbumObserver(Context context) {
        QueryObject queryObject = new QueryObject(Album.album_uri, Album.album_projections, null, null, MediaStore.Audio.Albums.ALBUM);
        albumObservable = getCursorObservable(context, queryObject)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(cursor -> Observable.from(Album.createAlbumsFromCursor(cursor)));
    }

    public static void initArtistObservable(Context context) {
        QueryObject queryObject = new QueryObject(Artist.artist_uri, Artist.projections, null, null, MediaStore.Audio.Artists.ARTIST);
        artistObservable = getCursorObservable(context, queryObject)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(cursor -> Observable.from(Artist.createArtistsFromCursor(cursor)));
    }

    public static void initSongObserver(Context context) {
        QueryObject queryObject = new QueryObject(Song.song_uri, Song.projections, Song.selection, null, MediaStore.Audio.Media.TITLE);
        songObservable = getCursorObservable(context, queryObject)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(cursor -> Observable.from(Song.createSongsFromCursor(cursor)));
    }

    public static void initPlaylistObservable(Context context) {
        QueryObject queryObject = new QueryObject(Playlist.playlist_uri, Playlist.projections, null, null, MediaStore.Audio.Playlists.NAME);
        playlistObservable = getCursorObservable(context, queryObject)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(cursor -> Observable.from(Playlist.createPlaylistsFromCursor(cursor)));
    }

    public static void initGenreObservable(Context context) {
        QueryObject queryObject = new QueryObject(Genre.genres_uri, Genre.projections, null, null, MediaStore.Audio.Genres.NAME);
        genreObservable = getCursorObservable(context, queryObject)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(cursor -> Observable.from(Genre.createGenersFromCursor(cursor)));
    }

    public static void initAll(Context context) {
        initAlbumObserver(context);
        initArtistObservable(context);
        initSongObserver(context);
        initPlaylistObservable(context);
        initGenreObservable(context);
    }


}
