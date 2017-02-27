package com.route.fr;

import com.google.auto.service.AutoService;
import com.route.fr.annotation.ClassName;
import com.route.fr.annotation.Key;
import com.route.fr.annotation.RequestCode;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * <p>解析路由框架的注解,activity跳转的类名,定义的数据类型,activity跳转是否需要return</p>
 * <p>最后生成java文件，生成文件目录是：todo</p>
 * Created by hansj on 2017/2/21.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class RouteProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, List<MethodModel>> functions = new HashMap<>();
        parseElements(functions, roundEnv);
        if (functions.size() > 0) {
            for (Map.Entry<String, List<MethodModel>> entry : functions.entrySet()) {
                createJavaFile(entry.getKey(), entry.getValue());
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(Key.class);
        annotations.add(ClassName.class);
        return annotations;
    }

    private void parseElements(Map<String, List<MethodModel>> functions, RoundEnvironment roundEnv) {
        Set<? extends Element> elememts = roundEnv.getElementsAnnotatedWith(ClassName.class);
        for (Element e : elememts) {
            if (e.getKind() == ElementKind.METHOD) {
                String protolClass = e.getEnclosingElement().getSimpleName().toString();
                MethodModel methodModel = new MethodModel();
                methodModel.setClassName(e.getAnnotation(ClassName.class).value());
                methodModel.setMethodName(e.getSimpleName().toString());

                if (!functions.containsKey(protolClass)) {
                    List<MethodModel> methodModels = new ArrayList<>();
                    methodModels.add(methodModel);
                    functions.put(protolClass, methodModels);
                } else {
                    List<MethodModel> methodModels = functions.get(protolClass);
                    methodModels.add(methodModel);
                }
            }
        }

        elememts = roundEnv.getElementsAnnotatedWith(Key.class);
        for (Element e : elememts) {
            if(e.getKind() == ElementKind.PARAMETER){

                String protoClass = e.getEnclosingElement().getEnclosingElement().getSimpleName().toString();
                String method = e.getEnclosingElement().getSimpleName().toString();

                List<MethodModel> methodModels = functions.get(protoClass);

                for (MethodModel methodModel : methodModels) {
                    if (methodModel.getMethodName().equals(method)) {
                        String field = e.getAnnotation(Key.class).value();
                        Map<String, TypeMirror> params = methodModel.getParams();
                        params.put(field, e.asType());
                        break;
                    }
                }
            }

        }

        elememts = roundEnv.getElementsAnnotatedWith(RequestCode.class);
        for (Element e : elememts) {
            if (e.getKind() == ElementKind.METHOD) {
                String protolClass = e.getEnclosingElement().getSimpleName().toString();
                String method = e.getSimpleName().toString();

                List<MethodModel> methodModels = functions.get(protolClass);
                for (MethodModel methodModel : methodModels) {
                    if (methodModel.getMethodName().equals(method)) {
                        methodModel.setRequestCode(e.getAnnotation(RequestCode.class).value());
                        break;
                    }
                }
            }
        }
    }

    private void createJavaFile(String protolClass, List<MethodModel> methodModels) {
        TypeSpec.Builder routebuilder = TypeSpec.classBuilder(protolClass + "Impl");
        routebuilder.addModifiers(Modifier.PUBLIC);

        com.squareup.javapoet.ClassName intent = com.squareup.javapoet.ClassName.get("android.content", "Intent");

        for (MethodModel methodModel : methodModels) {
            Map<String, TypeMirror> params = methodModel.getParams();
            MethodSpec.Builder methodBuild = MethodSpec.methodBuilder(methodModel.getMethodName());
            methodBuild.returns(void.class);
            methodBuild.addModifiers(Modifier.PUBLIC, Modifier.STATIC);

            String className = methodModel.getClassName();
            methodBuild.addStatement("$T intent = new $T(context," + className + ".class)", intent, intent);

            for (Map.Entry<String, TypeMirror> param : params.entrySet()) {
                ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.get(param.getValue()), param.getKey()).build();
                methodBuild.addParameter(parameterSpec);
                if (!param.getKey().contains("context")) {

                    String type = param.getValue().toString();
                    if (type.contains("ArrayList")) {
                        if (type.contains("String")) {
                            methodBuild.addStatement("intent.putStringArrayListExtra(\"" + param.getKey() + "\"," + param.getKey() + ")");
                        } else if (type.contains("Integer")) {
                            methodBuild.addStatement("intent.putIntegerArrayListExtra(\"" + param.getKey() + "\"," + param.getKey() + ")");
                        } else if (type.contains("CharSequence")) {
                            methodBuild.addStatement("intent.putCharSequenceArrayList(\"" + param.getKey() + "\"," + param.getKey() + ")");
                        } else {
                            methodBuild.addStatement("intent.putParcelableArrayList(\"" + param.getKey() + "\"," + param.getKey() + ")");
                        }

                    } else {
                        methodBuild.addStatement("intent.putExtra(\"" + param.getKey() + "\"," + param.getKey() + ")");
                    }
                }
            }

            if (methodModel.getRequestCode() != -1) {
                methodBuild.addStatement("((android.app.Activity)context).startActivityForResult(intent," + methodModel.getRequestCode() + ")");
            } else {
                methodBuild.addStatement("context.startActivity(intent)");
            }
            routebuilder.addMethod(methodBuild.build());
        }

        TypeSpec routManager = routebuilder.build();
        JavaFile javaFile = JavaFile.builder("com.routefr.manager", routManager)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
