package com.example.uploadexcelfile.controller;

import com.example.uploadexcelfile.AbstractTest;
import com.example.uploadexcelfile.model.UploadedFile;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class FileUploadControllerTest extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }


    @Test
    @WithMockUser(username = "default-user", password = "user", roles = "user_role")
    public void testUploadFiles() throws Exception {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("registrationList.xlsx");

        assert resourceAsStream != null;
        MockMultipartFile file = new MockMultipartFile("file", "registrationList.xlsx", "application/vnd.ms-excel", resourceAsStream);
        String uri = "/file/upload";
        MvcResult mvcResult =  mvc.perform(MockMvcRequestBuilders.multipart(uri)
                        .file(file)
                        .characterEncoding("UTF-8")).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        UploadedFile actual = super.mapFromJson(content, UploadedFile.class);
        assertEquals(actual.getTotalRecords(),18);
    }
}