package tlschannel;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.util.function.Consumer;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;

/**
 * A {@link ByteChannel} that implements a channel-like interface for the TLS
 * protocol, using a {@link SSLEngine}.
 * <p>
 * Classes implementing this interface do not do any cryptography: everything is
 * delegated to a {@link SSLEngine}. The only protocol-level operations are
 * related to reading the Server Name Indication (SNI) in the server side, as
 * this is not currently supported by the {@link SSLEngine}.
 */
public interface TlsChannel extends ByteChannel, GatheringByteChannel, ScatteringByteChannel {

	/**
	 * Return a reference to the underlying {@link ByteChannel}.
	 */
	ByteChannel getUnderlying();

	/**
	 * Return a reference to the {@link SSLEngine} used.
	 * 
	 * @return the engine reference if present, or <code>null</code> if unknown
	 *         (that can happen in server-side channels before the SNI is
	 *         parsed).
	 */
	SSLEngine getSslEngine();

	/**
	 * Return the callback function to be executed when the TLS session is
	 * established (or re-established).
	 * 
	 * @see TlsChannelBuilder#withSessionInitCallback(Consumer)
	 */
	Consumer<SSLSession> getSessionInitCallback();

	/**
	 * Return the {@link BufferAllocator} to use for unencrypted data.
	 * 
	 * @see TlsChannelBuilder#withPlainBufferAllocator(BufferAllocator)
	 */
	BufferAllocator getPlainBufferAllocator();

	/**
	 * Return the {@link BufferAllocator} to use for encrypted data.
	 * 
	 * @see TlsChannelBuilder#withEncryptedBufferAllocator(BufferAllocator)
	 */
	BufferAllocator getEncryptedBufferAllocator();

	/**
	 * Return whether CPU-intensive tasks are run or not.
	 * 
	 * @see TlsChannelBuilder#withRunTasks(boolean)
	 */
	boolean getRunTasks();

	void renegotiate() throws IOException;

	void negotiate() throws IOException;

	void close();

}
