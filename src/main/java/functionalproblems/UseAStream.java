package functionalproblems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
interface ExFunction<A, R> {
  R apply(A a) throws Throwable;
}

class Either<L, R> {
  private L left;
  private R right;

  private Either(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public static <L, R> Either<L, R> success(R r) {
    return new Either(null, r);
  }

  public static <L, R> Either<L, R> failure(L l) {
    return new Either(l, null);
  }

  public boolean isFailure() {
    return left != null;
  }

  public boolean isSuccess() {
    return left == null;
  }

  public void ifFailure(Consumer<L> op) {
    if (isFailure()) {
      op.accept(left);
    }
  }

  public R get() {
    if (isFailure()) throw new IllegalStateException("get on a failure");
    return right;
  }

//  public ... flatMapIfFailure(Function<L, Either<L, R>> op)
  public Either<L, R> recover(Function<L, Either<L, R>> op) {
    if (isFailure()) {
      return op.apply(left);
    } else return this;
  }

  public static <A, B> Function<A, Either<Throwable, B>>
    wrap(ExFunction<A, B> op) {
//  public static <A, B> Function<A, Optional<B>> wrap(ExFunction<A, B> op) {
    return a -> {
      try {
        return Either.success(op.apply(a));
//        return Optional.of(op.apply(a));
      } catch (Throwable e) {
        return Either.failure(e);
//        return Optional.empty();
      }
    };
  }
}

public class UseAStream {
//  public static Optional<Stream<String>> getFileContents(String fn) {
//    try {
//      return Optional.of(Files.lines(Path.of(fn)));
//    } catch (IOException e) {
////      throw new RuntimeException(e); // still breaks the flatMap operation
//      // generalized utility is NOT a good place for error recovery
//      // might be good to log the issue however
//      System.err.println("Problem with file " + fn);
//      return Optional.empty();
//    }
//  }

  public static void main(String[] args) {

    Function<Throwable, Either<Throwable, Stream<String>>> recovery =
        Either.wrap(t -> Files.lines(Path.of("d.txt")));

    try {
      Stream.of("a.txt", "b.txt", "c.txt")
          // filename -> contents of the file...
          // Files.lines -> take a Path, return Stream of String
//          .flatMap(fn -> getFileContents(fn))
//          .map(fn -> getFileContents(fn))
          .map(Either.wrap(fn -> Files.lines(Path.of(fn))))
//          .peek(opt -> {
//            if (opt.isEmpty()) {
//          .peek(either -> {
//            if (either.isFailure()) {
//              System.out.println("Problem with one of the files");
//            }})

          // ***** Here's where I was getting confused
          // AS CS correctly spotted, I actually needed to use the
          // recover function!!!
          // in other words, take the either that we have (which might
          // represent success, or might represent failure) and
          // invoke the recover function using our recovery method
          // if it's a failure, we'll get a new read on file d.txt
          // but if it was already success, we'll go with the data
          // we already have.
          // email me if you have difficulty with this!
          // also know that you should not use my code...
          // there is an excellent library called "vavr" vavr.io
          // which provides Either and more for functional
          // style programming
          .map(either -> either.recover(recovery))
          .peek(either -> either.ifFailure(
              t -> System.out.println("broke with " + t.getMessage())))

//          .filter(Optional::isPresent)
          .filter(Either::isSuccess)
//          .flatMap(Optional::get)
          .flatMap(Either::get)
          .forEach(System.out::println);
    } catch (Throwable t) {
      t.printStackTrace();
    }


//        .map(n -> n.toUpperCase())
//        .filter(s -> !s.startsWith("A"))
//        .forEach(s -> System.out.println(s));
  }
}
