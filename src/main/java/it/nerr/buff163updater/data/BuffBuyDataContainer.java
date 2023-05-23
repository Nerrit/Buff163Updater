package it.nerr.buff163updater.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.nerr.database.buff163.BuffBuyData;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BuffBuyDataContainer(String code, BuffBuyData data, String msg) {

}
