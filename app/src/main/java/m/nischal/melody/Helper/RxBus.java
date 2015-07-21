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

import android.support.annotation.CheckResult;
import android.support.v4.util.ArrayMap;
import android.view.View;

import m.nischal.melody.ObjectModels._BaseModel;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

    public static final String TAG_RECYCLER_VIEW_ITEM_CLICK = "m.nischal.melody.recycler_view_item_click";
    public static final String TAG_RECYCLER_VIEW_ITEM_LONG_PRESS = "m.nischal.melody.recycler_view_item_long_press";
    public static final String TAG_RECYCLER_VIEW_MENU_CLICK = "m.nischal.melody.recycler_view_menu_click";

    public static final String TAG_PAGER_POSITION = "m.nischal.melody.pagerPosition";

    private static RxBus rxBus;
    private final ArrayMap<String, Integer> values = new ArrayMap<>();
    private final Subject<BusClass, BusClass> bus;

    private RxBus() {
        this.bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getBus() {
        if (rxBus == null)
            rxBus = new RxBus();
        return rxBus;
    }

    public void putValue(String s, Integer i) {
        values.put(s, i);
    }
    
    @CheckResult
    public Integer getValue(String s) {
        return values.get(s);
    }

    public Observable<BusClass> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return this.bus.hasObservers();
    }

    public <T extends BusClass> void publish(T t) {
        this.bus.onNext(t);
    }

    public static class BusClass {
        public static class TapEvent extends BusClass {
        }

        public static class RecyclerViewItemClick extends BusClass {
        }

        public static class ViewPagerPageChanged extends BusClass{

        }

    }
}
