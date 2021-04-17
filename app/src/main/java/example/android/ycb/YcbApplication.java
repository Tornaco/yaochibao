package example.android.ycb;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import example.android.ycb.biz.GoodsService;
import example.android.ycb.biz.YcbContext;
import example.android.ycb.data.service.GoodsServiceImpl;
import example.android.ycb.data.source.remote.goods.GoodsDataSource;
import example.android.ycb.ui.main.MainViewModel;
import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YcbApplication extends Application implements ViewModelProvider.Factory {

  @Getter
  private YcbContext ycbContext;

  @Override
  public void onCreate() {
    super.onCreate();
    this.ycbContext = new YcbContextImpl();
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

  private static class YcbContextImpl implements YcbContext {

    private GoodsDataSource buildGoodsDataSource() {
      return new Retrofit.Builder()
          .baseUrl("https://www.example.com/mobile/")
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .build()
          .create(GoodsDataSource.class);
    }

    @Override
    public GoodsService getGoodsService() {
      return new GoodsServiceImpl(buildGoodsDataSource());
    }
  }
}
