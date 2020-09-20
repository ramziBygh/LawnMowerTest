package mowitnow.interview.test.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.tools.packager.IOUtils;
import mowitnow.interview.test.service.MowingService;
import mowitnow.interview.test.service.MowingServiceImpl;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MowingWebServiceIT {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SUCCESS_RESULT = "1 3 N\n" +
            "5 1 E\n";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MowingService mowingService;

    @Before
    public void setUp() {

    }

    @Test
    public void testMowingWebService() throws Exception {
        File testFile = new File("src/test/resources/successTestFile");

        MockMultipartFile multipartFile = new MockMultipartFile("instructionsFile", testFile.getName(), "text/plain", IOUtils.readFully(testFile));
        mockMvc.perform(MockMvcRequestBuilders.multipart("/mowTheLawn")
                .file(multipartFile)
                .param("some-random", "0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(mowingService, Mockito.times(1)).moveTheMower(multipartFile);
    }
}
