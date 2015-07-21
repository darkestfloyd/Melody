package m.nischal.melody.Helper;

/*
 * Copyright 2015 Nischal M
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


/**
 * <code>BusClass</code>
 * <p>
 * Provides simple objects to pass on the bus.
 */
public class BusEvents {

    /**
     * Used for debugging
     */
    public static class TapEvent extends BusEvents {
    }

    /**
     * Passed when a click is detected on any item of the recycler view.
     */
    public static class RecyclerViewItemClick extends BusEvents {
    }

    /**
     * Passed when the viewpager is scrolled to a new tab.
     */
    public static class ViewPagerPageChanged extends BusEvents {
    }

    /**
     * Debug media control object.
     */
    public static class MediaControl extends BusEvents {
    }

}
