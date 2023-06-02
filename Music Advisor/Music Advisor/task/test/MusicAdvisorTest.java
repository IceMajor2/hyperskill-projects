import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.List;
import java.util.function.Function;

class Attach {
    Function<String, CheckResult> func;

    Attach(Function<String, CheckResult> func) {
        this.func = func;
    }
}

public class MusicAdvisorTest extends StageTest<Attach> {

    @Override
    public List<TestCase<Attach>> generate() {
        return List.of(
                new TestCase<Attach>()
                        .setInput("featured\nnew\nexit")
                        .setAttach(new Attach(reply -> {
                            if (reply.split("\n").length == 0 || !reply.split("\n")[0].contains("---FEATURED---")) {
                                return new CheckResult(false,
                                        "Wrong answer!");
                            }
                            if (!reply.contains("---NEW RELEASES---")) {
                                return new CheckResult(false,
                                        "Wrong answer!");
                            }
                            return CheckResult.correct();
                        })),
                new TestCase<Attach>()
                        .setInput("new\nexit")
                        .setAttach(new Attach(reply -> {
                            if (reply.split("\n").length == 0 || !reply.split("\n")[0].contains("---NEW RELEASES---")) {
                                return new CheckResult(false,
                                        "Wrong answer!");
                            }
                            return CheckResult.correct();
                        })),
                new TestCase<Attach>()
                        .setInput("categories\nexit")
                        .setAttach(new Attach(reply -> {
                            if (reply.split("\n").length == 0 || !reply.split("\n")[0].contains("---CATEGORIES---")) {
                                return new CheckResult(false,
                                        "Wrong answer!");
                            }
                            return CheckResult.correct();
                        })),
                new TestCase<Attach>()
                        .setInput("playlists Mood\ncategories\nexit")
                        .setAttach(new Attach(reply -> {
                            if (reply.split("\n").length == 0 || !reply.split("\n")[0].contains("PLAYLISTS---")) {
                                return new CheckResult(false,
                                        "Wrong answer!");
                            }
                            if (!reply.contains("---CATEGORIES---")) {
                                return new CheckResult(false,
                                        "Wrong answer!");
                            }
                            return CheckResult.correct();
                        })),
                new TestCase<Attach>()
                        .setInput("exit\n")
                        .setAttach(new Attach(reply -> {
                            if (reply.split("\n").length == 0 || !reply.split("\n")[0].contains("---GOODBYE!---")) {
                                return new CheckResult(false,
                                        "Wrong answer!");
                            }
                            return CheckResult.correct();
                        }))
        );
    }

    @Override
    public CheckResult check(String reply, Attach clue) {
        return clue.func.apply(reply);
    }
}