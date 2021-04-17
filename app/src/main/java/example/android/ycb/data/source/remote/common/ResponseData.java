package example.android.ycb.data.source.remote.common;

import androidx.annotation.Keep;
import lombok.Getter;

@Getter
@Keep
public class ResponseData<T> {

  private static final int RESP_CODE_SUCCESS = 0;

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
