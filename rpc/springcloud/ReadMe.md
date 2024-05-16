

RestTemplate的Htttp Post请求我们经常使用下面两个方法：

1，postForObject()：返回Http协议的响应体
2，postForEntity()：返回ResponseEntity,ResponseEntity对Http进行了封装，除了包含响应体以外，还包含Http状态码、contentType、Header等信息。

##### postForObject()方法的使用

###### Json格式提交

```java
@PostMapping("comments")
    public TestEntity test(){
        TestEntity entity = new TestEntity();
        entity.setId(501);
        entity.setPostId(101);
        entity.setBody("test demo");
        entity.setEmail("Davion@zz.net");
        entity.setName("zhouo bang");
        TestEntity forEntity = restTemplate.postForObject("http://jsonplaceholder.typicode.com/comments?author.name=typicode", entity,TestEntity.class);
        return forEntity;
    }
```

**注**：postForObject的**第二个参数是请求数据对象，第三个参数是返回值类型**

###### 表单数据提交

```java
@PostMapping("comments/form")
    public String testform(){
        // 请求地址
        String url = "http://jsonplaceholder.typicode.com/comments";
        // 请求头设置,x-www-form-urlencoded格式的数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //提交参数设置
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("postId", "102");
        map.add("id", "502");
        map.add("name", "li si");
        map.add("email", "qq.@qq.com");
        map.add("body", "my body");
        // 组装请求体
        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<MultiValueMap<String, String>>(map, headers);
        // 发送post请求，并打印结果，以String类型接收响应结果JSON字符串
        String result = restTemplate.postForObject(url, request, String.class);
        return result;
    }
```

##### postForEntity()方法的使用

getForObject()所有的传参请求方式，getForEntity()都可以使用，使用方式也几乎一样。**在返回结果上有区别**，使用**ResponseEntity**来就收响应结果。

```java
@PostMapping("comments_4/{type}")
    public TestEntity test_4(@PathVariable("type")String type){
        TestEntity entity = new TestEntity();
        entity.setId(520);
        entity.setPostId(110);
        entity.setBody("comments_4 demo");
        entity.setEmail("comments_4@zz.net");
        entity.setName("zhouo comments_4");
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        ResponseEntity<TestEntity> forEntity = restTemplate.postForEntity("http://jsonplaceholder.typicode.com/{type}", entity,TestEntity.class,map);
        
        System.out.println("StatusCode: "+ forEntity.getStatusCode());
        System.out.println("StatusCodeValue: "+forEntity.getStatusCodeValue());
        System.out.println("Headers: "+ forEntity.getHeaders());
        return forEntity.getBody();
    }


RestTemplate restTemplate = new RestTemplate();
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_JSON);
User user = new User();
user.setName("张三");
user.setAge(20);
HttpEntity request = new HttpEntity<>(user, headers);
ResponseEntity responseEntity = restTemplate.postForEntity(url, request, String.class);
HttpHeaders responseHeaders = responseEntity.getHeaders();
String responseBody = responseEntity.getBody();
```

##### postForLocation() 方法的使用

postForLocation用法基本都和postForObject()或postForEntity()一致。**唯一区别在于返回值是一个URI**。该URI返回值体现的是：用于提交完成数据之后的页面跳转，或数据提交完成之后的下一步数据操作URI。

```java
@PostMapping("comments_5/{type}")
public String test_5(@PathVariable("type")String type){

    TestEntity entity = new TestEntity();
    entity.setId(520);
    entity.setPostId(110);
    entity.setBody("comments_4 demo");
    entity.setEmail("comments_4@zz.net");
    entity.setName("zhouo comments_4");
    Map<String,Object> map = new HashMap<>();
    map.put("type",type);
    URI uri = restTemplate.postForLocation("http://jsonplaceholder.typicode.com/{type}",entity,map);
    return uri.getPath();
}
```

```text
"http://jsonplaceholder.typicode.com/comments/501"
```