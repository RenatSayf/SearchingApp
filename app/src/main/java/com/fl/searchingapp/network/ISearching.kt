package com.fl.searchingapp.network

import android.content.Context
import android.net.Uri
import io.reactivex.Observable

interface ISearching
{
    fun getLinksFromFile(context: Context, path: String, linkTicket: Int) : Observable<Uri>
    //fun getLinksFromNet(param: String, linkTicket: Int) : Observable<Uri>
    fun getLinksByJsoup() : Observable<Uri>
}