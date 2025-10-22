package io.horizontalsystems.bankwallet.modules.blockchainsettings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.BaseComposeFragment
import io.horizontalsystems.bankwallet.core.slideFromBottom
import io.horizontalsystems.bankwallet.core.stats.StatEvent
import io.horizontalsystems.bankwallet.core.stats.StatPage
import io.horizontalsystems.bankwallet.core.stats.stat
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.AppBar
import io.horizontalsystems.bankwallet.ui.compose.components.CellUniversalLawrenceSection
import io.horizontalsystems.bankwallet.ui.compose.components.HsBackButton
import io.horizontalsystems.bankwallet.ui.compose.components.RowUniversal
import io.horizontalsystems.bankwallet.ui.compose.components.body_leah
import io.horizontalsystems.bankwallet.ui.compose.components.subhead2_grey

class BlockchainSettingsFragment : BaseComposeFragment() {

    @Composable
    override fun GetContent(navController: NavController) {
        BlockchainSettingsScreen(
            navController = navController,
        )
    }

}

@Composable
private fun BlockchainSettingsScreen(
    navController: NavController,
    viewModel: BlockchainSettingsViewModel = viewModel(factory = BlockchainSettingsModule.Factory()),
) {
    Surface(color = ComposeAppTheme.colors.tyler) {
        Column(
            modifier = Modifier.navigationBarsPadding()
        ) {
            AppBar(
                title = "Solana 设置",  // ✅ 改为 Solana 设置
                navigationIcon = {
                    HsBackButton(onClick = { navController.popBackStack() })
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(Modifier.height(12.dp))

                // ✅ 仅保留 Solana 链
                val solanaChains = viewModel.otherChains.filter {
                    it.blockchainItem is BlockchainSettingsModule.BlockchainItem.Solana
                }

                BlockchainSettingsBlock(
                    solanaChains = solanaChains,
                    navController = navController
                )

                Spacer(Modifier.height(44.dp))
            }
        }
    }
}

@Composable
fun BlockchainSettingsBlock(
    solanaChains: List<BlockchainSettingsModule.BlockchainViewItem>,
    navController: NavController
) {
    CellUniversalLawrenceSection(solanaChains) { item ->
        BlockchainSettingCell(item) {
            onClick(item, navController)
        }
    }
}

private fun onClick(
    item: BlockchainSettingsModule.BlockchainViewItem,
    navController: NavController
) {
    when (item.blockchainItem) {
        is BlockchainSettingsModule.BlockchainItem.Solana -> {
            navController.slideFromBottom(R.id.solanaNetworkFragment)

            stat(
                page = StatPage.BlockchainSettings,
                event = StatEvent.Open(StatPage.BlockchainSettingsSolana)
            )
        }
        else -> { /* 不处理其他链 */ }
    }
}

@Composable
private fun BlockchainSettingCell(
    item: BlockchainSettingsModule.BlockchainViewItem,
    onClick: () -> Unit
) {
    RowUniversal(
        onClick = onClick
    ) {
        Image(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(32.dp),
            painter = rememberAsyncImagePainter(
                model = item.imageUrl,
                error = painterResource(R.drawable.ic_platform_placeholder_32)
            ),
            contentDescription = null,
        )
        Column(modifier = Modifier.weight(1f)) {
            body_leah(text = item.title)
            subhead2_grey(text = item.subtitle)
        }
        Icon(
            modifier = Modifier.padding(horizontal = 16.dp),
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = ComposeAppTheme.colors.grey
        )
    }
}
