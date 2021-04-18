package example.android.ycb.data.service;

import example.android.ycb.biz.service.FoodService;
import example.android.ycb.biz.model.Food;
import example.android.ycb.biz.model.Food.FilterOptions;
import example.android.ycb.data.source.remote.goods.GoodsDataConverter;
import example.android.ycb.data.source.remote.goods.GoodsDataSource;
import io.reactivex.Single;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FoodServiceImpl implements FoodService {

  private final GoodsDataSource goodsDataSource;

  @Override
  public Single<List<Food>> getFoodList(EnumSet<FilterOptions> filterOptions) {
    return goodsDataSource.getGoods(filterOptions.stream().map(Enum::name).toArray(String[]::new))
        .map(listResp -> {
          listResp.ensureSuccessOrThrow();
          return listResp.getData()
              .stream()
              .map(GoodsDataConverter::toFood)
              .collect(Collectors.toList());
        });
  }
}
