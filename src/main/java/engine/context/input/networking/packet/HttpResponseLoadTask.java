//package context.input.networking.packet;
//
//import java.io.IOException;
//
//import engine.common.loader.IOLoadTask;
//
//public class HttpResponseLoadTask implements IOLoadTask<byte[]> {
//
//	private final HttpRequestModel requestModel;
//
//	public HttpResponseLoadTask(HttpRequestModel requestModel) {
//		this.requestModel = requestModel;
//	}
//
//	@Override
//	public byte[] loadIO() throws IOException {
//		return requestModel.execute();
//	}
//
//}
