package m.nischal.melody.RecyclerViewHelpers;

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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import m.nischal.melody.Helper.GeneralHelpers;
import m.nischal.melody.ObjectModels._BaseModel;
import m.nischal.melody.R;

import static m.nischal.melody.Helper.GeneralHelpers.*;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RVViewHolder> {

    private ArrayList<_BaseModel> baseModelArrayList;

    public RecyclerViewAdapter(ArrayList<_BaseModel> baseModelArrayList) {
        this.baseModelArrayList = baseModelArrayList;
    }

    @Override
    public RVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(RVViewHolder holder, int position) {
        _BaseModel b = baseModelArrayList.get(position);
        holder.titleText.setText(b.getTitle());
        holder.subTitleText.setText(b.getSubTitle());
        PicassoHelper.putInImageView(b.getImagePath(), holder.imageView);
        holder.menuImage.setOnClickListener(view -> DebugHelper.overdose(holder.menuImage.getContext(), "click on menu"));
    }

    @Override
    public int getItemCount() {
        return baseModelArrayList.size();
    }

    public class RVViewHolder extends RecyclerView.ViewHolder {

        public TextView titleText;
        public TextView subTitleText;
        public ImageView imageView;
        public ImageView menuImage;

        public RVViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.cardTitle);
            subTitleText = (TextView) itemView.findViewById(R.id.cardSubTitle);
            imageView = (ImageView) itemView.findViewById(R.id.cardImage);
            menuImage = (ImageView) itemView.findViewById(R.id.cardMenuImage);
        }
    }

}
