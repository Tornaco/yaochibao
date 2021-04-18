package example.android.ycb;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import example.android.ycb.biz.YcbContext;
import example.android.ycb.biz.service.FoodService;
import example.android.ycb.biz.service.OrderService;
import example.android.ycb.data.service.FoodServiceImpl;
import example.android.ycb.data.service.OrderServiceImpl;
import example.android.ycb.data.source.remote.goods.GoodsDataSource;
import example.android.ycb.data.source.remote.order.OrderDataSource;
import example.android.ycb.infra.util.NetworkStateManager;
import example.android.ycb.ui.main.MainViewModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YcbApplication extends Application implements ViewModelProvider.Factory {

  @Getter
  private YcbContext ycbContext;

  @Override
  public void onCreate() {
    super.onCreate();
    this.ycbContext = new YcbContextImpl(this.getApplicationContext());
  }

  @SuppressWarnings("unchecked")
  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass == MainViewModel.class) {
      return (T) new MainViewModel(getYcbContext());
    }
    return null;
  }

  @RequiredArgsConstructor
  private static class YcbContextImpl implements YcbContext {

    private final Context context;

    private GoodsDataSource buildGoodsDataSource() {
      return new Retrofit.Builder()
          .baseUrl("https://www.example.com/mobile/")
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .build()
          .create(GoodsDataSource.class);
    }

    private OrderDataSource buildOrderDataSource() {
      return new Retrofit.Builder()
          .baseUrl("https://www.example.com/mobile/")
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .build()
          .create(OrderDataSource.class);
    }

    @Override
    public Context getAndroidContext() {
      return context;
    }

    @Override
    public NetworkStateManager getNetworkStateManager() {
      return new NetworkStateManager();
    }

    @Override
    public FoodService getGoodsService() {
      return new FoodServiceImpl(buildGoodsDataSource());
    }

    @Override
    public OrderService getOrderService() {
      return new OrderServiceImpl(buildOrderDataSource());
    }
  }
}
