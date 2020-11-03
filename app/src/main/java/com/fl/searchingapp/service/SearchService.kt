package com.fl.searchingapp.service

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.widget.Toast
import com.fl.searchingapp.MainActivity
import com.fl.searchingapp.network.SearchingImpl
import com.fl.searchingapp.utils.RxBehaviorBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SearchService : Service()
{
    private lateinit var searchingImpl: SearchingImpl
    private lateinit var mainActivity: MainActivity

    override fun onBind(p0: Intent?): IBinder?
    {
        return null
    }

    override fun onCreate()
    {
        super.onCreate()
        searchingImpl = SearchingImpl()
        //runBrowserByFileLink(path, linkTicket)
        runBrowser()
        val sbj = RxBehaviorBus.toObservable()
        mainActivity = sbj.value as MainActivity
        return
    }

    private fun runBrowserByFileLink(path: String, linkTicket: Int)
    {
        var disposable: Disposable? = null
        disposable = searchingImpl.getLinksFromFile(this, path, linkTicket)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ uri ->
                createBrowserIntent(uri)
            }, { err ->
                err.printStackTrace()
                Toast.makeText(this, err.message, Toast.LENGTH_LONG).show()
            }, {
                disposable?.dispose()
                mainActivity.finish()
                stopSelf()
            })
    }

    private fun runBrowser()
    {
        var disposable: Disposable? = null
        disposable = searchingImpl.getLinksByJsoup()
            .delay(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({uri ->
                createBrowserIntent(uri)
            },{ err ->
                err.printStackTrace()
                err.message?.let {
                    if (it.contains("Unable to resolve host", true))
                    {
                        Toast.makeText(this, "Internet is not available", Toast.LENGTH_LONG).show()
                    }
                }
                disposable?.dispose()
                stopSelf()
            },{
                disposable?.dispose()
                stopSelf()
            })
    }

    private fun createBrowserIntent(uri: Uri)
    {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        //val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra(Browser.EXTRA_CREATE_NEW_TAB, this.packageName)
        println("************* $uri ********************")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try
        {
            this.applicationContext.startActivity(intent)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.finish()
        println("*********** ${this::class.java.simpleName} has been closed ***************************")
    }




}