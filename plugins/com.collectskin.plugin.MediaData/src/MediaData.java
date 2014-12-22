package com.collectskin.plugin;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import android.app.Activity;
import android.provider.MediaStore;
import android.database.Cursor;
import android.content.ContentUris;
/**
 * This class echoes a string called from JavaScript.
 */
public class MediaData extends CordovaPlugin
{

    BroadcastReceiver receiver;
    Long recAlbumId;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        // We need to listen to connectivity events to update navigator.connection
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        intentFilter.addAction("com.android.music.playbackcomplete");
        intentFilter.addAction("com.android.music.queuechanged");
        if (this.receiver == null) {
            this.receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // (The null check is for the ARM Emulator, please use Intel Emulator for better results)
                    //this.recAlbumId = intent.getStringExtra("albumId");;
                            String action = intent.getAction();
                            String cmd = intent.getStringExtra("command");

                            String com = intent.getStringExtra("ComponentInfo");

                            System.out.println("aa ------------------>>>>mIntentReceiver.onReceive "+ action + " / " + cmd+ " / "+ com);
                            Bundle bundle = intent.getExtras();
                            String artist = intent.getStringExtra("artist");
                            String album = intent.getStringExtra("album");
                            String track = intent.getStringExtra("track");
                            Long albumid = bundle.getLong("albumId",0);

                            setAlbumId(albumid);
                        for (String key : bundle.keySet()) {
                            Object value = bundle.get(key);
                            System.out.println("WAAAAAAAAAA ->" + key );
                        }

                }
            };
            webView.getContext().registerReceiver(this.receiver, intentFilter);
        }

    }

    private void setAlbumId(Long theId){
      this.recAlbumId = theId;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("get_art_path")) {
          //  Cursor cursor = cordova.getActivity().managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
          //                  new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
          //                  MediaStore.Audio.Albums._ID+ "=?",
          //                  new String[] {this.recAlbumId},
          //                  null);
            Cursor cursor =  cordova.getActivity().getContentResolver().query(
              ContentUris.withAppendedId(
                  MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, this.recAlbumId),
              new String[] { MediaStore.Audio.AlbumColumns.ALBUM_ART },
              null,
              null,
              null);


            if (cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                callbackContext.success(path);
            }
            callbackContext.success("meh" + this.recAlbumId);
            cursor.close();
      }else if(action.equals("full_details")) {

      }  else {
      }
      return true;
    }


}
