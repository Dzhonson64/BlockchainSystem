# Blockchain system

## Information about the app content

This system contains the chain of blocks. Every block (*Chain.java*) include:
* a hash of the previous block
* an own hash (create from a magic number, a time stamp, a number of blocks.  And this based on SHA256) (*StringUtil.java*)
* a magic number (random (int) digit between 10e6 & 10e7)
* an own generation time
* a previous block of chain
* a list messages of users
* a miner id (a number tread which created this block)
* an identity

The class with a main logic is BlockchainSystem (*BlockchainSystem.java*). This class load an old data with serialize. Then mining blocks.
Because  this process is difficult, so I use multihreading. And which the first thread find a correct block. This block save in special file with serialize.
This class look after all system:
* a load old data/save(deligate *SerializeFile.java*)/validate(deligate *BlockChainValidator.java*) 
* a number of blocks
* a last block
* paths file to save data

The class *Message* has a text message (list of bytes). This can complete:
* create sign
* get a private key

To check the recieve message you need to verify him. It's can class *VerifyMessage*. This class verify based on a public key and a sign.

All serialization methods contain in the class *SerializeFile*. There are method read/write one object and read/write without close to read/write all chain of blocks.

The class *GenerateKeys* create public and private keys. And then write and save to file on the computer.

The class *StringUtil*  is responsible for create hash. I choose SHA-256 method.

## Detailed information about the methods I use in my blockchain system

###  A proof of work concept 
Also, this system has a solution called ***proof of work***. This means that creating new blocks and fixing hash values int the existing ones should some take time and shouldn't be instatnt. The time should depend on the amount of computational work put ibto it. 

Because, a hash shouldn't be random. It should start with some anount of zeros. To achive that, the block should contain an additional field: a ***magic number***. This number should take part in calculating the hash. With one magic number, and with another, the hashes would be totally different even though the other part of the block stays the same. The only way to find one of them is to make random guesses until we found one of them. For a computer, this means that the only way to find the solution is to brute force it: try 1, 2, 3, and so on.

And finally, if the hacker wants to change some information in the middle of the blockchain, the hash of the modified block would be changed and it won't start with zeros, so the hacker would be forced to find another magic number to create a block with a hash which starts with zeros. Note that the hacker must find magic numbers for all of the blocks until the end of the blockchain, which seems like a pretty impossible task, considering that the blockchain will grow faster.

It's said that that the ***block is proved*** if it has a hash which starts with some number of zeros. The information inside it is impossible to change even though the information itself is open and easy to edit in the text editor. The result of the edit is a changed hash of the block, no longer containing zeros at the start, so this block suddenly becomes ***unproved*** after the edit. And since the blockchain must consist of only proved blocks, the whole blockchain becomes invalid. This is the power of the proof of work concept.

### Miner mania
The blockchain itself shouldn't create new blocks. The blockchain just keeps the chain valid and accepts the new blocks from outside. In the outside world, there are a lot of computers that try to create a new block. All they do is search for a magic number to create a block whose hash starts with some zeros. The first computer to do so is a winner, the blockchain accepts this new block, and then all these computers try to find a magic number for the next block.

There is a special word for this: mining. The process of mining blocks is hard work for computers, like the process of mining minerals in real life is hard work. Computers that perform this task are called miners.

Note that if there are more miners, the new blocks will be mined faster. But the problem is that we want to create new blocks with a stable frequency. If suddenly there are so many miners that the new block is created in a matter of seconds, the complexity of the next block should be increased by increasing the number N (the number of zeros at the start of a hash of the new block).

### Messages
The most useful information in the blockchain is the data that every block stores. I create a simple chat based on the blockchain. If this blockchain works on the internet, it would be a world-wide chat. Everyone can add a line to this blockchain, but no one can edit it afterward. Every message would be visible to anyone. 

A block contain messages that the blockchain received during the creation of the previous block. When the block was created, all new messages should become a part of the new block, and all the miners should start to search for a magic number for this block. New messages, which were sent after this moment, shouldn't be included in this new block.

### Security

It's possible that anyone send send a message using name. Without encryption, this is totally possible. There needs to be a method to verify that it is actually you who sent this message. . Note that the registration/authorization method is bad because there is no server to check for a valid login/password pair. And if there is, it can be cracked by the hackers who can steal your password. There needs to be a whole new level of security.

I used ***asymmetric cryptography*** (RSA method). With this, you can sign the message and let the signature be a special part of the message. 
You can generate a pair of keys: a public key and a private key. The message should be signed with a private key. And anyone can verify that the message and the signature pair is valid using a public key. The private key should be only on your computer, so no one from the internet can steal it.

The public key is for encryption, the private key is for decryption. If person "A" encrypted the data on his private key, recipient "B" will need the public key "A" to decrypt it. Recipient "B" can not only decrypt the "A" message, but also respond "A" with an encrypted message. To do so, they need to encrypt their response with the public key "A", so that "A" can decrypt that response with its private key. When using an asymmetric algorithm, it is impossible to encrypt and decrypt the message with the same key; these keys, although mathematically related, do not match (as opposed to symmetric algorithms). "A" can encrypt data on its private key, then the recipient "B" can decrypt it on its public key "A". When decrypting a message on the public key "A", the recipient can be sure that the message really comes from "A", because the message can only be decrypted on the public key "A" if it was encrypted on the corresponding private key "A". This ensures that authentication is possible because "A" is the only one who has this private key. If recipient "B" wants to make sure that the only one who can read his response is "A", he must encrypt his message "A" on his public key. Then only "A" will be able to decrypt the message, since only he has the required private key.

There are 2 message formats:
* If the sender is more concerned with the confidentiality of the transmitted information, he should encrypt his message on the recipient's public key. This is called a ***secure message format*** because only someone with the appropriate private key can decrypt the message.
* Encryption of data on the sender's private key is called an ***open message format*** because anyone can decrypt the data with the sender's public key. Confidentiality is not ensured.

A hacker can't just take any message and sign it like it is your message, but he can take an already signed message and paste it into the blockchain again; the signature of this message stays the same, doesn’t it? For this reason, all messages contain a unique identifier, and all these identifiers are in ascending order in the blockchain.


In the case of a signature, a digital signature is a relatively small amount of additional digital information transmitted with the text to be signed. The electronic digital signature protocol includes two procedures:
1) procedure of signature placement; it uses the secret key of the message sender;
2) signature verification procedure, which uses the sender's public key.

Impossibility of forgery of EDS is guaranteed by two moments: impossibility to define a secret key on open in asymmetric cryptosystems and impossibility of change of the document so that hash function remained without change.
