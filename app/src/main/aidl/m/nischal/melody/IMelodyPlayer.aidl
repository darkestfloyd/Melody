// IMelodyPlayer.aidl
package m.nischal.melody;

interface IMelodyPlayer {

   oneway void setDataSource(String path);
   oneway void play();
   oneway void killService();
   boolean isPlaying();

}
