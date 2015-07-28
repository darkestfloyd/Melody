// IMelodyPlayer.aidl
package m.nischal.melody;

interface IMelodyPlayer {

   oneway void setDataSource(in List<String> details, in Bitmap bitmap, in int color);
   oneway void play();
   oneway void killService();
   boolean isPlaying();

}
