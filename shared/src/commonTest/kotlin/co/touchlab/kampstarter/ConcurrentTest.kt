package co.touchlab.kampstarter

import co.touchlab.stately.concurrency.AtomicInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class ConcurrentTest {
    @Test
    fun testMainThread() {
        val actions = AtomicInt(0)
        fun expect(index: Int) = assertEquals(index, actions.incrementAndGet())

        expect(1)
        runTest {
            val job = GlobalScope.launch(Dispatchers.Main) {
                delay(1000)
                expect(3)
            }
            expect(2)
            job.join()
            expect(4)
        }
    }
}