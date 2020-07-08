# Blockchain system

This system contains the chain of blocks. Every block (Chain.java) include:
* a hash of the previous block
* an own hash (create from a magic number, a time stamp, a number of blocks.  And this based on SHA256) (StringUtil.java)
* a magic number (random (int) digit between 10e6 & 10e7)
* an own generation time
* a previous block of chain
* a list messages of users
* a miner id (a number tread which created this block)

The class with a main logic is BlockchainSystem (BlockchainSystem.java). This class load an old data with serialize. Then mining blocks.
Because  this process is difficult, so I use multihreading. And which the first thread find a correct block. This block save in special file with serialize.
This class look after all system:
* a load/save(deligate SerializeFile.java)/validate(deligate BlockChainValidator.java) old data
* a number of blocks
* a last block
* paths file to save data
