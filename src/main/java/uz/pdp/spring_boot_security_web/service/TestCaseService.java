package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.spring_boot_security_web.entity.TaskEntity;
import uz.pdp.spring_boot_security_web.entity.TestCaseEntity;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.model.dto.TaskRequestDTO;
import uz.pdp.spring_boot_security_web.model.dto.TestCaseDto;
import uz.pdp.spring_boot_security_web.repository.TaskRepository;
import uz.pdp.spring_boot_security_web.repository.TestCaseRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TaskRepository questionService;


    public List<TestCaseEntity> getList() {
        return testCaseRepository.findAll();
    }


    public TestCaseEntity getById(int id) {
        Optional<TestCaseEntity> byId = testCaseRepository.findById(id);
        return byId.orElseThrow(() -> new RecordNotFountException("Test Case not found"));
    }


    public void delete(int id) {
        TestCaseEntity byId = getById(id);
        testCaseRepository.delete(byId);
    }


    public TestCaseEntity add(TestCaseDto testCaseDto) {
        TestCaseEntity test = TestCaseEntity.builder()
                .firstParam(testCaseDto.getFirstParam())
                .secondParam(testCaseDto.getSecondParam())
                .question(questionService.findById(testCaseDto.getTaskId()).orElse(null))
                .build();
        String result = null;
        if (testCaseDto.getResult().startsWith("\"") && testCaseDto.getResult().endsWith("\"")) {
            result = testCaseDto.getResult().substring(1, testCaseDto.getResult().length() - 2);
            test.setResult(result);
        } else {
            test.setResult(testCaseDto.getResult());
        }
        return testCaseRepository.save(test);
    }

    public List<TestCaseEntity> testCasesOfQuestion(Integer id) {
        Optional<List<TestCaseEntity>> allByQuestionId = testCaseRepository.findAllByQuestionId(id);
        return allByQuestionId.orElseThrow(()-> new RecordNotFountException("Test case not found"));
    }

    public List<String> quantityOfSuccessfulTestCases(List<String> list) {
        return list.stream().filter(s -> s.contains("✅")).toList();
    }
    public TestCaseEntity update(int id, TestCaseDto testCaseDto) {
        TestCaseEntity testCaseEntity = getById(id);
        if (testCaseDto.getFirstParam() != null && !testCaseDto.getFirstParam().equals(""))
            testCaseEntity.setFirstParam(testCaseDto.getFirstParam());
        if (testCaseDto.getSecondParam() != null && !testCaseDto.getSecondParam().equals(""))
            testCaseEntity.setSecondParam(testCaseDto.getSecondParam());
        if (testCaseDto.getResult() != null && !testCaseDto.getResult().equals(""))
            testCaseEntity.setResult(testCaseDto.getResult());
        return testCaseRepository.save(testCaseEntity);

    }
}
