package org.choottd.librcon.session.event

data class ServerCmdData(
    val client: ClientData,
    val company: CompanyData,
    val command: CommandData,
    val p1: Long,
    val p2: Long,
    val title: Long,
    val text: String,
    val frame: Long
)
