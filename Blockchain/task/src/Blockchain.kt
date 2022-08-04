package blockchain

import java.security.MessageDigest

class Blockchain {
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
        private val previousBlockHash: String
        private val currentBlockHash: String

        init {
            previousBlockHash = if (blocksList.isEmpty()) "0" else blocksList.last().currentBlockHash
            currentBlockHash = applySha256(this.toString())
        }

        override fun toString(): String {
            return "Block: \n" +
                    "Id: $id \n" +
                    "Timestamp: $timestamp\n" +
                    "Hash of the previous block: \n" +
                    "$previousBlockHash \n" +
                    "Hash of the block: \n" +
                    "$currentBlockHash \n"
        }
    }
}