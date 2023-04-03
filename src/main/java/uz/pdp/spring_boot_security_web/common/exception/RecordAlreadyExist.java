package uz.pdp.spring_boot_security_web.common.exception;

public class RecordAlreadyExist extends RuntimeException{
    public RecordAlreadyExist(String message) {
        super(message);
    }
}
