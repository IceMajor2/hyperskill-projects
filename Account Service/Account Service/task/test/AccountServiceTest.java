import account.AccountServiceApplication;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class AccountServiceTest extends SpringTest {
  public AccountServiceTest() {
    super(AccountServiceApplication.class, "../service_db.mv.db");
  }

  SSLSocket socket;
  java.security.cert.X509Certificate[] chain;

  // Warning!!! Only for testing reason, trust all certificates!
  TrustManager[] trustAllCerts = new TrustManager[] {
          new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
              return new java.security.cert.X509Certificate[0];
            }
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
          }
  };

  // Test SSL
  public CheckResult checkCertificateName(String nameCN) {
    try {
      SSLContext sc = SSLContext.getInstance("SSL");
      //ТАК нельзя!!! доверяем всем сертификатам, только для тестирования и разработки!!!
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      SSLSocketFactory factory = sc.getSocketFactory();
      HttpRequest request = get("");
      socket = (SSLSocket) factory.createSocket(request.getHost(), request.getPort());
      getCertificates();
      if (findCert(nameCN)) {
        return CheckResult.correct();
      } else {
        throw new WrongAnswer("Not found certificate with CN - " + nameCN);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Connection not found");
      throw new WrongAnswer("Can't establish https connection!");
    }
  }

  // Get certificate chain
  public void getCertificates() {
    try {
      chain = (X509Certificate[]) socket.getSession().getPeerCertificates();
    } catch (SSLPeerUnverifiedException e) {
      e.printStackTrace();
      System.out.println(e.toString());
    }
  }


  // Searching certificate by Common Name
  public boolean findCert(String subject) {
    for (java.security.cert.X509Certificate c : chain) {
      String subjectName = c.getSubjectDN().getName();
      System.out.println(subjectName + " " + c.getSigAlgName());
      if (subjectName.contains("CN=" + subject)) {
        return true;
      }
    }
    return false;
  }

  @DynamicTest
  DynamicTesting[] dt = new DynamicTesting[]{

          // Check certificate name
          () -> checkCertificateName("accountant_service"),
  };
}
