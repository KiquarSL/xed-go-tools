package io.kiquar.plugin.go.runner

import android.content.Context
import android.app.Activity
import com.rk.file.FileObject
import com.rk.icons.Icon
import com.rk.runner.Runner
import com.rk.file.BuiltinFileType
import com.rk.exec.launchTerminal
import com.rk.exec.TerminalCommand

class GoRunner(
    val icon: Icon? = BuiltinFileType.GO.icon,
) : Runner() {

    override val id = "go.run"
    override val label = "Run Go File"

    override fun getIcon(context: Context) = icon

    fun matcher(fileObject: FileObject): Boolean {
        return fileObject.getExtension() == "go"
    }

    suspend fun run(activity: Activity, fileObject: FileObject) {
        val workingDir = fileObject.getParentFile()?.getAbsolutePath()
        launchTerminal(
            activity = activity,
            terminalCommand = TerminalCommand(
                exe = "/bin/go",
                args = arrayOf("run", fileObject.getAbsolutePath()),
                id = id,
                workingDir = workingDir,
            ),
        )
    }

    override suspend fun isRunning(): Boolean = false

    override suspend fun stop() {}
}