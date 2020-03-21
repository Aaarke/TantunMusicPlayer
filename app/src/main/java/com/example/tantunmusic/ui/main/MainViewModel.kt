package com.example.tantunmusic.ui.main

import android.app.Application
import android.content.ContentProviderClient
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tantunmusic.model.Song


class MainViewModel( application: Application) : AndroidViewModel(application) {

     var listOfSongs:MutableLiveData<ArrayList<Song>>?=MutableLiveData()
    private val context = getApplication<Application>().applicationContext


    fun getListOfLocalSongs(){
        val allSongUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection: String = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        var listSongs:ArrayList<Song>?= ArrayList()
        val cursor: Cursor? = context.applicationContext.contentResolver.query(allSongUri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val songUrl: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val albumId:Long=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                    listSongs?.add(Song(songName,songAuthor,songUrl,albumId))

                } while (cursor.moveToNext())

            }
        }
        cursor?.close()
        listOfSongs?.value=listSongs
    }
}
