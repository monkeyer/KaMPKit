package co.touchlab.kampstarter

import kotlinx.coroutines.initMainThread
import kotlinx.coroutines.runBlocking
import platform.CoreFoundation.CFRunLoopRun
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze
import kotlin.native.internal.test.testLauncherEntryPoint
import kotlin.system.exitProcess

actual fun <T> runTest(block: suspend () -> T) {
    runBlocking { block() }
}

class SqlDelightTestJvm : SqlDelightTest()

//TODO: Need to figure out koin main thread requirement
//class BreedModelTestJvm : BreedModelTest()

fun mainBackground(args: Array<String>) {
    initMainThread() // not really needed, since runtime is initialized by the main thread here anyway
    val worker = Worker.start(name = "main-background")
    worker.execute(TransferMode.SAFE, { args.freeze() }) {
        val result = testLauncherEntryPoint(it)
        exitProcess(result)
    }
    CFRunLoopRun()
    error("CFRunLoopRun should never return")
}