package mowitnow.interview.test.service;

import org.springframework.web.multipart.MultipartFile;

public interface MowingService {

    String moveTheMower(MultipartFile instructionsFile) throws Exception;
}
