package ai.joblio.ap.ui.fragments

import ai.joblio.ap.FruitsApp
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
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoadingFragment : Fragment(R.layout.loading_fragment) {

    private val viewModel: FruitViewModel by viewModels()
    private val checker = Checkers()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            FruitsApp.gadId =
                AdvertisingIdClient.getAdvertisingIdInfo(requireContext()).id.toString()
            OneSignal.setExternalUserId(FruitsApp.gadId)
        }

        when (checker.isDeviceSecured(requireActivity())) {
            true -> {
                startGame()
            }
            false -> {
                viewModel.getUrlFromDb().observe(viewLifecycleOwner) { urlEntity ->
                    if (urlEntity == null) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.fetchData(requireActivity())
                        }
                        lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.urlLiveData.observe(viewLifecycleOwner) { url ->
                                startWebView(url)
                            }
                        }
                    } else {
                        viewModel.getUrlFromDb().observe(viewLifecycleOwner) { uE ->
                            startWebView(uE?.url ?: "smth went wrong")
                        }
                    }
                }
            }
        }
    }

    private fun startWebView(url: String) {
        val bundle = bundleOf("url" to url)
        findNavController().navigate(R.id.webViewFragment, bundle)
    }

    private fun startGame() {
        findNavController().navigate(R.id.startFragment)
    }
}