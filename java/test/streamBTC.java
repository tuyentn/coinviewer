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
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.examples.CandlesticksCacheExample;
import formula.standardDeviation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
/**
 *
 * @author 8570w
 */

public class streamBTC {
    public BinanceApiClientFactory factory;
    public BinanceApiRestClient client;
    public String temp;
    public List<String> allSymbols;
    public List<TickerPrice> listz;
    public ArrayList<LinkedList<Candlestick>> candleStreams;
    public CandlesticksCacheExample tempCandle;
    
    public streamBTC() {
        allSymbols = new ArrayList();
        factory = BinanceApiClientFactory.newInstance("API-KEY", "SECRET");
        client = factory.newRestClient();
        candleStreams = new ArrayList<LinkedList<Candlestick>>();
        System.out.println("StreamBTC init");
        //Filter here
        listz = client.getAllPrices();
        //allSymbols.add("ETHBTC");
        for (int i =0; i < listz.size(); i++){
            temp = listz.get(i).getSymbol();
            if(temp.substring(temp.length()-3, temp.length()).compareTo("BTC") == 0 ){
                allSymbols.add(temp);
                //System.out.println(temp);
            }
        }
    }
    
    public void restart(String interval, int numSticks, double rate) {
        candleStreams.clear();
        CandlestickInterval intev = CandlestickInterval.ONE_MINUTE;
        if (interval.equals("3m")) {
            intev = CandlestickInterval.THREE_MINUTES;
        } else if (interval.equals("5m")) {
            intev = CandlestickInterval.FIVE_MINUTES;
        } else if (interval.equals("15m")) {
            intev = CandlestickInterval.FIFTEEN_MINUTES;
        } else if (interval.equals("30m")) {
            intev = CandlestickInterval.HALF_HOURLY;
        } else if (interval.equals("1H")) {
            intev = CandlestickInterval.HOURLY;
        } else if (interval.equals("2H")) {
            intev = CandlestickInterval.TWO_HOURLY;
        } else if (interval.equals("4H")) {
            intev = CandlestickInterval.FOUR_HOURLY;
        } else if (interval.equals("1D")) {
            intev = CandlestickInterval.DAILY;
        } else {
            intev = CandlestickInterval.ONE_MINUTE;
        }
        System.out.println("Stream restart: interval = "+interval+", sticks >= "+numSticks+", BB < "+rate);
        for (int i = 0; i < allSymbols.size(); i++){
        	
            tempCandle = new CandlesticksCacheExample(allSymbols.get(i), intev);
            int j = 0;
            //System.out.println("Size = " + tempCandle.queue.size());
            for ( j=0; j<numSticks; j++) {
                //System.out.println(tempCandle.queue.getFirst());
                if (tempCandle.queue.get(j).getSD()/ standardDeviation.SMA20(tempCandle.arrClose.toArray(new Double[tempCandle.arrClose.size()])) *200 > rate) {
                    break;
                }
            }
            //System.out.println("j = " + j);
            if (j==numSticks) {
            	System.out.println(allSymbols.get(i));
                candleStreams.add(tempCandle.queue);
            }
        }
        LinkedList<Candlestick> queueStickTemp = new LinkedList<Candlestick>();
        for (int i=0; i < candleStreams.size(); i ++) {
        	for (int j = i; j < candleStreams.size() ; j++) {
        		if (candleStreams.get(i).getFirst().getSD()/Double.valueOf(candleStreams.get(i).getFirst().getClose()).doubleValue() 
        				> candleStreams.get(j).getFirst().getSD()/Double.valueOf(candleStreams.get(j).getFirst().getClose()).doubleValue()) {
        			queueStickTemp = candleStreams.get(i);
        			candleStreams.set(i, candleStreams.get(j));
        			candleStreams.set(j, queueStickTemp);
        		}
        	}
        }
    }    
}


