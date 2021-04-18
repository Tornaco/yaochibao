package example.android.ycb.biz.presenter;

import android.accounts.NetworkErrorException;
import android.content.Context;
import example.android.ycb.biz.model.Rate;
import example.android.ycb.biz.service.OrderService;
import example.android.ycb.infra.util.NetworkStateManager;
import io.reactivex.Single;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderPresenter {

  private final Context context;
  private final OrderService orderService;
  private final NetworkStateManager networkStateManager;

  public Single<Rate.Result> publishRateForOrder(String orderId, Rate rate) {
    if (!networkStateManager.isNetworkAvailable(context)) {
      return Single.error(new NetworkErrorException());
    }
    return orderService.publishOrderRate(orderId, rate);
  }
}
