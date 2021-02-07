package com.jenkin.systemservice.history.service;

import com.jenkin.common.config.FeignRequestConfig;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.vos.aibizhi.AbzResponse;
import com.jenkin.common.entity.vos.aibizhi.Category;
import com.jenkin.common.entity.vos.aibizhi.Wallpaper;
import feign.Headers;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.PrivateKey;
import java.util.Date;
import java.util.List;

/**
 * @author jenkin
 * @className AibizhiService
 * @description TODO
 * @date 2020/12/28 16:22
 */
@FeignClient(name = "histroyService",url = "${third.ssxx.url}",configuration = FeignRequestConfig.class)
public interface HistoryService {

    public static final String ACTIVITY_ID="5f71e934bcdbf3a8c3ba5061";
    public static final String MODE_ID="5f71e934bcdbf3a8c3ba51d5";
    public static final int WAY=1;


    /**
     * 获取token
     * @param uid
     * @param avatar
     * @param activity_id
     * @return
     */
    @GetMapping("/authorize/token/")
    TokenResponse  getToken(@RequestParam("uid") String uid,@RequestParam("avatar") String avatar,@RequestParam("activity_id") String activity_id);


    /**
     * 获取公钥
     * @return
     */
    @GetMapping("/base/public/key/")
    Response<MyPublicKey>  getPublicKey();

    /**
     * 获取 用户
     * @return
     */
    @GetMapping("/portal/user/")
    Response<UserInfo> getUser();


    /**
     * 获取问题列表
     * @param activity_id
     * @param mode_id
     * @param way
     * @return
     */
    @GetMapping("/race/beginning/")
    QuestionsResponse  getQuestionList(@RequestParam("activity_id") String activity_id,@RequestParam("mode_id") String mode_id,@RequestParam("way") String way);

    /**
     * 获取问题详情
     * @param activity_id
     * @param mode_id
     * @param way
     * @return
     */
    @GetMapping("/race/question/")
    Response<Question>  getQuestionInfo(@RequestParam("activity_id") String activity_id, @RequestParam("question_id") String question_id,
                                   @RequestParam("mode_id") String mode_id,@RequestParam("way") String way);

    /**
     * 答题
     * @param answerParam
     * @return
     */
    @PostMapping("/race/answer/")
    Response<Answer> answer(@RequestBody AnswerParam answerParam);


    /**
     * 答题
     * @param codeParam
     * @return
     */
    @PostMapping("/save/verification/code/")
    CheckStatus saveCode(@RequestBody CodeParam codeParam);



    /**
     * 答题
     * @param codeParam
     * @return
     */
    @PostMapping("/check/verification/code/")
    CheckStatus checkCode(@RequestBody CodeParam codeParam);





    /**
     * 交卷
     * @param submit
     * @return
     */
    @PostMapping("/race/finish/")
    Response<SubmitEntiry> finish(@RequestBody Submit submit);

    /**
     * 个人积分
     * @param activity_id
     * @return
     */
    @GetMapping("/race/grade/")
    Response<Person> grade(@RequestParam("activity_id") String activity_id);


    @Data
    static class TokenResponse{
        private Boolean code;
        private String token;
        private String refresh_token;
        private Boolean sign_in_result;
        private Boolean status_code;
    }
    @Data
    static class QuestionsResponse{
        private Integer code;
        private String race_code;
        private String prev_qid;
        private List<String> question_ids;

        private String mode_id;
        private String mode_title;
        private Integer mode;
        private Integer status_code;
    }

    @Data
    static class Answer{
        private Boolean correct;
        private Boolean finished;
        private String reason;
        private List<String> correct_ids;
    }
    @Data
    static class Submit{
        private String race_code;
    }
    @Data
    static  class Person{
        private String name;
        private String t_title;
        private String university_name;
        private String province_name;
        private Boolean cert_status;
        private Integer cert_type;
        private String cert_location;
        private Integer integral;
        private Integer badges;
        private Integer join_times;
        private Integer t_join_times;
        private Integer t_integral;
    }
    @Data
    static class UserInfo{

        private String id;
        private String code;
        private String name;
        private String mobile;
        private String zip;
        private String gender;
        private String email;
        private String university_id;
        private String university_name;
        private String identity;
        private String department;
        private Integer politics;
        private Integer category;
    }
    @Data
    static class Question {

        private String id;
        private String title;
        private Integer category;
        private String content;
        private String source;
        private List<Options> options;
        private String media_id;
        private String media_content_type;
        private String media_location;
    }
    @Data
    static class Options {

        private String id;
        private String code;
        private String title;
        private Integer weight;
    }
    @Data
    static class AnswerParam{
        private String activity_id;
        private String question_id;
        private List<String> answer;
        private String mode_id;
        private String way;
    }

    @Data
    static class SubmitEntiry{

        private int status;
        private int integral;
        private Owner owner;
        private Mode mode;
        private boolean badge;
    }
    @Data
    static class Mode {

        private String id;
        private String title;
        private int mode;
    }
    @Data
    static class Owner{
        private String id;
        private String name;
        private String univ_id;
        private String univ_name;
        private String province_code;
        private String province_name;
        private int correct_amount;
        private int consume_time;
    }
    @Data
    static class MyPublicKey{
        private String public_key;
    }


    @Data
    static class  CheckStatus{
        private Integer code;
        private Integer status_code;
        private Boolean status;
    }

    @Data
    static class CodeParam{
        private String activity_id;
        private String way;
        private String mode_id;
        private String code;
    }


}
