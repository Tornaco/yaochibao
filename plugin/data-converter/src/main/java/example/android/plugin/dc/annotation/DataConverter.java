package example.android.plugin.dc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface DataConverter {

  Class<?> value();

  String converterClassNameSuffix() default "Converter";


  @Target(ElementType.FIELD)
  @Retention(RetentionPolicy.SOURCE)
  @interface Ignore {

  }

}
