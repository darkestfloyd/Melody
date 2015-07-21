// IMelodyPlayer.aidl
package m.nischal.melody;

// Declare any non-default types here with import statements

interface IMelodyPlayer {

   void setDataSource(String path);
   void play();
   void pause();

}
