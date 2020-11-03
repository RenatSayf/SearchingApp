package com.fl.searchingapp.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.fl.searchingapp.MainActivity
import com.fl.searchingapp.R
import com.fl.searchingapp.service.SearchService
import com.fl.searchingapp.utils.RxBehaviorBus


class MainFragment : Fragment() {

    companion object
    {
        const val TEXT = "авто"
        const val LINK_TICKET = 2
        const val LINK = "https://yandex.ru/search/touch/?text=$TEXT&lr=54"
    }

    private lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(LINK)
        )
        activity?.startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).let{
            val service = Intent(activity, SearchService::class.java)
            it.startService(service)
            RxBehaviorBus.send(it)
        }
    }


}
