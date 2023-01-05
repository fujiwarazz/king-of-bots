import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author peelsannaw
 * @create 05/01/2023 19:23
 */
public class test {

    public static void main(String[] args) {
        List<student> list = new ArrayList<>();
        list.add(new student("123",123));
        student student = list.get(0);
        student.setName("12345");
        System.out.println(list.get(0).name);
    }
}

@Data
@AllArgsConstructor
class student{
    String name;
    Integer age;
}