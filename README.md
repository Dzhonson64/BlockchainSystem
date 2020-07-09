# Blockchain system

This system contains the chain of blocks. Every block (*Chain.java*) include:
* a hash of the previous block
* an own hash (create from a magic number, a time stamp, a number of blocks.  And this based on SHA256) (StringUtil.java)
* a magic number (random (int) digit between 10e6 & 10e7)
* an own generation time
* a previous block of chain
* a list messages of users
* a miner id (a number tread which created this block)
* an identity

The class with a main logic is BlockchainSystem (BlockchainSystem.java). This class load an old data with serialize. Then mining blocks.
Because  this process is difficult, so I use multihreading. And which the first thread find a correct block. This block save in special file with serialize.
This class look after all system:
* a load/save(deligate SerializeFile.java)/validate(deligate BlockChainValidator.java) old data
* a number of blocks
* a last block
* paths file to save data

Also, this system has a solution called ***proof of work***. This means that creating new blocks and fixing hash values int the existing ones should some take time and shouldn't be instatnt. The time should depend on the amount of computational work put ibto it. 

Because, a hash shouldn't be random. For example, It should start with some anount of zeros. To achive that, the blck should contain an additional field: a ***magic number***. This number should take part in calculating the hash. With one magic number, and with another, the hashes would be totally different even though the other part of the block stays the same. The only way to find one of them is to make random guesses until we found one of them. For a computer, this means that the only way to find the solution is to brute force it: try 1, 2, 3, and so on.

And finally, if the hacker wants to change some information in the middle of the blockchain, the hash of the modified block would be changed and it won't start with zeros, so the hacker would be forced to find another magic number to create a block with a hash which starts with zeros. Note that the hacker must find magic numbers for all of the blocks until the end of the blockchain, which seems like a pretty impossible task, considering that the blockchain will grow faster.

It's said that that the ***block is proved*** if it has a hash which starts with some number of zeros. The information inside it is impossible to change even though the information itself is open and easy to edit in the text editor. The result of the edit is a changed hash of the block, no longer containing zeros at the start, so this block suddenly becomes ***unproved** after the edit. And since the blockchain must consist of only proved blocks, the whole blockchain becomes invalid. This is the power of the proof of work concept.
