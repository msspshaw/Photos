package com.shaw.photos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.webview.*
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar

private const val urlTag = "url"
const val infoFragmentClassTag = "InfoFragment"

fun infoFragmentNewInstance(url: String): InfoFragment {
    val fragment = InfoFragment()
    val args = Bundle()
    args.putString(urlTag, url)
    fragment.arguments = args
    return fragment
}

class InfoFragment: Fragment(), View.OnClickListener {
    // The decision was made to separate web stack from app stack for back - to navigate back on web
    // must use the web back, and hitting device back will return you to the previous app state
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.backButton -> webView.goBack()
            R.id.forwardButton -> webView.goForward()
            R.id.reloadButton -> reload()
        }
    }

    private fun reload() {
        Log.v(infoFragmentClassTag, "Reloading, url: " + webView.url)
        load(webView.url)
    }

    private fun load(url: String) {
        progressBar.visibility = View.VISIBLE
        webView.loadUrl(url)
    }

    private val infoFragmentInstantiationError = "InfoFragment was not properly instantiated with infoFragmentNewInstance()"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebviewClient()
        val url = arguments?.getString(urlTag)
        if (url != null) {
            load(url)
        }
        else {
            Log.e(infoFragmentClassTag, infoFragmentInstantiationError)
        }
    }

    private fun setupWebviewClient() {
        // Disable loading new urls in default web browser, keeping them in custom web view instead
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                backButton.isEnabled = webView.canGoBack()
                forwardButton.isEnabled = webView.canGoForward()
                super.onPageFinished(view, url)
            }
        }
        backButton.setOnClickListener(this)
        forwardButton.setOnClickListener(this)
        reloadButton.setOnClickListener(this)
    }
}