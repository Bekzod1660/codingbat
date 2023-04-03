package uz.pdp.spring_boot_security_web.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.spring_boot_security_web.common.exception.RecordNotFountException;
import uz.pdp.spring_boot_security_web.entity.AttachmentContentEntity;
import uz.pdp.spring_boot_security_web.entity.AttachmentEntity;
import uz.pdp.spring_boot_security_web.entity.UserEntity;
import uz.pdp.spring_boot_security_web.repository.AttachmentContentRepository;
import uz.pdp.spring_boot_security_web.repository.AttachmentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private static final String getPath = "../static/";
    private static final String uploadPath2 = "C:\\Users\\user\\Spring\\codingbat-testCase\\src\\main\\resources\\static\\view\\img";


    @SneakyThrows
    public String uploadImage2(MultipartFile file) {

        if (file != null) {
            String originalFileName = file.getOriginalFilename();
            long size = file.getSize();
            String contentType = file.getContentType();

            AttachmentEntity attachment = new AttachmentEntity();
            attachment.setSize(size);
            attachment.setContentType(contentType);
            attachment.setFileOriginalName(originalFileName);

            String randomName = makeRandomFileName(originalFileName);

            attachment.setName(randomName);
            AttachmentEntity saveAttachment = attachmentRepository.save(attachment);

            AttachmentContentEntity attachmentContentEntity = new AttachmentContentEntity();
            attachmentContentEntity.setAttachment(saveAttachment);
            attachmentContentEntity.setBytes(file.getBytes());
            attachmentContentRepository.save(attachmentContentEntity);

            writeToFile(file, randomName);
            return randomName;
        }
        throw new IOException("file  not found");
    }

    private String makeRandomFileName(String originalFileName) {
        String[] split = originalFileName.split("\\.");
        return UUID.randomUUID() + "." + split[split.length - 1];
    }

    @SneakyThrows
    private void writeToFile(MultipartFile file, String randomName) {
        Path path = Paths.get(uploadPath2 + "/" + randomName);
        Files.copy(file.getInputStream(), path);
    }

    public String updateImage(MultipartFile file, String url) throws IOException {
        if (url != null) {
            Optional<AttachmentEntity> foundByName = attachmentRepository.findByName(url);
            String randomName = makeRandomFileName(file.getOriginalFilename());
            if (foundByName.isPresent()) {
                writeToFile(file, randomName);
                AttachmentEntity updateAttachment = updateAttachmentEntity(foundByName.get(), file, randomName);
                updateAttachmentContentEntity(file,updateAttachment);
                deleteExistFile(url);
                return randomName;
            } if (url.startsWith("http")){
                writeToFile(file,randomName);
                AttachmentEntity attachmentEntity = updateAttachmentEntity(new AttachmentEntity(), file, randomName);
                updateAttachmentContentEntity(file,attachmentEntity);
                return randomName;
            }else {
                throw new RecordNotFountException("AttachmentEntity was not found");
            }
        }
        return "";
    }

    public AttachmentEntity updateAttachmentEntity(AttachmentEntity attachment,MultipartFile file, String randomName) {
        attachment.setName(randomName);
        attachment.setSize(file.getSize());
        attachment.setContentType(file.getContentType());
        attachment.setFileOriginalName(file.getOriginalFilename());
        return attachmentRepository.save(attachment);
    }

    public void updateAttachmentContentEntity( MultipartFile file, AttachmentEntity updateAttachment) throws IOException {
        Optional<AttachmentContentEntity> foundByAtmId = attachmentContentRepository.findByAttachmentId(updateAttachment.getId());
        if (foundByAtmId.isPresent()) {
            setValueInAttachmentCont(foundByAtmId.get(),file,updateAttachment);
        } else {
            setValueInAttachmentCont(new AttachmentContentEntity(),file,updateAttachment);
//            throw new RecordNotFountException("AttachmentContentEntity was not found");
        }
    }
    @SneakyThrows
    public void setValueInAttachmentCont(AttachmentContentEntity attachmentContentEntity, MultipartFile file, AttachmentEntity updateAttachment){
        attachmentContentEntity.setBytes(file.getBytes());
        attachmentContentEntity.setAttachment(updateAttachment);
        attachmentContentRepository.save(attachmentContentEntity);
    }
    private void deleteExistFile(String url){
        File removedFile = new File(uploadPath2 + "/" + url);
        if (removedFile.delete()) {
            System.out.println("File was  deleted successfully");
        } else {
            System.out.println("File was Not  deleted successfully");
        }
    }
}
