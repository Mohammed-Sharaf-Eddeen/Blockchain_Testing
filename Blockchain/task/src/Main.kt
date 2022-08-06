package blockchain


fun main() {
    print("Enter how many zeros the hash must start with: ")
    val difficultyLevel = readln().toInt()
    val blockchain = Blockchain(difficultyLevel)

    blockchain.generateNewBlock()
    blockchain.generateNewBlock()
    blockchain.generateNewBlock()
    blockchain.generateNewBlock()
    blockchain.generateNewBlock()

    blockchain.printBlockchain()
}
