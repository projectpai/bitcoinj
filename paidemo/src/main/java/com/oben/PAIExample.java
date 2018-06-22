package com.oben;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.Iterator;
import javax.xml.bind.DatatypeConverter;

import com.google.common.base.Throwables;
import com.subgraph.orchid.encoders.Hex;
import org.bitcoinj.core.*;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptChunk;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class PAIExample {
	private NetworkParameters params = null;
	private ECKey privateKey = null;
	private PeerGroup peerGroup = null;
	private BlockChain blockChain = null;
	private BlockStore blockStore = null;

	public void init(String privateKeyString)
	throws Exception {
		this.params = TestNet3Params.get();
		//this.params = MainNetParams.get();
		//this.params = RegTestParams.get();
		DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(params, privateKeyString);
		this.privateKey = dumpedPrivateKey.getKey();
		this.blockStore = new MemoryBlockStore(params);
		this.blockChain = new BlockChain(params, blockStore);
		this.peerGroup = new PeerGroup(this.params, blockChain);
		this.peerGroup.addPeerDiscovery(new DnsDiscovery(params));
		this.peerGroup.start();
	}

	public void shutdown() {
		peerGroup.stop();
	}

	public void rawTx(String toAddress, String amount) {
		//Private key should be provided in base58 format
		Address to = Address.fromBase58(params, toAddress);
		Transaction tx = new Transaction(params);
		tx.addOutput(Coin.parseCoin(amount), to);

		//Add inputs manually here...
		//this.blockStore.get("YOURTXHASH HERE");
		//tx.addInput(new TransactionInput())

		//Set components of the transaction
		tx.getConfidence().setSource(TransactionConfidence.Source.SELF);
		tx.setPurpose(Transaction.Purpose.USER_PAYMENT);

		Sha256Hash hash = Sha256Hash.wrap(tx.getHashAsString());
		//Sign the transaction
		ECDSASignature sig = this.privateKey.sign(hash);
		byte[] encodedBytes = sig.encodeToDER();
		String signedTransaction = DatatypeConverter.printHexBinary(encodedBytes);
		System.out.println("Raw transaction created: " + tx.getHashAsString());
		System.out.println("Signed transaction: " + signedTransaction);
		TransactionBroadcast broadcast = this.peerGroup.broadcastTransaction(tx);
		System.out.println("Broadcasted hash: " + tx.getHashAsString());
		ListenableFuture<Transaction> future = broadcast.future();

		try {
			future.get();
		} catch (Exception e) {
			System.out.println("Failed while waiting for broadcast with message: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void simpleTx(String toAddress, String amount) {
		Wallet wallet = new Wallet(params);
		wallet.importKey(this.privateKey);
		this.blockChain.addWallet(wallet);
		this.peerGroup.addWallet(wallet);
		this.peerGroup.downloadBlockChain();
		Address to = Address.fromBase58(params, toAddress);
		SendRequest request = SendRequest.to(to, Coin.parseCoin(amount));
		try {
			Coin feePerKb = request.feePerKb;
			System.out.println("Current balance: " + wallet.getBalance() + " and fee per kb: " + feePerKb);
			wallet.sendCoins(request);
		}
		catch(Exception e) {
			System.out.println("Failed while sending funds with message: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void feeExample()
	{

	}
	public void walletAddresses() {
		Wallet wallet = new Wallet(params);
		wallet.importKey(this.privateKey);
		wallet.importKey(this.privateKey);
		for (int x = 0; x < 10; x++) {
			System.out.println("Address: " + wallet.freshReceiveAddress().toString());
		}
		wallet.removeKey(this.privateKey);
	}

	public void getPubKeyScript(String addressString)
	throws Exception {
		Address address = Address.fromBase58(this.params, addressString);
		Script outputScript = ScriptBuilder.createOutputScript(address);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputScript.getChunks().forEach(chunk -> {
			try {
				chunk.write(outputStream);
			} catch(Exception e) {
				throw Throwables.propagate(e);
			}
		});

		System.out.println("Script: " + new String(Hex.encode(outputStream.toByteArray())));
	}

	public static void main(String[] args) throws Exception {
		PAIExample example = new PAIExample();
		//Substitute a key that is linked to an address that has funds or an HD key
		example.init("aUFnBx22DoSao9NzqV8hB5UtRnFHdXTLP9r8wPuF6Utz2H8syqWr");
		example.getPubKeyScript("Mutrsk37EpPbn2Qp2esJEKSsYag2eK7P3B");
		example.walletAddresses();
		example.rawTx("Mutrsk37EpPbn2Qp2esJEKSsYag2eK7P3B", ".01");
		example.simpleTx("Mutrsk37EpPbn2Qp2esJEKSsYag2eK7P3B", ".015");
		example.shutdown();
	}
}

