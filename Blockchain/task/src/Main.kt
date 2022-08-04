package blockchain


fun main() {
    val blockchain = Blockchain()

    blockchain.generateNewBlock()
    blockchain.generateNewBlock()
    blockchain.generateNewBlock()
    blockchain.generateNewBlock()
    blockchain.generateNewBlock()

    blockchain.printBlockchain()
}

