package mowitnow.interview.test.webservice;

import mowitnow.interview.test.service.MowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MowingWebService {

    @Autowired
    private MowingService mowingService;

    @PostMapping("/mowTheLawn")
    public String mowTheLawn(@RequestParam("instructionsFile") MultipartFile instructionsFile) throws Exception {
        return mowingService.moveTheMower(instructionsFile);
    }
}
