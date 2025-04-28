package test;

/**
 * @author MnhTng
 * @Package test
 * @date 4/26/2025 11:38 AM
 * @Copyright tùng
 */

class Person {
    String name;
}

public class Test {
    public static void changeName(Person p) {
        p.name = "Alice";
        p = new Person();
        p.name = "Bob";
    }

    public static void main(String[] args) {
        Person person = new Person();
        person.name = "Tung";
        changeName(person);
        System.out.println(person.name);
        // Alice
    }
}
