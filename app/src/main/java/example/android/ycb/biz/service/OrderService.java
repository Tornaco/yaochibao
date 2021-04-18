package example.android.ycb.biz.service;

import example.android.ycb.biz.model.Rate;
import io.reactivex.Single;

public interface OrderService {

  Single<Rate.Result> publishOrderRate(String orderId, Rate rate);
}
