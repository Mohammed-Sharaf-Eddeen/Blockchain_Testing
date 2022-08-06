package blockchain

import java.security.MessageDigest
import java.util.*

class Blockchain(private var difficultyLevel: Int) {
    private var length: Int = 0
    private val blocksList: MutableList<Block> = mutableListOf()

    fun generateNewBlock() {
        val newBlock = Block()
        blocksList.add(newBlock)
        length++
    }

    fun printBlockchain(){
        for (block in blocksList) {
            println(block)
        }
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

    inner class Block {
        private val id: Int = length + 1
        private val timestamp: Long = System.currentTimeMillis()
        private var magicNumber: Long
        private val previousBlockHash: String
        private val currentBlockHash: String
        private val creationTime: Int

        init {
            val creationStartingTime = System.currentTimeMillis()
            previousBlockHash = if (blocksList.isEmpty()) "0" else blocksList.last().currentBlockHash

            // Generating how the hash should look like
            val difficultyStringBuilder: StringBuilder = StringBuilder()
            for (i in 1..difficultyLevel){
                difficultyStringBuilder.append("0")
            }
            val difficultyString = difficultyStringBuilder.toString()

            // Testing for the magic number that will pass the difficulty level
            while (true) {
                magicNumber = Random().nextLong()
                val testedHash = applySha256(this.toString())
                if (testedHash.startsWith(difficultyString)) {
                    break
                }
            }
            currentBlockHash = applySha256(this.toString())
            creationTime = ((System.currentTimeMillis() - creationStartingTime) / 1000).toInt()
        }

        override fun toString(): String {
            return "Block: \n" +
                    "Id: $id \n" +
                    "Timestamp: $timestamp \n" +
                    "Magic number: $magicNumber \n" +
                    "Hash of the previous block: \n" +
                    "$previousBlockHash \n" +
                    "Hash of the block: \n" +
                    "$currentBlockHash \n" +
                    "Block was generating for $creationTime seconds \n"

        }
    }
}