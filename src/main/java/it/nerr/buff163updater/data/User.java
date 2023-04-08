package it.nerr.buff163updater.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record User(String avatar, String avatar_safe, boolean is_premium_vip, String nickname, int seller_level,
                   String shop_id, String user_id, List<String> v_types) {}
