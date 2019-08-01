package com.binance.api.client.domain.market;

import com.binance.api.client.constant.BinanceApiConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Kline/Candlestick bars for a symbol. Klines are uniquely identified by their open time.
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder()
@JsonIgnoreProperties(ignoreUnknown = true)
public class Candlestick {

  private Long openTime;

  private String open;

  private String high;

  private String low;

  private String close;

  private String volume;

  private Long closeTime;

  private String quoteAssetVolume;

  private Long numberOfTrades;

  private String takerBuyBaseAssetVolume;

  private String takerBuyQuoteAssetVolume;

  private String symbol;

  private double SD;

    public double getSD() {
        return SD;
    }

    public void setSD(double SD) {
        this.SD = SD;
    }
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
  
  public Long getOpenTime() {
    return openTime;
  }

  public void setOpenTime(Long openTime) {
    this.openTime = openTime;
  }

  public String getOpen() {
    return open;
  }

  public void setOpen(String open) {
    this.open = open;
  }

  public String getHigh() {
    return high;
  }

  public void setHigh(String high) {
    this.high = high;
  }

  public String getLow() {
    return low;
  }

  public void setLow(String low) {
    this.low = low;
  }

  public String getClose() {
    return close;
  }

  public void setClose(String close) {
    this.close = close;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public Long getCloseTime() {
    return closeTime;
  }

  public void setCloseTime(Long closeTime) {
    this.closeTime = closeTime;
  }

  public String getQuoteAssetVolume() {
    return quoteAssetVolume;
  }

  public void setQuoteAssetVolume(String quoteAssetVolume) {
    this.quoteAssetVolume = quoteAssetVolume;
  }

  public Long getNumberOfTrades() {
    return numberOfTrades;
  }

  public void setNumberOfTrades(Long numberOfTrades) {
    this.numberOfTrades = numberOfTrades;
  }

  public String getTakerBuyBaseAssetVolume() {
    return takerBuyBaseAssetVolume;
  }

  public void setTakerBuyBaseAssetVolume(String takerBuyBaseAssetVolume) {
    this.takerBuyBaseAssetVolume = takerBuyBaseAssetVolume;
  }

  public String getTakerBuyQuoteAssetVolume() {
    return takerBuyQuoteAssetVolume;
  }

  public void setTakerBuyQuoteAssetVolume(String takerBuyQuoteAssetVolume) {
    this.takerBuyQuoteAssetVolume = takerBuyQuoteAssetVolume;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
        .append("openTime", openTime)
        .append("open", open)
        .append("high", high)
        .append("low", low)
        .append("close", close)
        .append("volume", volume)
        .append("closeTime", closeTime)
        .append("quoteAssetVolume", quoteAssetVolume)
        .append("numberOfTrades", numberOfTrades)
        .append("takerBuyBaseAssetVolume", takerBuyBaseAssetVolume)
        .append("takerBuyQuoteAssetVolume", takerBuyQuoteAssetVolume)
        .append("symbol", symbol)
        .append("SD", SD)
        .toString();
  }
  
  public String[] toArray(){
      String[] temp = new String[4];
      temp[0] = this.symbol;
      temp[1] = this.close;
      temp[2] = this.volume;
      temp[3] = String.valueOf(SD / Double.valueOf(this.getClose())*200);
      return temp;
    }
}
