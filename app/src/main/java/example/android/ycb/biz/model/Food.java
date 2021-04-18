package example.android.ycb.biz.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Food {

  private float price;
  private String imgUrl;
  private long publishTimeMills;

  public enum FilterOptions {
    Distance,
    Price
  }
}
