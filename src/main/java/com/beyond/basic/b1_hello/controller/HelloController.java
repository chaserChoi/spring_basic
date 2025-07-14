package com.beyond.basic.b1_hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Component 어노테이션을 통해 별도의 객체를 생성할 필요가 없는, 싱글톤 객체 생성
// Controller 어노테이션을 통해 쉽게 사용자의 http req을 분석하고, http res를 생성
@Controller
// 클래스 차원에 url 매핑 시에는 RequestMapping을 사용
@RequestMapping("/hello")
public class HelloController {

    // get 요청의 case들
    // case 1. 서버가 사용자에게 단순 String 데이터 return - @ResponseBody 있을 때
    @GetMapping("") // 아래 메서드에 대한 서버의 엔드 포인트를 설정
    // ResponseBody가 없고, return 타입이 String인 경우 서버는  templates 폴더 밑에 helloworld.html을 찾아서 리턴
    @ResponseBody
    public String helloWorld() {
        return "helloworld";
    }

    // case 2. 서버가 사용자에게 String(json 형식)의 데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Hello helloJson() throws JsonProcessingException {
        Hello h1 = new Hello("hong", "hong@naver.com");
        // 직접 json으로 직렬화 할 필요 없이, return 타입에 객체가 있으면 자동으로 직렬화
//        ObjectMapper objectMapper = new ObjectMapper();
//        String result = objectMapper.writeValueAsString(h1);
        return h1;
    }

    // case 3. parameter 방식을 통해 사용자로부터 값을 수신
    // parameter 형식 : /member?name=hongildong
    @GetMapping("/param")
    @ResponseBody
    public Hello param(@RequestParam(value = "name") String inputName) {
        System.out.println(inputName);
        return new Hello(inputName, "jae1@naver.com");
    }

    // case 4. path variable 방식을 통해 사용자로부터 값을 수신
    // path variable 형식 : /member/1
    // path variable 방식은 url을 통해 자원 구조를 명확하게 표현할 때 사용 (좀 더 restful 형)
    @GetMapping("/path/{inputId}")
    @ResponseBody
    public String path(@PathVariable Long inputId) {
        // 별도의 형변환 없이도, 매개변수에 타입 지정 시 자동형변환 시켜줌.
//        long id = Long.parseLong(inputId);
        System.out.println(inputId);
        return "OK";
    }

    // case 5. parameter 2개 이상 형식
    // /hello/param2?name=hongildong&email=hong@naver.com
    @GetMapping("/param2")
    @ResponseBody
    public String param2(@RequestParam(value = "name") String inputName,
                         @RequestParam(value = "email") String inputEmail ) {
        System.out.println(inputName);
        System.out.println(inputEmail);
        return "OK";
    }

    // case 6. parameter가 많아질 경우, 데이터 바인딩을 통해 input값 처리
    // 데이터 바인딩 : param을 사용하여 객체로 생성해줌
    // ?name=hong&email=hong@naver.com
    @GetMapping("/param3")
    @ResponseBody
//    public String parma3(Hello hello) {
    // @ModelAttribute를 써도 되고 안써도 되는데, 이 키워드를 써서 명시적으로 param 혈식의 데이터를 받겠다라는 것을 표현 (사용 권장)
    public String parma3(@ModelAttribute Hello hello) {
        System.out.println(hello);
        return "OK";
    }

    // case 7. 서버에서 화면을 return, 사용자로부터 넘어오는 input 값을 활용하여 동적인 화면 생성
    // 서버에서 화면(+데이터)을 렌더링해주는 ssr 방식 (csr은 서버는 데이터ㅁ만)
    // MVC(model view comtrollerZ) 패턴이라고 함
    @GetMapping("/model-param")
    public String modelParam(@RequestParam(value = "id") Long inputId, Model model) {
        // model 객체는 데이터를 화면에 전달해주는 역할
        // name이라는 키에 hongildong이라는 value를 key:value 형태로 화면에 전달
        if (inputId == 1) {
            model.addAttribute("name", "hongildong1");
            model.addAttribute("email", "hong1@naver.com");
        } else if (inputId == 2) {
            model.addAttribute("name", "hongildong2");
            model.addAttribute("email", "hong2@naver.com");
        }
        return "helloworld2";
    }

    // post 요청의 case들 : url 인코딩 방식 또는 multipart-formdata, json
    // case 1. text만 있는 form-data 형식
    // 형식 : body부에 name=xxx&email=xxx
    @GetMapping("/form-view")
    public String formView() {
        return "form-view";
    }
    @PostMapping("/form-view")
    @ResponseBody
    // get 요청에 url에 파라미터 방식과 동일한 데이터 형식이므로, RequestParam 또는 데이터 바인딩 방식 가능
    public String formViewPost(@ModelAttribute Hello hello) {
        System.out.println(hello);
        return "ok";
    }

    // case 2-1. text와 file이 있는 form-data 형식 (순수 html 방식)
    @GetMapping("/form-file-view")
    public String formFileView() {
        return "form-file-view";
    }
    @PostMapping("/form-file-view")
    @ResponseBody
    public String formFileViewPost(@ModelAttribute Hello hello,
                                   @RequestParam(value = "photo")MultipartFile photo) {
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "ok";
    }

    // case 2-2. text와 file이 있는 form-data 형식 (js로 제출)
    @GetMapping("/axios-file-view")
    public String axiosFileView() {
        return "axios-file-view";
    }

    // case 3. text와 multi file이 있는 form-data 형식 (js로 제출)
    @GetMapping("/axios-multi-file-view")
    public String axiosMultiFileView() {
        return "axios-multi-file-view";
    }
    @PostMapping("/axios-multi-file-view")
    @ResponseBody
    public String axiosMultiFileViewPost(@ModelAttribute Hello hello,

                                         @RequestParam(value = "photo") List<MultipartFile> photos) {
        System.out.println(hello);
        for (int i = 0; i < photos.size(); i++) {
            System.out.println(photos.get(i).getOriginalFilename());
        }
        return "ok";
    }

    // case 4. json 데이터 처리
    @GetMapping("/axios-json-view")
    public String axiosJsonView() {
        return "axios-json-view";
    }
    @PostMapping("/axios-json-view")
    @ResponseBody
    public String axiosJsonViewPost(@RequestBody Hello hello) {
        System.out.println(hello);
        return "ok";
    }

    // case 5. 중첩된 json 데이터 처리
    @GetMapping("/axios-nested-json-view")
    public String axiosNestedJsonView() {
        return "axios-nested-json-view";
    }
    @PostMapping("/axios-nested-json-view")
    @ResponseBody
    // RequestBody 어노테이션을 통해 json 형식의 데이터를 객체로 변환
    public String axiosNestedJsonViewPost(@RequestBody Student student) {
        System.out.println(student);
        return "ok";
    }

    // case 6. json(text) + file 같이 처리할 때 : text 구조가 복잡하며 피치 못하게 json을 써야하는 경우
    // 데이터 형식 :  hello={name:"xx", email:"xxx@naver.com"}&photo=이미지.jpg
    // 결론은 단순 json 구조가 아닌, multipart-formdata
    @GetMapping("/axios-json-file-view")
    public String axiosJsonFileView() {
        return "axios-json-file-view";
    }

    @PostMapping("/axios-json-file-view")
    @ResponseBody
    public String axiosJsonFileViewPost(
            @RequestPart(value = "hello") Hello hello,
            @RequestPart(value = "photo") MultipartFile photo) {
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "ok";
    }
}
