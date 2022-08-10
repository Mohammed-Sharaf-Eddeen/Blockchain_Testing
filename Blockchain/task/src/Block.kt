package blockchain


class Block {
    var id: Int = 0
    var miner: String = ""
    var timestamp: Long = 0L
    var magicNumber: Long = 0L
    var previousBlockHash: String = ""
    var currentBlockHash: String = ""
    var creationTime: Int = 0
    var difficultyLevel: Int = 0
    var blockData: String = ""

    override fun toString(): String {
            return "Block: \n" +
                "Created by: miner # $miner \n" +
                "Id: $id \n" +
                "Timestamp: $timestamp \n" +
                "Magic number: $magicNumber \n" +
                "Hash of the previous block: \n" +
                "$previousBlockHash \n" +
                "Hash of the block: \n" +
                "$currentBlockHash \n" +
                "Block data: \n" +
                "$blockData \n" +
                "Block was being generated for $creationTime seconds \n" +
                "Difficulty Level (N) is $difficultyLevel \n"
    }

    // currentBlockHash can't be hashed because it is not already there yet! What is being hashed is its default value!
    // So after adding it to the block and trying to validate the hash, the validation would fail.
    // So, it should not be there for hashing from the beginning
    fun toStringWithoutCurrentHashField(): String {
        return "Block: \n" +
                "Created by: miner # $miner \n" +
                "Id: $id \n" +
                "Timestamp: $timestamp \n" +
                "Magic number: $magicNumber \n" +
                "Hash of the previous block: \n" +
                "$previousBlockHash \n" +
                "Block was generating for $creationTime seconds \n" +
                "Block data: \n" +
                "$blockData \n"
    }
}
