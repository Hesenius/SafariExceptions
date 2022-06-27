package pointofsale;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

class ModemDidNotConnectException extends Exception {
}

class ModemLibrary {
  public static void dialModem(int phonenumber) throws ModemDidNotConnectException {
  }
}

class InfrastructureException extends Exception {
  public InfrastructureException(Throwable cause) {
    super("Something broke in the comms", cause);
  }
}

public class SellStuff {
  public static boolean USE_NETWORK = true;
  public static void sellSomething(int price, int phone)
//        throws ModemDidNotConnectException, IOException {
        throws InfrastructureException {  // really this is just IOException
    int retries = 3;
    boolean success = false;
    while (retries > 0 && !success) {
      try { // try block represents "happy path"
        // connect
        if (USE_NETWORK) {
          Socket s = new Socket("127.0.0.1", 1234);
        } else {
          ModemLibrary.dialModem(1234);
        }
        // authorize payment
        // ...
//      if (good...)
        success = true;
      } catch (IOException | ModemDidNotConnectException mdnce) {
        // catching here at low level?
        // maybe we can retry?
        // maybe we actually have the ability to do some "real" recovery
        // maybe we simply don't have the "resources" to do anything
        // except give up.
        if (--retries == 0) {
          throw new InfrastructureException(mdnce);
        }
      }
    }
  }

  public static void main(String[] args) {
    // customer approaches
    // determine price and payment method
    // if credit card...
    try {
      sellSomething(1234, 1234);
      // let them take it away
//    } catch (ModemDidNotConnectException | IOException e) {
    } catch (InfrastructureException e) {
      // ask for alternative form of payment??
    }
//    } catch (Exception e) {
//      // catches way tooo many exception types, including
//      // all the Runtime variants!
//      // ask for alternative...
//    }
//    } catch (ModemDidNotConnectException mdnce) {
//      // ask for alternative form of payment??
//    } catch (IOException ioe) {
//      // ask for alternative form of payment??
//    }

  }
}
