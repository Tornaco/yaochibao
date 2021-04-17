package example.android.ycb.biz;

import example.android.ycb.biz.model.Goods;
import example.android.ycb.biz.model.Goods.FilterOptions;
import io.reactivex.Single;
import java.util.EnumSet;
import java.util.List;

public interface GoodsService {

  Single<List<Goods>> getGoodsList(EnumSet<FilterOptions> filterOptions);
}
