package io.horizontalsystems.bankwallet.modules.sendx

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.navigation.navGraphViewModels
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.BaseFragment
import io.horizontalsystems.bankwallet.modules.amount.AmountInputModeViewModel
import io.horizontalsystems.bankwallet.modules.sendx.binance.SendBinanceConfirmationScreen
import io.horizontalsystems.bankwallet.modules.sendx.binance.SendBinanceViewModel
import io.horizontalsystems.bankwallet.modules.sendx.bitcoin.SendBitcoinConfirmationScreen
import io.horizontalsystems.bankwallet.modules.sendx.bitcoin.SendBitcoinViewModel
import io.horizontalsystems.bankwallet.modules.sendx.zcash.SendZCashConfirmationScreen
import io.horizontalsystems.bankwallet.modules.sendx.zcash.SendZCashViewModel
import io.horizontalsystems.bankwallet.modules.xrate.XRateViewModel
import io.horizontalsystems.core.findNavController
import kotlinx.parcelize.Parcelize

class SendConfirmationFragment : BaseFragment() {
    val amountInputModeViewModel by navGraphViewModels<AmountInputModeViewModel>(R.id.sendXFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                when (requireArguments().getParcelable<Type>(typeKey)) {
                    Type.Bitcoin -> {
                        val sendBitcoinViewModel by navGraphViewModels<SendBitcoinViewModel>(R.id.sendXFragment)
                        val xRateViewModel by navGraphViewModels<XRateViewModel>(R.id.sendXFragment)

                        SendBitcoinConfirmationScreen(
                            findNavController(),
                            sendBitcoinViewModel,
                            xRateViewModel,
                            amountInputModeViewModel
                        )
                    }
                    Type.Bep2 -> {
                        val sendBinanceViewModel by navGraphViewModels<SendBinanceViewModel>(R.id.sendXFragment)

                        SendBinanceConfirmationScreen(
                            findNavController(),
                            sendBinanceViewModel,
                            amountInputModeViewModel
                        )
                    }
                    Type.ZCash -> {
                        val sendZCashViewModel by navGraphViewModels<SendZCashViewModel>(R.id.sendXFragment)

                        SendZCashConfirmationScreen(
                            findNavController(),
                            sendZCashViewModel,
                            amountInputModeViewModel
                        )
                    }
                    null -> Unit
                }
            }
        }
    }

    @Parcelize
    enum class Type : Parcelable {
        Bitcoin, Bep2, ZCash
    }

    companion object {
        private const val typeKey = "typeKey"

        fun prepareParams(type: Type) = bundleOf(
            typeKey to type
        )
    }
}