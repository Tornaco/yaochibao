package example.android.ycb.data.source.remote.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SeverErrorException extends RuntimeException {

  private final int code;
  private final String message;
}
