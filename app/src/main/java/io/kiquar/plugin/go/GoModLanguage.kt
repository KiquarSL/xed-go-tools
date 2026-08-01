package io.kiquar.plugin.go

import android.content.res.Resources
import com.rk.file.FileType
import com.rk.icons.Icon
import com.rk.file.BuiltinFileType

class HaskellLanguage(resources: Resources) : FileType {
    override val extensions = listOf()
    override val textmateScope = "source.go.mod"
    override val name = "go.mod"
    override val title = "Go Module"
    override val icon = BuiltinFileType.PROPERTIES.icon
}