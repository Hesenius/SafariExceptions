package verify;

sealed class V permits Verify {}
public final class Verify extends V {
  public static void main(String[] args) {
    System.out.println("Hello Java 17 World!");
  }
}
