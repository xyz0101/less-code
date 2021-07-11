package com.jenkin.systemservice.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.UserQo;
import com.jenkin.systemservice.SystemApplication;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("devhome")
@RunWith(SpringJUnit4ClassRunner.class)
@EnableDiscoveryClient
@AutoConfigureMockMvc
@SpringBootTest(classes = SystemApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void init(){
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        System.out.println("初始化mock实例成功");
    }

    @Test
    public void listUserByPage() throws Exception {
        BaseQo<UserQo> query = new BaseQo<>();
        query.setPage(1);
        query.setPageSize(10);
        UserQo userQo = new UserQo();
        userQo.setUserName("jenkin");
        query.setData(userQo);

            MvcResult mvcResult = mockMvc.perform(
                    MockMvcRequestBuilders.post("/lsc/system/user/listUserByPage")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("token","kTqzFtkEPUufjkgKf9WBYKOhjSVQHxhQe1JafS+GmRJMPiJ8gpq/Hf/cp1bhF5ahuC063u8LBy1ddNXs2n6G+CL0qF7WwFbUoNZMlE9h8S8ZMYe8OBoY+Lly/40xMA286xqb4lNSE+SfjimjGq6uIvfdI0UwlKy+Zhd05KSTUJCmm2uy4OA+6b+hd2CEC98OF3uhh3j6ygxg/OXC3JlRWpshK0L5lUhPiKbRnQ4Zpcf8H981Zy3yNaJzpB403j7k")
                            .accept(MediaType.APPLICATION_JSON)
                            .content(JSON.toJSONString(query))
            )
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            assertEquals(200,response.getStatus());
        String contentAsString = response.getContentAsString();
        Response<Page<UserDto>> res = JSON.parseObject(contentAsString, new TypeReference<Response<Page<UserDto>>>(){} );
        assertNotNull(res);
        assertNotNull(res.getData());
        assertEquals("200",res.getCode());

        System.out.println(res);

    }

}