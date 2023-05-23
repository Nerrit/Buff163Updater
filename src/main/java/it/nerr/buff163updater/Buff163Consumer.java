package it.nerr.buff163updater;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import it.nerr.buff163updater.data.BuffBuyDataContainer;
import it.nerr.buff163updater.data.BuffSellDataContainer;
import it.nerr.database.buff163.BuffBuyData;
import it.nerr.database.buff163.BuffSellData;
import it.nerr.database.repositories.Buff163BuyOrdersRepository;
import it.nerr.database.repositories.Buff163GoodsIdRepository;
import it.nerr.database.repositories.Buff163SellOrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
public class Buff163Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Buff163Consumer.class);

    /**
     * Execution rate in ms
     */
    private static final long RATE = 1000L * 60L * 60L * 6L;
    private static final String BUY_ORDERS_ENDPOINT = "https://buff.163.com/api/market/goods/buy_order?game=csgo" +
            "&goods_id={goodsId}&page_num=1&page_size=100";
    private static final String SELL_ORDERS_ENDPOINT = "https://buff.163.com/api/market/goods/sell_order?game=csgo" +
            "&goods_id={goodsId}&page_num=1&page_size=100";
    private final WebClient webClient;
    private final Buff163BuyOrdersRepository buff163BuyOrdersRepository;
    private final Buff163SellOrdersRepository buff163SellOrdersRepository;
    private final RateLimiter buyOrderRateLimiter;
    private final RateLimiter sellOrderRateLimiter;
    private final List<Integer> goodsIds;

    public Buff163Consumer(Buff163GoodsIdRepository buff163GoodsIdRepository,
                           Buff163BuyOrdersRepository buff163BuyOrdersRepository,
                           Buff163SellOrdersRepository buff163SellOrdersRepository) {
        this.buyOrderRateLimiter = RateLimiter.of("buff163-buy-order",
                RateLimiterConfig.custom().limitForPeriod(1).limitRefreshPeriod(Duration.ofMillis(500))
                        .timeoutDuration(Duration.ofMillis(RATE)).build());
        this.sellOrderRateLimiter = RateLimiter.of("buff163-sell-order",
                RateLimiterConfig.custom().limitForPeriod(1).limitRefreshPeriod(Duration.ofSeconds(4))
                        .timeoutDuration(Duration.ofMillis(RATE)).build());
        this.buff163BuyOrdersRepository = buff163BuyOrdersRepository;
        this.buff163SellOrdersRepository = buff163SellOrdersRepository;
        this.goodsIds = buff163GoodsIdRepository.findAllGoodsIds().collectList().block();
        this.webClient = WebClient.create();
    }

    @Scheduled(fixedRate = RATE)
    public void consumeBuyOrders() {
        LOGGER.info("Consuming buy orders from buff.163.com");
        Flux.fromIterable(goodsIds).flatMap((Integer goodsId) -> getBuyData(goodsId).flatMap(
                data -> buff163BuyOrdersRepository.saveBuyData(goodsId, data)).log()).subscribe();
    }

    private Mono<BuffBuyData> getBuyData(Integer goodsId) {
        buyOrderRateLimiter.acquirePermission();
        return webClient.get().uri(BUY_ORDERS_ENDPOINT, goodsId).retrieve().bodyToMono(BuffBuyDataContainer.class)
                .map(BuffBuyDataContainer::data);
    }

    @Scheduled(fixedRate = RATE)
    public void consumeSellOrders() {
        LOGGER.info("Consuming sell orders from buff.163.com");
        Flux.fromIterable(goodsIds).flatMap((Integer goodsId) -> getSellData(goodsId).flatMap(
                data -> buff163SellOrdersRepository.saveSellData(goodsId, data)).log()).subscribe();
    }

    private Mono<BuffSellData> getSellData(Integer goodsId) {
        sellOrderRateLimiter.acquirePermission();
        return webClient.get().uri(SELL_ORDERS_ENDPOINT, goodsId).retrieve().bodyToMono(BuffSellDataContainer.class)
                .map(BuffSellDataContainer::data);
    }
}
