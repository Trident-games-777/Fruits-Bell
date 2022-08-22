package ai.joblio.ap.ui.fragments

import ai.joblio.ap.R
import ai.joblio.ap.ui.viewmodel.FruitViewModel
import ai.joblio.ap.util.Checkers
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoadingFragment : Fragment(R.layout.loading_fragment) {

    private val viewModel: FruitViewModel by viewModels()
    private val checker = Checkers()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (!checker.isDeviceSecured(requireActivity())) {
            true -> {
                startGame()
            }
            false -> {
                Timber.d("Checking db")
                viewModel.getUrlFromDb().observe(viewLifecycleOwner) { urlEntity ->
                    if (urlEntity == null) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.fetchDeeplink(requireActivity())
                            Timber.d("started deeplink")
                        }
                        lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.urlLiveData.observe(viewLifecycleOwner) { url ->
                                startWebView(url)
                                Timber.d("started web from new url")
                            }
                        }
                    } else {
                        viewModel.getUrlFromDb().observe(viewLifecycleOwner) { urlEntity ->
                            startWebView(urlEntity?.url ?: "smth went wrong")
                            Timber.d("started web from db")
                        }
                    }
                }
            }
        }

    }

    private fun startWebView(url: String) {
        val bundle = bundleOf("url" to url)
        findNavController().navigate(R.id.web_view, bundle)
    }

    private fun startGame() {
        findNavController().navigate(R.id.gameFragment2)
    }
}