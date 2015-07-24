package m.nischal.melody.Util;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import m.nischal.melody.Helper.BusEvents;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * <code>RxBus</code>
 * <p>
 * An implementation of an event bus using Rx.
 * A single bus is used throughout the Application.
 */

public class RxBus {

    /**
     * Keys for putting in ArrayMap
     */
    public static final String TAG_RECYCLER_VIEW_ITEM_CLICK = "m.nischal.melody.recycler_view_item_click";
    public static final String TAG_RECYCLER_VIEW_ITEM_LONG_PRESS = "m.nischal.melody.recycler_view_item_long_press";
    public static final String TAG_RECYCLER_VIEW_MENU_CLICK = "m.nischal.melody.recycler_view_menu_click";
    public static final String TAG_PAGER_POSITION = "m.nischal.melody.pagerPosition";

    private static RxBus rxBus;
    private final ArrayMap<String, Integer> values = new ArrayMap<>();
    private final Subject<BusEvents, BusEvents> bus;

    /**
     * Constructor to initialise the RxBus.
     */
    private RxBus() {
        this.bus = new SerializedSubject<>(PublishSubject.create());
    }

    /**
     * A static method that returns a RxBus object if it exits, it not creates a new Bus.
     *
     * @return The RxBus for the Application.
     */
    public static RxBus getBus() {
        if (rxBus == null)
            rxBus = new RxBus();
        return rxBus;
    }

    /**
     * Method for putting values into an ArrayMap. Generally used to pass elements.
     *
     * @param kay   String key.
     * @param value Integer value.
     */
    public void putValue(@NonNull String kay, @NonNull Integer value) {
        values.put(kay, value);
    }

    /**
     * Method to retrieve elements from the ArrayMap.
     *
     * @param key        String key.
     * @param defaultInt Default integer, in case key does not exist.
     * @return Integer value.
     */
    @CheckResult
    public Integer getValue(@NonNull String key, @Nullable Integer defaultInt) {
        Integer integer = values.get(key);
        if (integer == null)
            return defaultInt;
        return integer;
    }

    /**
     * Method to get the subject, bus.
     *
     * @return SubjectB bus.
     */
    @CheckResult
    public Observable<BusEvents> toObservable() {
        return bus;
    }

    /**
     * Method to check if any subscribers have subscribed to the bus to listen to events.
     */
    @CheckResult
    public boolean hasObservers() {
        return this.bus.hasObservers();
    }

    /**
     * Method to push an event on the bus.
     *
     * @param t   Type of event.
     * @param <T> Extension of BusClass.
     */
    public <T extends BusEvents> void publish(@NonNull T t) {
        this.bus.onNext(t);
    }
}
