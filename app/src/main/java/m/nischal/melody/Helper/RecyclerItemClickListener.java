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
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    @Nullable
    private View childView;
    private int childPosition;
    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    public RecyclerItemClickListener(Context c, ClickListener cl) {
        gestureDetector = new GestureDetector(c, new GestureListener());
        this.clickListener = cl;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        childView = rv.findChildViewUnder(e.getX(), e.getY());
        childPosition = rv.getChildAdapterPosition(childView);

        return childView != null && gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        //Nothing to do here!
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        //Nothing to do here!
    }

    public interface ClickListener {
        void onClick(View v, int position);

        void onLongPress(View v, int position);
    }

    protected class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            clickListener.onClick(childView, childPosition);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            clickListener.onLongPress(childView, childPosition);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}
