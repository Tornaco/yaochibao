package example.android.ycb.biz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Rate {

  private final int rateStar;
  private final String content;

  @Getter
  @Builder
  public static class Result {

    private final boolean isSuccess;
    private final boolean isServerNotAvailableError;
    private final boolean isContentInvalidError;
    private final boolean isContentLengthOverLimitError;
  }
}
