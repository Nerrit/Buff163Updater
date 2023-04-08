package it.nerr.buff163updater.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BuffBuyDataContainer(String code, BuffBuyData data, String msg) {

}
