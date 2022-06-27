package functionalproblems;

import java.util.Map;
import java.util.Optional;

public class UseOptional {
  public static String getFirstName() {
    return "Freddy";
  }

  public static void main(String[] args) {
    Map<String, String> names = Map.of("Fred", "Jones");
    String firstName = getFirstName();
    String lastName = names.get(firstName);
    if (lastName != null) { // easy to forget
      String message = "Dear " + lastName.toUpperCase();
      System.out.println(message);
    }

    System.out.println("------------------");

    Optional<Map<String, String>> namesOpt = Optional.of(names);
    namesOpt.map(m -> m.get(getFirstName()))
        .map(n -> "Dear " + n.toUpperCase())
        .ifPresent(s -> System.out.println(s));
  }
}
