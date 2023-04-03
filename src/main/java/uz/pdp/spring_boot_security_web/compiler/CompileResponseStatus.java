package uz.pdp.spring_boot_security_web.compiler;

public enum CompileResponseStatus {
    TEST_CASE_SUCCESS("✅ Test case passed"),
    COMPILATION_SUCCESS("Compilation is successful"),
    COMPILATION_ERROR("❌ Compilation failed"),
    TEST_CASE_FAILED("❌ Test case failed !"),
    OTHER_ERROR("Something wrong");
    private final String message;
    CompileResponseStatus(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
