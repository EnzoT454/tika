import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
public class RunTests {
  public static void main(String[] a) throws Exception {
    LauncherDiscoveryRequestBuilder b = LauncherDiscoveryRequestBuilder.request();
    for (String c : a) b.selectors(selectClass(Class.forName(c)));
    Launcher launcher = LauncherFactory.create();
    SummaryGeneratingListener l = new SummaryGeneratingListener();
    launcher.execute(b.build(), l);
    TestExecutionSummary s = l.getSummary();
    System.out.println("found=" + s.getTestsFoundCount() + " started=" + s.getTestsStartedCount() + " succeeded=" + s.getTestsSucceededCount() + " failed=" + s.getTestsFailedCount());
    for (TestExecutionSummary.Failure f : s.getFailures()) System.out.println("FAIL " + f.getTestIdentifier().getDisplayName() + " : " + f.getException());
  }
}
