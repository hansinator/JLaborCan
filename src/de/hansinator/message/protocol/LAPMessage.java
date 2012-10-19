package de.hansinator.message.protocol;

import java.io.IOException;
import java.io.InputStream;
import de.hansinator.message.io.MessageFactory;

/**
 * @author hansinator
 */
public class LAPMessage extends CANMessage {
	
	public static final int MAX_PORT = 63;
	
	public static final int MASK_PORT = 0x3F;
	
	public static final int MAX_ADDR = 255;
	
	public static final int MASK_ADDR = 0xFF;

    private final byte srcAddr;
    private final byte dstAddr;
    private final byte srcPort;
    private final byte dstPort;

    public LAPMessage(byte srcAddr, byte srcPort, byte dstAddr, byte dstPort, byte[] data, boolean remote) {
        super(((srcPort & 0x3F) << 23) | ((dstPort & 0x30) << 17)
                | ((int) ((dstPort & 0x0F) << 16)
                | (((int) srcAddr & 0xFF) << 8)
                | ((int) dstAddr) & 0xFF), data, remote);

        this.srcAddr = srcAddr;
        this.dstAddr = dstAddr;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
    }

    public byte getDstAddr() {
        return dstAddr;
    }

    public byte getDstPort() {
        return dstPort;
    }

    public byte getSrcAddr() {
        return srcAddr;
    }

    public byte getSrcPort() {
        return srcPort;
    }
    
    public final static MessageFactory<LAPMessage> factory = new MessageFactory<LAPMessage>() {

        public LAPMessage assemble(InputStream in) throws IOException {
            CANMessage msg = CANMessage.factory.assemble(in);
            byte srcAddr = (byte) ((msg.id >> 8) & 0xFF);
            byte dstAddr = (byte) (msg.id & 0xFF);
            byte srcPort = (byte) ((msg.id >> 23) & 0x3F);
            byte dstPort = (byte) (((msg.id >> 16) & 0x0F) | ((msg.id >> 17) & 0x30));

            return new LAPMessage(srcAddr, srcPort, dstAddr, dstPort, msg.getPayload(), msg.remote);
        }
    };
}
