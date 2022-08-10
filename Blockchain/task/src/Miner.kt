package blockchain

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class Miner(private val minerName: String, private val blockchain: Blockchain, private val running: AtomicBoolean): Runnable {

    override fun run() {
        while (running.get()) {
            try {
                val newBlock = createNewBlock(blockchain.getLastBlock()) ?: return
                if (blockchain.appendNewBlock(newBlock)) {
                    running.set(false)
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    private fun createNewBlock(previousBlock: Block?): Block? {
        val creationStartingTime = System.currentTimeMillis()

        val newBlock = Block()
        newBlock.id = (previousBlock?.id?.plus(1)) ?: 1
        newBlock.miner = minerName
        newBlock.previousBlockHash = previousBlock?.currentBlockHash ?: "0"
        newBlock.difficultyLevel = blockchain.difficultyLevel

        // Generating how the hash should look like
        val difficultyStringBuilder: StringBuilder = StringBuilder()
        for (i in 1..blockchain.difficultyLevel){
            difficultyStringBuilder.append("0")
        }
        val difficultyString = difficultyStringBuilder.toString()

        /*
        Testing for the magic number that will pass the difficulty level
        And changing the timestamp and creation time after each iteration in order to be included in the final hash
        If they were included after testing the hash, this would mean that they were not hashed
         */
        // Keep digging for the new block till the thread gets a flag to stop from outside
        while (running.get()) {
            newBlock.magicNumber = Random().nextLong()
            newBlock.timestamp = System.currentTimeMillis()
            newBlock.creationTime = ((System.currentTimeMillis() - creationStartingTime) / 1000).toInt()
            newBlock.blockData = blockchain.getDataOfBlockBeingHashed()
            newBlock.currentBlockHash = blockchain.applySha256(newBlock.toStringWithoutCurrentHashField())

            if (newBlock.currentBlockHash.startsWith(difficultyString)) {
                return newBlock
            }
        }
        return null
    }
}