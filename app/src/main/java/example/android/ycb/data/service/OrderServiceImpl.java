package example.android.ycb.data.service;

import example.android.ycb.biz.service.OrderService;
import example.android.ycb.biz.model.Rate;
import example.android.ycb.biz.model.Rate.Result;
import example.android.ycb.data.source.remote.common.ResponseData;
import example.android.ycb.data.source.remote.order.OrderDataSource;
import io.reactivex.Single;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderDataSource orderDataSource;

  @Override
  public Single<Result> publishOrderRate(String orderId, Rate rate) {
    return orderDataSource.publishOrderRate(orderId, rate.getRateStar(), rate.getContent())
        .map(responseDataResponseData -> {
          if (responseDataResponseData.isSuccess()) {
            return Result.builder().isSuccess(true).build();
          }
          if (responseDataResponseData.getCode()
              == ResponseData.RESP_CODE_CONTENT_LENGTH_OVER_LIMIT) {
            return Result.builder().isContentLengthOverLimitError(true).build();
          }
          if (responseDataResponseData.getCode()
              == ResponseData.RESP_CODE_CONTENT_INVALID) {
            return Result.builder().isContentInvalidError(true).build();
          }

          // Generic failure.
          return Result.builder().isSuccess(false).build();
        });
  }
}
