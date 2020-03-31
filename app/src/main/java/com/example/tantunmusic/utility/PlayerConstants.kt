package com.example.tantunmusic.utility

import android.net.Uri

interface PlayerConstants {
    companion object {
        val sArtworkUri = Uri
                .parse("content://media/external/audio/albumart")
    }
}