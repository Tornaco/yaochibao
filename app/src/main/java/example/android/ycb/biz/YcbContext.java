package example.android.ycb.biz;

import android.content.Context;
import example.android.ycb.biz.service.FoodService;
import example.android.ycb.biz.service.OrderService;
import example.android.ycb.infra.util.NetworkStateManager;

public interface YcbContext {

  Context getAndroidContext();

  NetworkStateManager getNetworkStateManager();

  FoodService getGoodsService();

  OrderService getOrderService();
}
