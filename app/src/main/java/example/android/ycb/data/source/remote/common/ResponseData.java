package example.android.ycb.data.source.remote.common;

import androidx.annotation.Keep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Keep
public class ResponseData<T> {

  public static final int RESP_CODE_SUCCESS = 0;
  public static final int RESP_CODE_CONTENT_INVALID = 1010;
  public static final int RESP_CODE_CONTENT_LENGTH_OVER_LIMIT = 1011;

  private T data;
  private int code;
  private String message;

  public boolean isSuccess() {
    return code == 0;
  }

  public void ensureSuccessOrThrow() {
    if (!isSuccess()) {
      throw new SeverErrorException(code, message);
    }
  }
}
