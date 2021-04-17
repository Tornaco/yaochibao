package example.android.ycb.data.source.remote.goods;

import example.android.ycb.data.source.remote.common.ResponseData;
import io.reactivex.Single;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoodsDataSource {

  @GET("getGoods")
  Single<ResponseData<List<GoodsData>>> getGoods(@Query("filterOpts") String[] filterOpts);
}
