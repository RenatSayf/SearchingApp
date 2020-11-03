package com.fl.searchingapp.network

import android.content.Context
import android.net.Uri
import com.fl.searchingapp.ui.main.MainFragment
import com.fl.searchingapp.utils.FileUtil
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class SearchingImpl : ISearching
{
    override fun getLinksFromFile(context: Context, path: String, linkTicket: Int): Observable<Uri> {
        return Observable.create { emitter ->
            try {
                val content = FileUtil.loadContentFromFile(context, path)
                val document: Document = Jsoup.parse(content)
                val res = doParseDocument(document)
                if (res.linkList.size > 3 && !res.isCaptcha)
                {
                    val uri = Uri.parse(res.linkList[linkTicket])
                    return@create emitter.onNext(uri)
                }
                if (res.isCaptcha)
                {
                    val uri = Uri.parse(res.captchaLink)
                    return@create emitter.onNext(uri)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            } finally {
                emitter.onComplete()
            }
        }
    }

//    override fun getLinksFromNet(param: String, linkTicket: Int): Observable<Uri> {
//        var disposable: Disposable? = null
//        return Observable.create { emitter ->
//            try {
//                disposable = INetWork.create().search(param)
//                    .subscribe { doc ->
//                        val res = doParseDocument(doc)
//                        if (res.linkList.size > 3 && !res.isCaptcha)
//                        {
//                            val uri = Uri.parse(res.linkList[linkTicket])
//                            emitter.onNext(uri)
//                            return@subscribe
//                        }
//                        if (res.isCaptcha)
//                        {
//                            val uri = Uri.parse(res.captchaLink)
//                            return@subscribe emitter.onNext(uri)
//                        }
//                    }
//            } catch (e: Exception)
//            {
//                emitter.onError(e)
//            } finally
//            {
//                emitter.onComplete()
//                disposable?.dispose()
//            }
//        }
//    }

    override fun getLinksByJsoup(): Observable<Uri> {
        //val url = "https://yandex.ru/search/?text=$param"
        return Observable.create { emitter ->
            try
            {
                val document: Document = Jsoup.connect(MainFragment.LINK).get()
                val res = doParseDocument(document)
                if (res.linkList.size > 3 && !res.isCaptcha)
                {
                    val uri = Uri.parse(res.linkList[MainFragment.LINK_TICKET])
                    return@create emitter.onNext(uri)
                }
                if (res.isCaptcha)
                {
                    val uri = Uri.parse(res.captchaLink)
                    return@create emitter.onNext(uri)
                }
            } catch (e: Exception)
            {
                emitter.onError(e)
            } finally
            {
                emitter.onComplete()
            }
        }
    }

    data class Result(val linkList: ArrayList<String>, val captchaLink: String?, val isCaptcha: Boolean)

    private fun doParseDocument(document: Document) : Result
    {
        val body = document.body()
        val isResult: Elements? = body.select("#search-result")
        if (isResult != null && isResult.size > 0)
        {
            val link1 = isResult.get(0).select("#search-result > li:nth-child(3) a")
                .attr("href")
            val link2 = body.select("#search-result > li:nth-child(4) a").attr("href")
            val link3 = body.select("#search-result > li:nth-child(5) a").attr("href")
            val link4 = body.select("#search-result > li:nth-child(6) a").attr("href")
            return Result(arrayListOf(link1, link2, link3, link4), null, false)
        }
        val isCaptcha: Elements? = body.select("form.form_error_no")
        if (isCaptcha != null && isCaptcha.size > 0)
        {
            val location = document.location()
            if (location.contains("showcaptcha"))
            {
                return Result(arrayListOf(), location, true)
            }
        }
        return Result(arrayListOf(), null, false)
    }


}