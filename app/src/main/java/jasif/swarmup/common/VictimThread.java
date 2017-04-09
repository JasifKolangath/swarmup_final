package jasif.swarmup.common;

import java.io.File;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import android.util.Log;

public class VictimThread extends Thread {
	private OutputStream outStream = null;
	private byte[] jobParams = null;
	private int mode = -1;
	private int packetSize = 100;
	private String stringVal = null;

	public VictimThread(OutputStream pOut, byte[] pParams, int pMode) {
		this.outStream = pOut;
		this.jobParams = pParams;
		this.mode = pMode;
	}

	public VictimThread(OutputStream pOut, byte[] pParams, int pMode,
			int pPacket, String pS) {
		this.outStream = pOut;
		this.jobParams = pParams;
		this.packetSize = pPacket;
		this.mode = pMode;
		this.stringVal = pS;
	}

	public void run() {
		try {
//			BTFactory.getInstance().relock.lock();
			Log.d("Victim Thread", "In VictimThread, param mode is : "+mode+" stringVal is "+stringVal);
			int fullLen = jobParams.length;
			int writtenBytes = 0;
			File file = null;
			switch (mode) {
			case CommonConstants.READ_STRING_MODE:
				synchronized (outStream) {
					outStream
							.write(ByteBuffer.allocate(4).putInt(mode).array());
					outStream.flush();
					while (writtenBytes < fullLen) {
						if (fullLen - writtenBytes >= packetSize) {
							outStream
									.write(jobParams, writtenBytes, packetSize);
							outStream.flush();
							writtenBytes += packetSize;

						} else {
							outStream.write(jobParams, writtenBytes, fullLen
									- writtenBytes);
							outStream.flush();
							writtenBytes += (fullLen - writtenBytes);
						}
					}
					outStream.notify();
				}
				break;
			case CommonConstants.READ_FILE_MODE:
				synchronized (outStream) {
					JobPool.getInstance().transmitFileAsParams(jobParams,
							outStream, packetSize);
					outStream.notify();
				}
				file = FileFactory.getInstance().getFile(stringVal);
				if (file != null) {
					file.delete();
				}
				break;

			case CommonConstants.READ_FILES_MODE://face
				synchronized (outStream) {
//					JobPool.getInstance().transmitFilesAsParams(jobParams,
//							outStream, packetSize);
					
					JobPool.getInstance().transmitFileAsParams(jobParams,
							outStream, packetSize);
					outStream.notify();
				}
				file = FileFactory.getInstance().getFile(stringVal);
				if (file != null) {
					file.delete();
				}
				break;
				
			case CommonConstants.READ_FILE_NAME_MODE:
				synchronized (outStream) {
					JobPool.getInstance().transmitFilesAsParams(jobParams,
							outStream, packetSize);
					outStream.notify();
				}
				file = FileFactory.getInstance().getFile(stringVal);
				if (file != null) {
					file.delete();
				}
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally {
//
//			BTFactory.getInstance().relock.unlock();
//		}
	}
}
