package com.example.uploadexcelfile.controller;



import com.example.uploadexcelfile.AbstractTest;
import com.example.uploadexcelfile.model.UploadedFile;
import com.example.uploadexcelfile.repository.FileRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FileControllerTest extends AbstractTest {

    @Autowired
    private FileRepository fileRepository;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    @WithMockUser(username = "default-user", password = "user", roles = "user_role")
    public void testFileById() throws Exception {
        UploadedFile expected = fileRepository.save(new UploadedFile()
                .toBuilder().fileName(RandomStringUtils
                        .randomAlphabetic(10))
                .uploadBy(adminUser.getId())
                .totalRecords(10)
                .totalUploaded(10).build());

        String uri = "/files/" + expected.getId();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull(content);
    }

    @Test
    public void testListFiles() throws Exception {

        int randomNo = Integer.parseInt(RandomStringUtils.randomNumeric(1));
        List<UploadedFile> list = IntStream.range(0, randomNo).mapToObj(value -> new UploadedFile()
                .toBuilder().fileName(RandomStringUtils
                        .randomAlphabetic(10))
                .uploadBy(adminUser.getId())
                .totalRecords(10)
                .totalUploaded(10).build()).collect(Collectors.toList());
        fileRepository.saveAll(list);

        String uri = "/files/list";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Assert.assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull(content, list);
    }

    @Test
    public void testDeleteById() throws Exception {

        UploadedFile expected = fileRepository.save(new UploadedFile()
                .toBuilder().fileName(RandomStringUtils
                        .randomAlphabetic(10))
                .uploadBy(adminUser.getId())
                .totalRecords(10)
                .totalUploaded(10).build());

        String uri = "/files/delete/" + expected.getId();
        mvc.perform(MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

        Optional<UploadedFile> byId = fileRepository.findById(expected.getId());
        byId.ifPresent(uploadDocument -> Assert.assertTrue(uploadDocument.getDeleted()));
    }
}
