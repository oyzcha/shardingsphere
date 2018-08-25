/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.proxy.transport.mysql.packet.generic;

import io.shardingsphere.proxy.transport.mysql.constant.StatusFlag;
import io.shardingsphere.proxy.transport.mysql.packet.MySQLPacketPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class EofPacketTest {
    
    @Mock
    private MySQLPacketPayload packetPayload;
    
    @Test
    public void assertNewEofPacketWithSequenceId() {
        EofPacket actual = new EofPacket(1);
        assertThat(actual.getSequenceId(), is(1));
        assertThat(actual.getWarnings(), is(0));
        assertThat(actual.getStatusFlags(), is(StatusFlag.SERVER_STATUS_AUTOCOMMIT.getValue()));
    }
    
    @Test
    public void assertNewEofPacketWithMySQLPacketPayload() {
        when(packetPayload.readInt1()).thenReturn(1, EofPacket.HEADER);
        when(packetPayload.readInt2()).thenReturn(0, StatusFlag.SERVER_STATUS_AUTOCOMMIT.getValue());
        EofPacket actual = new EofPacket(packetPayload);
        assertThat(actual.getSequenceId(), is(1));
        assertThat(actual.getWarnings(), is(0));
        assertThat(actual.getStatusFlags(), is(StatusFlag.SERVER_STATUS_AUTOCOMMIT.getValue()));
        verify(packetPayload, times(2)).readInt1();
        verify(packetPayload, times(2)).readInt2();
    }
    
    @Test
    public void assertWrite() {
        new EofPacket(1).write(packetPayload);
        verify(packetPayload).writeInt1(EofPacket.HEADER);
        verify(packetPayload).writeInt2(0);
        verify(packetPayload).writeInt2(StatusFlag.SERVER_STATUS_AUTOCOMMIT.getValue());
    }
}
