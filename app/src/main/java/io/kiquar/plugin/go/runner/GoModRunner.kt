package io.kiquar.plugin.go.runner

import android.content.Context
import android.app.Activity
import android.content.res.Resources
import com.rk.file.FileObject
import com.rk.icons.Icon
import com.rk.runner.Runner
import com.rk.file.BuiltinFileType
import com.rk.exec.launchTerminal
import com.rk.exec.TerminalCommand
import com.rk.activities.main.MainActivity
import java.io.File

class GoModRunner(
    val icon: Icon? = BuiltinFileType.GO.icon,
) : Runner() {

    override val id = "go.mod.run"
    override val label = "Run Go Project"

    override fun getIcon(context: Context) = icon

    override fun matcher(fileObject: FileObject): Boolean {
        return fileObject.getName() == "go.mod"
    } 

    override suspend fun run(activity: Activity, fileObject: FileObject) {
        val projectRoot = fileObject.getParentFile()?.getAbsolutePath()
        launchTerminal(
            activity = activity,
            terminalCommand = TerminalCommand(
                exe = "/bin/go",
                args = arrayOf("run", "."),
                id = id,
                workingDir = projectRoot,
            ),
        )
    }

    override suspend fun isRunning() = false

    override suspend fun stop() {}
}