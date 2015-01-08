/**
 * Copyright (c) 2014, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.email;

import com.jcabi.email.stamp.StRecipient;
import com.jcabi.email.stamp.StSender;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link Envelope}.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 1.4
 */
public final class EnvelopeTest {

    /**
     * Envelope.Constant can cache.
     * @throws Exception If fails
     */
    @Test
    public void cachesPreviousCall() throws Exception {
        final Envelope origin = Mockito.mock(Envelope.class);
        Mockito.doReturn(
            new MimeMessage(Session.getDefaultInstance(new Properties()))
        ).when(origin).unwrap();
        final Envelope envelope = new Envelope.Constant(origin);
        envelope.unwrap();
        envelope.unwrap();
        Mockito.verify(origin, Mockito.times(1)).unwrap();
    }

    /**
     * Envelope.MIME can wrap another envelope.
     * @throws Exception If fails
     */
    @Test
    public void wrapsAnotherEnvelope() throws Exception {
        final Envelope origin = new Envelope.MIME().with(
            new StSender("jack@example.com")
        );
        final Message message = new Envelope.MIME(origin).with(
            new StRecipient("paul@example.com")
        ).unwrap();
        MatcherAssert.assertThat(
            message.getAllRecipients().length,
            Matchers.equalTo(1)
        );
        MatcherAssert.assertThat(
            message.getFrom().length,
            Matchers.equalTo(1)
        );
    }

}
