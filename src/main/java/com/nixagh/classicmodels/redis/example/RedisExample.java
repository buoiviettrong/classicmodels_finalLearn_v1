package com.nixagh.classicmodels.redis.example;

import com.nixagh.classicmodels.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class RedisExample implements CommandLineRunner {
    private final RedisTemplate<String, Object> template;

    @Override
    public void run(String... args) throws Exception {
//        withLettuce();
    }

    private void withLettuce() {
        // set value "nixagh" with key "name" to redis
//        template.opsForValue().set("name", "nixagh");

//        HashOperations<String, Object, User> hashOperations = template.opsForHash();
//        User user1 = User.builder()
//                .id(1L)
//                .firstName("nixagh1")
//                .lastName("nixagh")
//                .email("nixagh@gmail.com")
//                .password("123456")
//                .build();

//        SortQuery<String> sortQuery = SortQueryBuilder
//                .sort("fetch")
//                .order(SortParameters.Order.ASC)
//                .limit(10,20)
//                .build();
//        List<Object> list = template.sort(sortQuery);
//        System.out.println(list);

        ListOperations<String, Object> listOperations = template.opsForList();

        List<Object> list = listOperations.range("fetch", 0, 10);


//        template.opsForHash().put("users", 2, user1);

//        Map<Object, User> users = hashOperations.entries("users");


        // set value 23 with key "age" to redis
//        template.opsForValue().set("age", 23);
//
//        // set object user with key "user" to redis
//        User user1 = User.builder()
//                .id(1L)
//                .firstName("nixagh")
//                .lastName("nixagh")
//                .email("nixagh@gmail.com")
//                .password("123456")
//                .build();
//        template.opsForValue().set("user", user1);
//        // set list of user with key "users" to redis
//        List<User> users = new ArrayList<>();
//        users.add(user1);
//        User user2 = User.builder()
//                .id(2L)
//                .firstName("nghia")
//                .lastName("nghia")
//                .email("nghia@gmail.com")
//                .password("123456")
//                .build();
//        users.add(user2);
//        template.opsForValue().set("users", users);
//
//        // get value with key "name" from redis
//        String name = (String) template.opsForValue().get("name");
//        System.out.println("name: " + name);
//        // get value with key "age" from redis
//        Integer age = (Integer) template.opsForValue().get("age");
//        System.out.println("age: " + age);
//        // get object user with key "user" from redis
//        User user = (User) template.opsForValue().get("user");
//        System.out.println("user: " + user);
//        // get list of user with key "users" from redis
//        List<User> usersFromRedis = (List<User>) template.opsForValue().get("users");
//        System.out.println("users: " + usersFromRedis);
//
//
//        // del key "name" from redis
////        template.delete("name");
//
//        // set users on left to redis with key "users"
////        template.opsForList().leftPush("users", users);
//
//        // set users on right to redis with key "users"
////        template.opsForList().rightPush("users", users);
//
//
//        // get users from redis with key "users" from index 0 to index 100
////        List<Object> usersFromRedis2 = (List<Object>) template.opsForList().range("users", 0, 100);
////        System.out.println("usersFromRedis2: " + usersFromRedis2);
//
//        // get user at index 10 from redis with key "users"
//        User userFromRedis = (User) template.opsForList().index("users", 10);
//        System.out.println("userFromRedis: " + userFromRedis);
//
//        // get last user from redis with key "users"
//        User lastUserFromRedis = (User) template.opsForList().rightPop("users");
//        System.out.println("lastUserFromRedis: " + lastUserFromRedis);
//
//        // get first user from redis with key "users"
//        User firstUserFromRedis = (User) template.opsForList().leftPop("users");
//        System.out.println("firstUserFromRedis: " + firstUserFromRedis);
//
//        // opsForSet example
//
//        // set user1 to redis with key "user"
//        template.opsForSet().add("user", user1);
//        // set user2 to redis with key "user"
//        template.opsForSet().add("user", user2);
//
//        // get all users from redis with opsForSet
//        Set<Object> usersFromRedis_ = template.opsForSet().members("user");
//        System.out.println("usersFromRedis_: " + usersFromRedis_);
//
//        // check user1 is member of set with key "user"
//        Boolean isMember = template.opsForSet().isMember("user", user1);
//        System.out.println("isMember: " + isMember);
//
//        // delete user1 from set with key "user"
////        template.opsForSet().remove("user", user1);
//
//        // opsForHash example
//        // set user1 to redis with key "user"
//        template.opsForHash().put("user", "user1", user1);
//        // set user2 to redis with key "user"
//        template.opsForHash().put("user", "user2", user2);
//
//        // get all users from redis with opsForHash
//        Map<Object, Object> usersFromRedis_2 = template.opsForHash().entries("user");
//        System.out.println("usersFromRedis_2: " + usersFromRedis_2);
//
//        // get user1 from redis with key "user"
//        User userFromRedis2 = (User) template.opsForHash().get("user", "user1");
//        System.out.println("userFromRedis2: " + userFromRedis2);
//
//        // delete user1 from redis with key "user"
////        template.opsForHash().delete("user", "user1");
//
//        // opsForZSet example
//        // set user1 to redis with key "user"
//        template.opsForZSet().add("user", user1, 1);
//        // set user2 to redis with key "user"
//        template.opsForZSet().add("user", user2, 2);
//
//        // get users from redis with opsForZSet with range from 0 to 100
//        Set<Object> usersFromRedis3 = template.opsForZSet().range("user", 0, 100);
//        System.out.println("usersFromRedis3: " + usersFromRedis3);
//
//        // get users from redis with opsForZSet with range from 0 to 100 by score
//        Set<Object> usersFromRedis4 = template.opsForZSet().rangeByScore("user", 0, 100);
//        System.out.println("usersFromRedis4: " + usersFromRedis4);
//
//        // delete user1 from redis with key "user"
////        template.opsForZSet().remove("user", user1);
    }
}
