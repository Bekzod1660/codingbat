package uz.pdp.spring_boot_security_web.compiler;

import org.springframework.stereotype.Component;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;
import uz.pdp.spring_boot_security_web.model.dto.CompileResponseDto;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;


@Component
public class CompliedClass {
    public CompileResponseDto compile(String source, String methodName, String firstParamType, String secondParamType, String result) {

        File folder = new File("./src");
        File sourceFile = new File(folder, "Solution.java");

        try {
            Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sourceFile.getPath());
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.out.println("JDK required (running inside of JRE)");
        } else {
            System.out.println("you got it!");
        }

        int compilationResult = compiler.run(null, null, null, sourceFile.getPath());
        if (compilationResult == 0) {
            System.out.println(CompileResponseStatus.COMPILATION_SUCCESS.getMessage());
        } else {
            System.out.println(CompileResponseStatus.COMPILATION_ERROR.getMessage());
            return new CompileResponseDto( CompileResponseStatus.COMPILATION_ERROR,"❌ Compilation failed");
        }

        try {
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {folder.toURI().toURL() });
            Class<?> cls = Class.forName("Solution", true, classLoader);
            Object instance = cls.newInstance();
            Method method = cls.getDeclaredMethod(methodName, Class.forName("java.lang."+firstParamType),Class.forName("java.lang."+secondParamType));
            Object invoke = method.invoke(instance,null,null);
            System.out.println(invoke);
            if(String.valueOf(invoke).equals(result)) {
                return new CompileResponseDto(CompileResponseStatus.TEST_CASE_SUCCESS,"✅ Test case passed");
            }else{
                return new CompileResponseDto(CompileResponseStatus.TEST_CASE_FAILED,"❌ Test case failed !\n" +
                        "Expected -> "+result+" ;\n" +
                        "Actual -> "+invoke+" ;");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something wrong");
            return new CompileResponseDto(CompileResponseStatus.OTHER_ERROR,"Something wrong");
        }
    }
    public CompileResponseDto passTestCase(String[] split, String source, TestCaseEntity testCaseDto){
        String[] replaceSource = source.split("\\{", 3);
        String resultSource = "public class Solution{\n"
                +"    public "+split[0]+" "+split[1]+"("+split[2]+" firstParam, "+split[3]+" secondParam) {\n"+
                "        firstParam = " + testCaseDto.getFirstParam()+ ";secondParam = " + testCaseDto.getSecondParam() + ";"+replaceSource[2];
        return compile(resultSource,split[1],split[2],split[3], testCaseDto.getResult());
    }

    public List<String> passAllTestCases(
            List<TestCaseEntity> testCases,
            String[] split,
            String source){
        List<String> resList = new LinkedList<>();
        for (TestCaseEntity testCase : testCases) {
            CompileResponseDto compileResponseDto = passTestCase(split, source, testCase);
            if(!compileResponseDto.getStatus().equals(CompileResponseStatus.COMPILATION_ERROR)){
                resList.add(compileResponseDto.getMessage());
            }else{
                resList.add(compileResponseDto.getMessage());
                return resList;
            }
        }
        return resList;
    }
}
