package com.oben;

import java.util.Scanner;
import java.util.Scanner;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
//import org.bitcoinj.params.TestNet3Params;
//import org.bitcoinj.params.RegTestParams;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.WrongNetworkException;

public class PAIVerifyAddress {
    public class Result {
        public boolean status;
        public String message;
        
        public Result(boolean status, String message) {
            this.status = status;
            this.message = message;            
        }
    }
    
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    
    private NetworkParameters params = null;
    
    public PAIVerifyAddress() {
        this.params = MainNetParams.get();
        //this.params = TestNet3Params.get();
	//this.params = RegTestParams.get();
    }
    
    public Result verifyAddress(String base58Address) {
        // try creating the address from the string and treat exception
        Address address = null;
        try {
            address = Address.fromBase58(params, base58Address);
        }
        catch (WrongNetworkException ex) {
            return new Result(false, "The network prefix is invalid.");
        }
        catch (AddressFormatException ex) {
            return new Result(false, "The Base58 representation does not parse or the checksum is invalid.");
        }
        
        if (address.getVersion() == params.getAddressHeader()) {
            return new Result(true, "This is a P2PKH address.");
        }
        else if (address.getVersion() == params.getP2SHHeader()) {
            return new Result(true, "This is a P2SH address.");
        }
        
        return new Result(false, "Wrong address.");
    }
    
    public static void main(String[] args) throws Exception {
        PAIVerifyAddress va = new PAIVerifyAddress();
        
        // read the address
        System.out.println("Address: ");
        Scanner in = new Scanner(System.in);
        String base58Address = in.nextLine();
        
        Result result = va.verifyAddress(base58Address);
        if (result.status) {
            System.out.println(GREEN + "The address is a valid." + RESET + " " + result.message);
        } else {
            System.out.println(RED + "The address is not valid." + RESET + " " + result.message);
        }
    }
}
