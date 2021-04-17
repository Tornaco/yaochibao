package example.android.plugin.dc.internal;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import example.android.plugin.dc.annotation.DataConverter;
import example.android.plugin.dc.internal.common.Logger;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

@SupportedAnnotationTypes("example.android.plugin.dc.annotation.DataConverter")
public class DataConverterCompiler extends AbstractProcessor {

  private ErrorReporter mErrorReporter;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnvironment) {
    super.init(processingEnvironment);
    mErrorReporter = new ErrorReporter(processingEnvironment);
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    Collection<? extends Element> annotatedElements =
        roundEnvironment.getElementsAnnotatedWith(DataConverter.class);

    List<TypeElement> types = new ImmutableList.Builder<TypeElement>()
        .addAll(ElementFilter.typesIn(annotatedElements))
        .build();

    types.forEach(this::processType);

    return true;
  }

  private void processType(TypeElement type) {
    DataConverter annotation = type.getAnnotation(DataConverter.class);
    if (annotation == null) {
      mErrorReporter.abortWithError("@ConvertTo annotation is null on Type %s", type);
      return;
    }
    if (type.getKind() != ElementKind.CLASS) {
      mErrorReporter.abortWithError("@ConvertTo" + " only applies to class", type);
    }

    NestingKind nestingKind = type.getNestingKind();
    if (nestingKind != NestingKind.TOP_LEVEL) {
      mErrorReporter
          .abortWithError("@ConvertTo" + " only applies to top level class", type);
    }

    checkModifiersIfNested(type);

    // get the fully-qualified class name
    if (annotation.converterClassNameSuffix().length() == 0) {
      mErrorReporter.abortWithError("converterClassNameSuffix should not be empty", type);
    }
    String fqClassName = generatedSubclassName(type, 0, annotation.converterClassNameSuffix());
    // class name
    String className = CompilerUtil.simpleNameOf(fqClassName);

    // Create source.
    String source = generateClass(type, className, getToClazz(annotation));
    SourceFiles.writeSourceFile(processingEnv, fqClassName, source, type);
  }

  private static TypeMirror getToClazz(DataConverter annotation) {
    try {
      annotation.value();
    } catch (MirroredTypeException mte) {
      return mte.getTypeMirror();
    }
    return null;
  }

  private String generatedSubclassName(TypeElement type, int depth, String subFix) {
    return generatedClassName(type, null, Strings.repeat("$", depth) + subFix);
  }

  private String generatedClassName(TypeElement type, String prefix, String subFix) {
    String name = type.getSimpleName().toString();
    while (type.getEnclosingElement() instanceof TypeElement) {
      type = (TypeElement) type.getEnclosingElement();
      name = type.getSimpleName() + "_" + name;
    }
    String pkg = CompilerUtil.packageNameOf(type);
    String dot = Strings.isNullOrEmpty(pkg) ? "" : ".";
    String prefixChecked = Strings.isNullOrEmpty(prefix) ? "" : prefix;
    String subFixChecked = Strings.isNullOrEmpty(subFix) ? "" : subFix;
    return pkg + dot + prefixChecked + name + subFixChecked;
  }


  private String generateClass(TypeElement type, String className, TypeMirror toClazz) {
    if (type == null) {
      mErrorReporter.abortWithError("generateClass was invoked with null type", null);
      return null;
    }
    if (className == null) {
      mErrorReporter.abortWithError("generateClass was invoked with null class name", type);
      return null;
    }
    String pkg = CompilerUtil.packageNameOf(type);

    TypeSpec.Builder subClass = TypeSpec.classBuilder(className)
        .addModifiers(FINAL, PUBLIC)
        .addMethod(createMethodSpec(type, toClazz));

    JavaFile javaFile = JavaFile.builder(pkg, subClass.build())
        .addFileComment("Auto Generated.")
        .skipJavaLangImports(true)
        .build();
    return javaFile.toString();
  }

  public static String captureFirstLetter(String src) {
    char[] cs = src.toCharArray();
    cs[0] -= 32;
    return String.valueOf(cs);
  }

  private MethodSpec createMethodSpec(TypeElement typeElement, TypeMirror toClazz) {
    StringBuilder setupCode = new StringBuilder();

    // Retrieve all setXXX() methods.
    typeElement.getEnclosedElements().forEach((Consumer<Element>) element -> {
      if (element.getKind() == ElementKind.FIELD) {
        Logger.debug("Found field: " + element.getSimpleName());
        if (element.getAnnotation(DataConverter.Ignore.class) == null) {
          Logger.debug("Found field: " + element.getSimpleName());
          setupCode.append("res.set" + captureFirstLetter(element.getSimpleName().toString())
              + "("
              + "data.get"
              + (captureFirstLetter(element.getSimpleName().toString()))
              + "()"
              + ");");
          setupCode.append(System.lineSeparator());
        } else {
          Logger.debug("Ignore field: " + element.getSimpleName());
        }
      }
    });

    return MethodSpec.methodBuilder("to" + CompilerUtil.simpleNameOf(toClazz.toString()))
        .addModifiers(PUBLIC, FINAL, STATIC)
        .addParameter(ParameterSpec.builder(ClassName.get(typeElement), "data").build())
        .returns(ClassName.get(toClazz))
        .addCode(
            String.format("%s res = new %s();", toClazz.toString(), toClazz.toString()) + System
                .lineSeparator())
        .addCode(setupCode.toString())
        .addCode("return res;" + System.lineSeparator())
        .build();
  }

  private void checkModifiersIfNested(TypeElement type) {
    ElementKind enclosingKind = type.getEnclosingElement().getKind();
    if (enclosingKind.isClass() || enclosingKind.isInterface()) {
      if (type.getModifiers().contains(PRIVATE)) {
        mErrorReporter.abortWithError("@ConvertTo class must not be private", type);
      }
      if (!type.getModifiers().contains(STATIC)) {
        mErrorReporter.abortWithError("Nested @ConvertTo class must be static", type);
      }
    }
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }
}

