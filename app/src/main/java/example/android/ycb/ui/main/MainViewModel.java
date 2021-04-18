package example.android.ycb.ui.main;

import android.accounts.NetworkErrorException;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import example.android.ycb.biz.YcbContext;
import example.android.ycb.biz.model.Rate;
import example.android.ycb.biz.presenter.OrderPresenter;
import io.reactivex.schedulers.Schedulers;
import lombok.CustomLog;
import lombok.Getter;

@CustomLog
public class MainViewModel extends ViewModel {

  @Getter
  private final MutableLiveData<Rate.Result> rateResData = new MutableLiveData<>();
  @Getter
  private final MutableLiveData<Boolean> isNetworkErrorData = new MutableLiveData<>();
  private final OrderPresenter orderPresenter;

  public MainViewModel(YcbContext context) {
    this.orderPresenter = new OrderPresenter(
        context.getAndroidContext(),
        context.getOrderService(),
        context.getNetworkStateManager());
  }

  private void publishRateForOrder() {
    log.debug("publishRateForOrder: %s", orderPresenter);
    orderPresenter.publishRateForOrder("123", new Rate(3, "Nice~"))
        .subscribeOn(Schedulers.io())
        .subscribe(rateResData::postValue, throwable -> {
          if (throwable instanceof NetworkErrorException) {
            isNetworkErrorData.postValue(true);
          } else {
            log.error("publishRateForOrder: %s", throwable.getMessage());
          }
        });
  }

  public void start() {
    publishRateForOrder();
  }
}