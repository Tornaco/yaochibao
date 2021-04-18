package example.android.ycb.data.source.remote.goods;

import com.google.gson.annotations.SerializedName;
import example.android.plugin.dc.annotation.DataConverter;
import example.android.ycb.biz.model.Food;
import lombok.Data;

@Data
@DataConverter(Food.class)
public class GoodsData {

  private float price;
  @SerializedName("img")
  private String imgUrl;
  @SerializedName("publishAt")
  private long publishTimeMills;
}
