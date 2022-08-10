package blockchain

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

fun main() {
    val blockchain = Blockchain(0)

    // 5 calls to generate 5 blocks
    for (i in 1..5) {
        // Number of miners
        // Once a miner finds a new block, it signals to all other miners to stop their search and terminate
        // This call generates one block
        val executors = Executors.newFixedThreadPool(10)
        val newBlockNotFound = AtomicBoolean(true)
        for (ii in 1..10) {
            val miner = Miner("$ii", blockchain, newBlockNotFound)
            executors.submit(miner)
        }
        blockchain.addDataToBlockBeingHashed(generateRandomChars())
        executors.shutdown()
        executors.awaitTermination(3, TimeUnit.SECONDS )
    }


    blockchain.printBlockchain()

}

fun generateRandomChars(): String {
    val candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
    val sb = StringBuilder()
    val random = Random.Default
    for (i in 0 until 20) {
        sb.append(
            candidateChars[random.nextInt(
                candidateChars
                    .length
            )]
        )
    }
    return sb.toString()
}