package com.binance.api.examples;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.AllMarketTickersEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

/**
 * All market tickers channel examples.
 *
 * It illustrates how to create a stream to obtain all market tickers.
 */
public class AllMarketTickersExample {

  public static void main(String[] args) {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    BinanceApiWebSocketClient client = factory.newWebSocketClient();
    List<AllMarketTickersEvent> listBTC =  new ArrayList<AllMarketTickersEvent>();
    client.onAllMarketTickersEvent(event -> {
        //System.out.println(event);
        for (int i = 0; i < event.size(); i++) {
            String temp = event.get(i).getSymbol();
            if(temp.substring(temp.length()-3, temp.length()).compareTo("BTC") == 0 ){
                System.out.println(event.get(i));
            }
        }
        
        //System.out.println(event.get(0).getSymbol());
     });
     client.close();  
      
  }
}
