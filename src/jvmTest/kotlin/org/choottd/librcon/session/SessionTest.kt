package org.choottd.librcon.session

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.junit.jupiter.api.Test

internal class SessionTest {

    @Test
    fun testNovadartServer() {
        val session = Session("test", "1", "choottd", "localhost", 3977)
        session.sessionEvents.onEach {
            println(it)
        }.launchIn(GlobalScope)
        session.open()
        Thread.sleep(50000)
    }

}
