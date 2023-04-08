package it.nerr.buff163updater.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Coupon(String cdkey_id, String cdkey_text, String coupon_type) {}
