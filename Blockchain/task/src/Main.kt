package blockchain

import blockchain.backbone.Blockchain
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

fun main() {
    val blockchain = Blockchain(0)

    // 15 calls to generate 15 blocks
    for (i in 1..15) {
        // Number of miners
        // Once a miner finds a new block, it signals to all other miners to stop their search and terminate
        // This call generates one block
        val executors = Executors.newFixedThreadPool(5)
        val newBlockNotFound = AtomicBoolean(true)
        for (ii in 1..5) {
            val miner = Miner("miner$ii", blockchain, newBlockNotFound)
            executors.submit(miner)
        }
        val transaction0 = blockchain.createTransaction("miner1", "miner5", 50)
        val transaction1 = blockchain.createTransaction("miner5", "miner3", 50)
        blockchain.addTransaction(transaction0)
        blockchain.addTransaction(transaction1)

        executors.shutdown()
        executors.awaitTermination(3, TimeUnit.SECONDS )
    }


    blockchain.printBlockchain()
}
