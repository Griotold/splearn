package tobyspring.splearn.adapter.integration;


import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;
import tobyspring.splearn.domain.Email;

import static org.assertj.core.api.Assertions.assertThat;

class DummyEmailSenderTest {

    @StdIo
    @Test
    void send(StdOut stdOut) {
        DummyEmailSender dummyEmailSender = new DummyEmailSender();

        dummyEmailSender.send(new Email("rio@email.com"), "subject", "body");

        assertThat(stdOut.capturedLines()[0]).isEqualTo("DummyEmailSender send email: Email[address=rio@email.com]");
    }
}