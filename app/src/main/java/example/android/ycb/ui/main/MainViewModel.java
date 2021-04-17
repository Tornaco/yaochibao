package example.android.ycb.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import example.android.ycb.biz.GoodsService;
import example.android.ycb.biz.YcbContext;
import example.android.ycb.biz.model.Goods;
import example.android.ycb.biz.model.Goods.FilterOptions;
import io.reactivex.schedulers.Schedulers;
import java.util.EnumSet;
import java.util.List;
import lombok.CustomLog;
import lombok.Getter;

@CustomLog
public class MainViewModel extends ViewModel {

  @Getter
  private final MutableLiveData<List<Goods>> goodsListData = new MutableLiveData<>();
  private final GoodsService goodsService;

  public MainViewModel(YcbContext context) {
    this.goodsService = context.getGoodsService();
  }

  private void requestGoodsList() {
    log.debug("getGoodsList: %s", goodsService);
    goodsService.getGoodsList(EnumSet.of(FilterOptions.Distance))
        .subscribeOn(Schedulers.io())
        .subscribe(goodsListData::postValue,
            throwable -> log.error("getGoodsList err: %s", throwable.getMessage()));
  }

  public void start() {
    requestGoodsList();
  }
}