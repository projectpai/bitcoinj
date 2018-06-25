#### Project PAI - PAIcoin Demo Scripts

This folder contains scripts to demonstrate the usage of the bitcoinj library with PAIcoin and to provide tools for PAIcoin usage.

## Download the code

1. Create the appropriate directory and navigate to it
2. Clone the repository

        git clone https://github.com/projectpai/bitcoinj.git
   
3. Open the *bitcoinj* directory

        cd bitcoinj
   
4. Change the branch

        git checkout paicoin


## Build

1. Download dependencies and build the bitcoinj library

        mvn clean compile package install
   
2. Open the *paidemo* directory

        cd paidemo


## bitcoinj usage/modification

Project PAI is very similar to Bitcoin in regards to the protocol specifics. As such, we have updated bitcoinj with the network parameters required to interact with PAI here:
[https://github.com/projectpai/bitcoinj](https://github.com/projectpai/bitcoinj)
In the *paicoin* branch

We have also added a simple example of the requested interactions with PAI using bitcoinj in the paidemo directory of that branch.

# Usage

To run the script, execute:

    mvn exec:java -Dexec.mainClass=com.oben.PAIExample

Please note that the PAIExample contains hard-coded keys and addresses that are no longer usable so you will have to modify these to fit your needs. Ideally change the key to be a WIF
HD key for a reusable wallet and the address to whatever one you want to receive test funds.

You will also need to change the txins for the raw transaction to appropriate transaction hash(es) per the commented info.

If you’d like to change to mainnet simply uncomment the call in init() for the NetworkParameters initialization to use mainnet.

All of these interactions are done via bitcoinj and the peer-to-peer protocol. If you would rather utilize the RPC (mainnet port is *8566*, testnet is *18566*), you can do that as well as paicoind exposes the exact same set of RPC commands as bitcoind. So, you can do:

    createrawtransaction
    signrawtransaction
    sendrawtransaction
    estimatefee
    etc.

## Verify Address

This is a script that verifies the validity of a PAIcoin public address.

# Usage

To run the script, execute:

    mvn exec:java -Dexec.mainClass=com.oben.PAIVerifyAddress

The address will be input explicitly while running the script.

The result of the address analysis will reveal if the address is a valid PAIcoin address or not.

If you’d like to change to mainnet simply uncomment the call in the constructor for the NetworkParameters initialization to use testnet.