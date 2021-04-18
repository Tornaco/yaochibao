package example.android.ycb.biz.service;

import example.android.ycb.biz.model.Food;
import example.android.ycb.biz.model.Food.FilterOptions;
import io.reactivex.Single;
import java.util.EnumSet;
import java.util.List;

public interface FoodService {

  Single<List<Food>> getFoodList(EnumSet<FilterOptions> filterOptions);
}
