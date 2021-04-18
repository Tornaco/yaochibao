package example.android.ycb.data.source.remote.order;

import example.android.ycb.data.source.remote.common.ResponseData;
import io.reactivex.Single;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderDataSource {

  @POST("/order/rate")
  Single<ResponseData<Void>> publishOrderRate(
      @Query("orderId") String oid,
      @Query("rateStar") int rateStar,
      @Query("content") String content);
}
