package com.example.tantunmusic.ui.main

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.tantunmusic.R
import com.example.tantunmusic.adapter.SongsAdapter
import com.example.tantunmusic.model.Song
import com.example.tantunmusic.utility.RequestCode
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.details_slide_bar.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.ArrayList


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var varsInitialized: Boolean = false
    var relativeParams: RelativeLayout.LayoutParams? = null
    private lateinit var mLayoutManager:RecyclerView.LayoutManager
    private lateinit var songsAdapter: SongsAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        checkPermissions()


    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context!!, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), RequestCode.REQUEST_CODE_ASK_PERMISSIONS)
                return
            }
        }

    }

    private fun setAdapter() {
        mLayoutManager= LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvSongs.layoutManager=mLayoutManager
        songsAdapter = SongsAdapter(context!!, ArrayList())
        rvSongs.adapter = songsAdapter
    }

    private fun setObserver() {
        viewModel.listOfSongs?.observe(viewLifecycleOwner, Observer {
            updateAdapter(it)
        })
    }

    private fun updateAdapter(it: ArrayList<Song>) {
        songsAdapter.updateAdapter(it)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RequestCode.REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMusicFromLocal()
            } else {
                Toast.makeText(context!!,"Denial",Toast.LENGTH_LONG)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun loadMusicFromLocal() {
        setAdapter()
        setObserver()
        viewModel.getListOfLocalSongs()
        setSlidingPanelLayoutListeners()
    }

    private fun setSlidingPanelLayoutListeners() {
        sliding_layout.addPanelSlideListener(object : SlidingPaneLayout.PanelSlideListener,
            SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {
                if (lDetailsBar != null) {
                    if (!varsInitialized) {
                        relativeParams = lDetailsBar.layoutParams as RelativeLayout.LayoutParams
                        varsInitialized = true
                    }
                    val adjustedMargin: Int =
                        sliding_layout.height - sliding_layout.panelHeight - panel.top
                    relativeParams?.setMargins(0, adjustedMargin, 0, 0)
                    lDetailsBar.layoutParams = relativeParams
                }
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
            }

            override fun onPanelClosed(panel: View) {

            }

            override fun onPanelOpened(panel: View) {

            }

        })
    }

}
