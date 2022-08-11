package blockchain.backbone

import java.security.MessageDigest

class Blockchain(_difficultyLevel: Int) {
    var difficultyLevel = _difficultyLevel
    private set
    private var length: Int = 0
    private val blocksList: MutableList<Block> = mutableListOf()
    private val blockBeingHashedData: MutableList<String> = mutableListOf()
    private val transactionsLedger: TransactionsLedger = TransactionsLedger()

    @Synchronized fun appendNewBlock(newBlock: Block): Boolean {
        if (validateComingBlock(newBlock)) {
            blocksList.add(newBlock)
            transactionsLedger.addBlockReward(newBlock.miner, 100)
            length++
            blockBeingHashedData.clear()
            evaluateDifficultyLevel(newBlock)
            return true
        }
        return false
    }

    // No need for this step at this stage
    @Synchronized private fun evaluateDifficultyLevel(newBlock: Block) {
//        if (newBlock.creationTime < 60) {
//            difficultyLevel++
//        } else {
//            difficultyLevel--
//        }
    }

    private fun validateComingBlock(block: Block): Boolean {
        if (length == 0) {
            return testFirstBlock(block)
        }
        return testAfterFirstBlock(block)
    }

    @Synchronized fun printBlockchain(){
        for (block in blocksList) {
            println(block)
        }
    }

    private fun testAfterFirstBlock(newBlock: Block): Boolean {
        val lastCurrentBlock: Block = blocksList.last()

        if (newBlock.id <= lastCurrentBlock.id) {
            return false
        }
        if (newBlock.timestamp <= lastCurrentBlock.timestamp) {
            return false
        }
        if (newBlock.previousBlockHash != lastCurrentBlock.currentBlockHash) {
            return false
        }
        if (applySha256(lastCurrentBlock.toStringWithoutCurrentHashField()) != lastCurrentBlock.currentBlockHash) {
            return false
        }

        return true
    }

    private fun testFirstBlock(firstBlock: Block): Boolean {
        if (firstBlock.id != 1) {
            return false
        }
        if (firstBlock.previousBlockHash != "0") {
            return false
        }
        if (applySha256(firstBlock.toStringWithoutCurrentHashField()) != firstBlock.currentBlockHash) {
            return false
        }

        return true
    }

    @Synchronized fun getLastBlock(): Block? {
        if (blocksList.isNotEmpty()) return blocksList.last()
        return null
    }

    @Synchronized fun addTransaction(transaction: TransactionsLedger.Transaction) {
        if (transactionsLedger.validateTransaction(transaction)) {
            transactionsLedger.addTransaction(transaction)
            addDataToBlockBeingHashed(transaction.toString())
        }
    }
    fun createTransaction(sender: String, receiver: String, amount: Int): TransactionsLedger.Transaction {
        return transactionsLedger.createTransaction(sender, receiver, amount)
    }

    @Synchronized private fun addDataToBlockBeingHashed(data: String) {
        blockBeingHashedData.add(data)
    }
    fun getDataOfBlockBeingHashed(): String {
        return if (blockBeingHashedData.isEmpty()) "No Transactions"
        else blockBeingHashedData.joinTo(StringBuilder(), " \n").toString()
    }

    fun applySha256(input: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            /* Applies sha256 to our input */
            val hash = digest.digest(input.toByteArray(charset("UTF-8")))
            val hexString = StringBuilder()
            for (elem in hash) {
                val hex = Integer.toHexString(0xff and elem.toInt())
                if (hex.length == 1) hexString.append('0')
                hexString.append(hex)
            }
            hexString.toString()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}