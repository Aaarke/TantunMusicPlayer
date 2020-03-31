package com.example.tantunmusic.adapter

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tantunmusic.R
import com.example.tantunmusic.model.Song
import com.example.tantunmusic.utility.PlayerConstants
import com.google.android.material.textview.MaterialTextView

class SongsAdapter(var context: Context, var listOfSongs: ArrayList<Song>) :
    RecyclerView.Adapter<SongsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_songs, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listOfSongs.size
    }

    fun updateAdapter(listOfSongs: ArrayList<Song>) {
        this.listOfSongs.clear()
        this.listOfSongs.addAll(listOfSongs)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvSongName.text = listOfSongs[position].title
        holder.tvArtistName.text = listOfSongs[position].author
        holder.ivArtists.post {
            Glide.with(context).asBitmap().load(
                ContentUris.withAppendedId(
                    PlayerConstants.sArtworkUri,
                    listOfSongs[position].albumID!!
                )
            ).into(holder.ivArtists)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvArtistName: MaterialTextView = itemView.findViewById(R.id.tvArtistName)
        var tvSongName: TextView = itemView.findViewById(R.id.tvSongName)
        var ivArtists: ImageView = itemView.findViewById(R.id.ivArtist)
    }

}