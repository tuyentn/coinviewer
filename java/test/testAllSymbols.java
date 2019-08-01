/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.AllMarketTickersEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.examples.CandlesticksCacheExample;
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
 *
 * @author 8570w
 */
public class testAllSymbols {
    public static void main(String[] args) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("API-KEY", "SECRET");
        BinanceApiRestClient client = factory.newRestClient();
        String temp;
        List<String> allSymbols = new ArrayList();
        List<TickerPrice> listz = client.getAllPrices();
        for (int i =0; i < listz.size(); i++){
            temp = listz.get(i).getSymbol();
            if(temp.substring(temp.length()-3, temp.length()).compareTo("BTC") == 0 ){
                allSymbols.add(temp);
                //System.out.println(temp);
            }           
        }
        
        for (int i = 0; i < allSymbols.size(); i++){
            new CandlesticksCacheExample(allSymbols.get(i), CandlestickInterval.THREE_MINUTES);
        }
    }
}
