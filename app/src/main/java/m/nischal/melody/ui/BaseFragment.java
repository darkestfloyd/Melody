package m.nischal.melody.ui;

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

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import m.nischal.melody.Helper.DebugHelper;
import m.nischal.melody.Helper.LoaderHelper;
import m.nischal.melody.Helper.QueryObject;
import m.nischal.melody.ObjectModels.Album;
import m.nischal.melody.ObjectModels.Artist;
import m.nischal.melody.ObjectModels.Genre;
import m.nischal.melody.ObjectModels.Playlist;
import m.nischal.melody.ObjectModels.Song;
import m.nischal.melody.ObjectModels._BaseModel;
import m.nischal.melody.R;
import m.nischal.melody.RecyclerViewAdapter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class BaseFragment extends Fragment {

    private int fragmentType;
    private ArrayList<_BaseModel> baseModelArrayList = new ArrayList<>();
    private RecyclerView rv;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentType = getArguments().getInt(_BaseModel.VIEW_PAGER_POSITION_STRING);
        DebugHelper.LumberJack.v("BaseFragment creation with value " + fragmentType);
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = (RecyclerView) view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        populateList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscriptions.hasSubscriptions())
            subscriptions.unsubscribe();
    }

    private void populateList() {
        DebugHelper.LumberJack.v("populating list for fragment type: ", fragmentType);
        QueryObject queryObject;
        switch (fragmentType) {
            case _BaseModel.ALBUMS:
                queryObject = new QueryObject(Album.album_uri, Album.album_projections, null, null, MediaStore.Audio.Albums.ALBUM);

                subscriptions.add(LoaderHelper
                        .getObservable(getActivity().getApplicationContext(), queryObject)
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(cursor -> Observable.from(Album.createAlbumsFromCursor(cursor)))
                        .subscribe(new Observer<Album>() {
                            @Override
                            public void onCompleted() {
                                DebugHelper.LumberJack.d("onComplete called for albums.. populating adapter!");
                                rv.setAdapter(new RecyclerViewAdapter(baseModelArrayList));
                            }

                            @Override
                            public void onError(Throwable e) {
                                DebugHelper.LumberJack.d("onError called for albums " + e.getMessage());
                            }

                            @Override
                            public void onNext(Album album) {
                                baseModelArrayList.add(album);
                            }
                        }));
                break;

            case _BaseModel.SONGS:
                queryObject = new QueryObject(Song.song_uri, Song.projections, Song.selection, null, MediaStore.Audio.Media.TITLE);

                subscriptions.add(LoaderHelper
                        .getObservable(getActivity().getApplicationContext(), queryObject)
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(cursor -> Observable.from(Song.createSongsFromCursor(cursor)))
                        .subscribe(new Observer<Song>() {
                            @Override
                            public void onCompleted() {
                                DebugHelper.LumberJack.d("onComplete called for songs.. populating adapter!");
                                rv.setAdapter(new RecyclerViewAdapter(baseModelArrayList));
                            }

                            @Override
                            public void onError(Throwable e) {
                                DebugHelper.LumberJack.d("onError called for songs " + e.getMessage());
                            }

                            @Override
                            public void onNext(Song song) {
                                baseModelArrayList.add(song);
                            }
                        }));
                break;

            case _BaseModel.ARTISTS:
                queryObject = new QueryObject(Artist.artist_uri, Artist.projections, null, null, MediaStore.Audio.Artists.ARTIST);

                subscriptions.add(LoaderHelper
                        .getObservable(getActivity().getApplicationContext(), queryObject)
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(cursor -> Observable.from(Artist.createArtistsFromCursor(cursor)))
                        .subscribe(new Observer<Artist>() {
                            @Override
                            public void onCompleted() {
                                DebugHelper.LumberJack.d("onComplete called for artists.. populating adapter!");
                                rv.setAdapter(new RecyclerViewAdapter(baseModelArrayList));
                            }

                            @Override
                            public void onError(Throwable e) {
                                DebugHelper.LumberJack.d("onError called for artists " + e.getMessage());
                            }

                            @Override
                            public void onNext(Artist artist) {
                                baseModelArrayList.add(artist);
                            }
                        }));
                break;

            case _BaseModel.PLAYLISTS:
                queryObject = new QueryObject(Playlist.playlist_uri, Playlist.projections, null, null, MediaStore.Audio.Playlists.NAME);

                subscriptions.add(LoaderHelper
                        .getObservable(getActivity().getApplicationContext(), queryObject)
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(cursor -> Observable.from(Playlist.createPlaylistsFromCursor(cursor)))
                        .subscribe(new Observer<Playlist>() {
                            @Override
                            public void onCompleted() {
                                DebugHelper.LumberJack.d("onComplete called for playlists.. populating adapter!");
                                rv.setAdapter(new RecyclerViewAdapter(baseModelArrayList));
                            }

                            @Override
                            public void onError(Throwable e) {
                                DebugHelper.LumberJack.d("onError called for playlists " + e.getMessage());
                            }

                            @Override
                            public void onNext(Playlist playlist) {
                                baseModelArrayList.add(playlist);
                            }
                        }));
                break;

            case _BaseModel.GENERS:
                queryObject = new QueryObject(Genre.genres_uri, Genre.projections, null, null, MediaStore.Audio.Genres.NAME);

                subscriptions.add(LoaderHelper
                        .getObservable(getActivity().getApplicationContext(), queryObject)
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(cursor -> Observable.from(Genre.createGenersFromCursor(cursor)))
                        .subscribe(new Observer<Genre>() {
                            @Override
                            public void onCompleted() {
                                DebugHelper.LumberJack.d("onComplete called for genres.. populating adapter!");
                                rv.setAdapter(new RecyclerViewAdapter(baseModelArrayList));
                            }

                            @Override
                            public void onError(Throwable e) {
                                DebugHelper.LumberJack.d("onError called for genres " + e.getMessage());
                            }

                            @Override
                            public void onNext(Genre genre) {
                                baseModelArrayList.add(genre);
                            }
                        }));
                break;
        }
    }

}
