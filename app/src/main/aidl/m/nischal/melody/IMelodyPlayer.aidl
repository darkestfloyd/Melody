// IMelodyPlayer.aidl
package m.nischal.melody;

interface IMelodyPlayer {

   oneway void setDataSource(in List<String> details);
   oneway void play();
   oneway void killService();
   boolean isPlaying();

}
