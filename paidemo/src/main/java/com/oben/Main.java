package com.oben;

import java.io.File;
import java.io.IOException;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.BalanceType;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.ShellFactory;

public class Main {

	@Command(description = "Send some amount of coins to provided address")
	public void send(@Param(name = "address") String toAddress, @Param(name = "amount") String amount) {
		
		NetworkParameters params = TestNet3Params.get();
		WalletAppKit kit = new WalletAppKit(params, new File("."), "sendrequest-example");
		
		kit.startAsync();
		kit.awaitRunning();
		
		Coin value = Coin.parseCoin(amount);
		Address to = Address.fromBase58(params, toAddress);
		
		try {
			Wallet.SendResult result = kit.wallet().sendCoins(kit.peerGroup(), to, value);
			
			System.out.println("coins sent. transaction hash: " + result.tx.getHashAsString());
			System.out.println("transaction fee: " + result.tx.getFee());
		} catch (InsufficientMoneyException e) {
			System.out.println("Not enough coins in your wallet. Missing " + e.missing.getValue()
					+ " satoshis are missing (including fees)");
			System.out.println("Send money to: " + kit.wallet().currentReceiveAddress().toString());

			ListenableFuture<Coin> balanceFuture = kit.wallet().getBalanceFuture(value, BalanceType.AVAILABLE);
			FutureCallback<Coin> callback = new FutureCallback<Coin>() {
				@Override
				public void onSuccess(Coin balance) {
					System.out.println("coins arrived and the wallet now has enough balance");
				}

				@Override
				public void onFailure(Throwable t) {
					System.out.println("something went wrong");
				}
			};
			Futures.addCallback(balanceFuture, callback);
		}
	}

	public static void main(String[] args) throws IOException {
		ShellFactory.createConsoleShell("pai_demo", "Pai Demo", new Main()).commandLoop();
	}

}
