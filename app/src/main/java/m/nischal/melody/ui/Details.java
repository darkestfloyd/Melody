package m.nischal.melody.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import m.nischal.melody.ObjectModels._BaseModel;
import m.nischal.melody.R;
import m.nischal.melody.Util.ObservableContainer;
import m.nischal.melody.Util.RxBus;
import rx.Observable;

import static m.nischal.melody.Helper.GeneralHelpers.GlideHelper;

public class Details extends AppCompatActivity {

    private ImageView statusBar, image;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private RxBus rxBus;
    private _BaseModel model;
    private ArrayList<_BaseModel> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_new);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        rxBus = RxBus.getBus();
        int clickPosition = rxBus.getValue(RxBus.TAG_RECYCLER_VIEW_ITEM_CLICK, 0);
        int pagerPosition = rxBus.getValue(RxBus.TAG_PAGER_POSITION, 0);

        models = new ArrayList<>();
        Observable<? extends _BaseModel> objectModel = Observable.empty();
        switch (pagerPosition) {
            case 0:
                //for songs. do noting
                break;
            case 1:
                objectModel = ObservableContainer.getAlbumArrayListObservable();
                break;
            case 2:
                objectModel = ObservableContainer.getArtistArrayListObservable();
                break;
            case 3:
                objectModel = ObservableContainer.getPlaylistArrayListObservable();
                break;
            case 4:
                objectModel = ObservableContainer.getGenreArrayListObservable();
                break;
        }

        objectModel.take(clickPosition + 1)
                .last()
                .subscribe(baseModel -> {
                    this.model = baseModel;
                });

        ObservableContainer.getSongArrayListObservable()
                .filter(song -> song.getSong_album().equals(model.getTitle()))
                .subscribe(models::add);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(model.getTitle());
        toolbar.setSubtitle(model.getSubTitle());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        image = (ImageView) findViewById(R.id.image);
//        PicassoHelper.putInImageView(model.getImagePath(), image);
        GlideHelper.putInImageView(model.getImagePath(), image);
        statusBar = (ImageView) findViewById(R.id.statusBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.textView.setText(models.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public RecyclerViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }
}
