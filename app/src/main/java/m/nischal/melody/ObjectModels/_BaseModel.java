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

import m.nischal.melody.Adapters.RecyclerViewAdapter;

/**
 * <h>BaseModel</h>
 * <p>
 * A general class to represent all ObjectModels. No particular use.
 */
public abstract class _BaseModel {

    public static final String VIEW_PAGER_POSITION_STRING = "m.nischal.melody.Helper.LoaderHelper.position";

    public static final int SONGS = 0;
    public static final int ALBUMS = 1;
    public static final int ARTISTS = 2;
    public static final int PLAYLISTS = 3;
    public static final int GENERS = 4;

    /**
     * Returns a String for Debugging.
     */
    public abstract String toString();

    /**
     * Method to get main Title of object.
     * @return Title
     */
    public abstract String getTitle();

    /**
     * Method to get sub Title of object.
     * @return Sub Title
     */
    public abstract String getSubTitle();

    /**
     * Method to get image path of object.
     * @return Image Path
     */
    public abstract String getImagePath();

}
