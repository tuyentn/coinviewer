package com.binance.api.examples;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import formula.standardDeviation;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import test.gui;

/**
 * Illustrates how to use the klines/candlesticks event stream to create a local cache of bids/asks for a symbol.
 */
public class CandlesticksCacheExample {

  /**
   * Key is the start/open time of the candle, and the value contains candlestick date.
   */
  private Map<Long, Candlestick> candlesticksCache;
  public  LinkedList<Candlestick> queue = new LinkedList<Candlestick>();
  
  public double SD;
  //double[] arrClose;
  public LinkedList<Double> arrClose = new LinkedList<Double>();
  Double[] arrTemp;
          
  public CandlesticksCacheExample(String symbol, CandlestickInterval interval) {
    initializeCandlestickCache(symbol, interval);
    startCandlestickEventStreaming(symbol, interval);
  }

  /**
   * Initializes the candlestick cache by using the REST API.
   */
  private void initializeCandlestickCache(String symbol, CandlestickInterval interval) {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    BinanceApiRestClient client = factory.newRestClient();
    List<Candlestick> candlestickBars = client.getCandlestickBars(symbol.toUpperCase(), interval);
    //arrClose = new double[20];
    this.candlesticksCache = new TreeMap<>();
    for (Candlestick candlestickBar : candlestickBars) {
      candlestickBar.setSymbol(symbol);
      candlesticksCache.put(candlestickBar.getOpenTime(), candlestickBar);
      //System.out.println(candlestickBar.getOpenTime());
    }
    for (int i = 20; i >= 1 ; i--){
        queue.addLast(candlestickBars.get(candlestickBars.size()-i));
        //arrClose.add(Double.valueOf(candlestickBars.get(499-i).getClose()));
        arrClose.addLast(Double.valueOf(candlestickBars.get(candlestickBars.size()-i).getClose()));
        //System.out.println(candlestickBars.get(499-i).getClose());
    }
    arrTemp = arrClose.toArray(new Double[arrClose.size()]);
    SD = standardDeviation.calculateSD(arrTemp);
    //System.out.println(queue.getFirst().getSymbol() +  ":"+SD/Double.valueOf(queue.getFirst().getClose())*200);
    queue.getFirst().setSD(SD);
  }

  /**
   * Begins streaming of depth events.
   */
  private void startCandlestickEventStreaming(String symbol, CandlestickInterval interval) {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    BinanceApiWebSocketClient client = factory.newWebSocketClient();
    
    client.onCandlestickEvent(symbol.toLowerCase(), interval, (CandlestickEvent response) -> {
      Long openTime = response.getOpenTime();
      Candlestick updateCandlestick = candlesticksCache.get(openTime);
//      if (updateCandlestick == null) {
//        // new candlestick
//        updateCandlestick = new Candlestick();
//      }
        //System.out.println(queue.getLast().getOpenTime() - openTime);
        if (queue.getFirst().getOpenTime() < openTime){
            updateCandlestick = new Candlestick();
            updateCandlestick.setOpenTime(response.getOpenTime());
            updateCandlestick.setOpen(response.getOpen());
            updateCandlestick.setLow(response.getLow());
            updateCandlestick.setHigh(response.getHigh());
            updateCandlestick.setClose(response.getClose());
            updateCandlestick.setCloseTime(response.getCloseTime());
            updateCandlestick.setVolume(response.getVolume());
            updateCandlestick.setNumberOfTrades(response.getNumberOfTrades());
            updateCandlestick.setQuoteAssetVolume(response.getQuoteAssetVolume());
            updateCandlestick.setTakerBuyQuoteAssetVolume(response.getTakerBuyQuoteAssetVolume());
            updateCandlestick.setTakerBuyBaseAssetVolume(response.getTakerBuyQuoteAssetVolume());
            updateCandlestick.setSymbol(symbol);
            
            arrClose.addFirst(Double.valueOf(updateCandlestick.getClose()));
            arrTemp = arrClose.toArray(new Double[arrClose.size()]);
            SD = standardDeviation.calculateSD(arrTemp);
            updateCandlestick.setSD(SD);
            
            if(queue.size()> 19) {
              queue.pollLast();
              arrClose.pollLast();
          }
          
          
          //System.out.println();
          
//          for(Candlestick uk: queue)
//            {
//                System.out.println(uk);
//            }
            //System.out.println(queue.getLast());
          
            queue.addFirst(updateCandlestick);
            //System.out.println("SD = " + SD);
            //System.out.println("first SD = "+queue.getFirst().getSD());
            //System.out.println("0 SD = "+queue.get(0).getSD());
            //System.out.println(queue.getFirst().getSymbol() +  ":"+queue.getFirst().getSD()/Double.valueOf(queue.getFirst().getClose())*200);
        }
      // update candlestick with the stream data
      
      // Store the updated candlestick in the cache
      //candlesticksCache.put(openTime, updateCandlestick);
      // System.out.println(candlesticksCache.size());
     
   
    });
  }

  /**
   * @return a klines/candlestick cache, containing the open/start time of the candlestick as the key,
   * and the candlestick data as the value.
   */
  public Map<Long, Candlestick> getCandlesticksCache() {
    return candlesticksCache;
  }

  public static void main(String[] args) {
    new CandlesticksCacheExample("ETHBTC", CandlestickInterval.ONE_MINUTE);
  }
}
