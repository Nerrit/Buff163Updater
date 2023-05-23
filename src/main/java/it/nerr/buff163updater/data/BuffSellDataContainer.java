package it.nerr.buff163updater.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.nerr.database.buff163.BuffSellData;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BuffSellDataContainer(String code, BuffSellData data, String msg) {

}
