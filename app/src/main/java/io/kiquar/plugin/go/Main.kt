package io.kiquar.plugin.go

import androidx.annotation.Keep
import com.rk.extension.ExtensionAPI
import com.rk.extension.ExtensionContext
import com.rk.lsp.LspRegistry
import com.rk.utils.getTempDir
import com.rk.file.child
import com.rk.runner.RunnerManager
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver
import java.io.File
import io.kiquar.plugin.go.runner.GoRunner
import io.kiquar.plugin.go.runner.GoModRunner
import com.rk.file.FileTypeManager

@Keep
@Suppress("unused")
class Main(context: ExtensionContext) : ExtensionAPI(context) {
    
    private var goServer: GoServer? = null
    private var fileResolver: AssetsFileResolver? = null
    private var goRunner: GoRunner? = null
    private var goModRunner: GoModRunner? = null
    
    override fun onLoad() {
        loadServer()
        loadLanguages()
        loadRunners()
    }

    override fun onDispose() {
        dispose()
    }
    
    private fun acquireLspInstallScript(): File {
        val assetStream = context.assets.open("install-gopls.sh")
        val assetContent = assetStream.bufferedReader().use { it.readText() }
        val scriptFile = getTempDir().child("install-gopls.sh").also {
            it.writeText(assetContent)
            it.setExecutable(true)
        }
        return scriptFile
    }
    
    private fun dispose() {
        goServer?.let {
            LspRegistry.unregisterServer(it)
            goServer = null
        }
        goRunner?.let {
            RunnerManager.unregisterRunner(it)
            goRunner = null
        }
        goModRunner?.let {
            RunnerManager.unregisterRunner(it)
            goModRunner = null
        }
    }
    
    private fun loadServer() {
        dispose()
        goServer = GoServer(
            installScript = acquireLspInstallScript(),
            context = context
        ).also {
            LspRegistry.registerServer(it)
        }
    }
    
    private fun loadLanguages() {
        val fileProviderRegistry = FileProviderRegistry.getInstance()
        fileResolver = AssetsFileResolver(context.assets)
        fileProviderRegistry.addFileProvider(fileResolver)

        val grammarRegistry = GrammarRegistry.getInstance()
        grammarRegistry.loadGrammars("lang/language.json")

        GoModLanguage(context.resources).also {
            FileTypeManager.register(it)
        }
    }
    
    private fun loadRunners() {
        goRunner = GoRunner().also {
            RunnerManager.registerRunner(it)
        }
        goModRunner = GoModRunner().also {
            RunnerManager.registerRunner(it)
        }
    }
}