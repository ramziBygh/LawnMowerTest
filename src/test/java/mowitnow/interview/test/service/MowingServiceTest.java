package mowitnow.interview.test.service;

import com.oracle.tools.packager.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MowingServiceTest {

    private static final String EMPTY_FILE = "the file is empty";
    private static final String DIMENSIONS_EXCEPTION = "Illegal arguments: list of dimensions >2";
    private static final String MISSING_DATA_EXCEPTION = "Illegal Argument: Missed data";
    private static final String UNKNOWN_INSTRUCTION = "Illegal Argument: unknown instruction";
    private static final String SUCCESS_RESULT = "1 3 N\n" +
            "5 1 E\n";
    private static final String MOWING_STOPS_WHEN_OUT_OF_LAWN_RESULT = "1 3 N\n" +
            "6 3 E\n";

    @Autowired
    private MowingService mowingService;

    @Before
    public void setup() {

    }

    @Test
    public void emptyFileTest() throws IOException {
        MultipartFile multipartFile = transformFileIntoMultipartFile("src/test/resources/emptyFile");
        try {
            mowingService.moveTheMower(multipartFile);
        } catch(Exception e) {
            Assert.assertEquals(e.getMessage(), EMPTY_FILE);
        }
    }

    @Test
    public void wrongDimensionsTest() throws IOException {
        MultipartFile multipartFile = transformFileIntoMultipartFile("src/test/resources/eronedLawnDimensionsTestFile");
        try {
            mowingService.moveTheMower(multipartFile);
        } catch(Exception e) {
            Assert.assertEquals(e.getMessage(), DIMENSIONS_EXCEPTION);
        }
    }

    @Test
    public void missedDataTest() throws IOException {
        MultipartFile multipartFile = transformFileIntoMultipartFile("src/test/resources/missedDataTestFile");
        try {
            mowingService.moveTheMower(multipartFile);
        } catch(Exception e) {
            Assert.assertEquals(e.getMessage(), MISSING_DATA_EXCEPTION);
        }
    }

    @Test
    public void unknownInstructionTest() throws IOException {
        MultipartFile multipartFile = transformFileIntoMultipartFile("src/test/resources/unknownInstructionTestFile");
        try {
            mowingService.moveTheMower(multipartFile);
        } catch(Exception e) {
            Assert.assertEquals(e.getMessage(), UNKNOWN_INSTRUCTION);
        }
    }

    @Test
    public void successTest() throws Exception {
        MultipartFile multipartFile = transformFileIntoMultipartFile("src/test/resources/successTestFile");
        String result = mowingService.moveTheMower(multipartFile);
        Assert.assertEquals(result, SUCCESS_RESULT);

    }

    @Test
    public void mowingStopsWhenOutOfLawnTest() throws Exception {
        MultipartFile multipartFile = transformFileIntoMultipartFile("src/test/resources/mowingStopsWhenOutOfLawnTestFile");
        String result = mowingService.moveTheMower(multipartFile);
        Assert.assertEquals(result, MOWING_STOPS_WHEN_OUT_OF_LAWN_RESULT);

    }

    private MultipartFile transformFileIntoMultipartFile(String path) throws IOException {
        File testFile = new File(path);

        return new MockMultipartFile("file",
                testFile.getName(), "text/plain", IOUtils.readFully(testFile));
    }

}
