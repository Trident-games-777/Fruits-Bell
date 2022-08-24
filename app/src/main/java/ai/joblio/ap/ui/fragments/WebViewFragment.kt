package ai.joblio.ap.ui.fragments

import ai.joblio.ap.R
import ai.joblio.ap.databinding.WebViewFragmentBinding
import ai.joblio.ap.db.UrlEntity
import ai.joblio.ap.ui.viewmodel.FruitViewModel
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment : Fragment(R.layout.web_view_fragment) {

    lateinit var webView: WebView
    private val viewModel: FruitViewModel by viewModels()
    private var messageAB: ValueCallback<Array<Uri?>>? = null
    private var _binding: WebViewFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WebViewFragmentBinding.inflate(inflater, container, false)
        webView = binding.webView
        webView.loadUrl(arguments?.getString(ARGUMENTS_NAME) ?: "no args")
        webView.webViewClient = LocalClient()
        webView.settings.userAgentString = System.getProperty(SYSTEM_PROPERTY)
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = false
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                messageAB = filePathCallback
                selectImageIfNeeded()
                return true
            }

            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                val newWebView = WebView(requireContext())
                newWebView.webChromeClient = this
                newWebView.settings.domStorageEnabled = true
                newWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                newWebView.settings.javaScriptEnabled = true
                newWebView.settings.setSupportMultipleWindows(true)
                val transport = resultMsg?.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                newWebView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        view!!.loadUrl(url!!)
                        return true
                    }
                }
                return true
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        isEnabled = false
                    }
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            messageAB?.onReceiveValue(null)
            return
        } else if (resultCode == Activity.RESULT_OK) {
            if (messageAB == null) return

            messageAB!!.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(
                    resultCode, data
                )
            )
            messageAB = null
        }
    }

    private fun selectImageIfNeeded() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = INTENT_TYPE
        startActivityForResult(Intent.createChooser(intent, CHOOSER_TITLE), RESULT_CODE)
    }

    private inner class LocalClient : WebViewClient() {

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (url == BASE_URL) {
                findNavController().navigate(R.id.gameFragment2)
            } else {
                viewModel.getUrlFromDb().observe(viewLifecycleOwner) {
                    if (it == null) {
                        viewModel.insertUrlToDB(UrlEntity(url = url))
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARGUMENTS_NAME = "url"
        const val INTENT_TYPE = "image/*"
        const val CHOOSER_TITLE = "Image Chooser"
        const val BASE_URL = "https://fruitsbell.site/"
        const val RESULT_CODE = 1
        const val SYSTEM_PROPERTY = "http.agent"
    }

}