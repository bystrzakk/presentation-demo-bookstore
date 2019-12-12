package bstrzkk.test.spring.project.model.response;

import lombok.Data;

import java.util.List;

@Data
public class AllUsersResponse {
    private List<User> data;
}
