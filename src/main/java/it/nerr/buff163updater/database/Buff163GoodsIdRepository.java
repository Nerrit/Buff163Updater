package it.nerr.buff163updater.database;

import it.nerr.buff163updater.data.Buff163GoodsIdRecord;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface Buff163GoodsIdRepository extends ReactiveCrudRepository<Buff163GoodsIdRecord, Integer> {

    @Query("SELECT goods_id FROM public.buff163_goods_ids WHERE hash_name = :hashName")
    Flux<Integer> findGoodsIdByHashName(String hashName);

    @Query("SELECT goods_id FROM public.buff163_goods_ids")
    Flux<Integer> findAllGoodsIds();
}
