package okhttp3.logging;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
public final class HttpLoggingInterceptor implements Interceptor {
  private static final Charset UTF8 = Charset.forName("UTF-8");

  public enum Level {
    NONE,
    BASIC,
    HEADERS,
    BODY
  }

  public interface Logger {
    void log(String message);

    /** A {@link Logger} defaults output appropriate for the current platform. */
    Logger DEFAULT = new Logger() {
      @Override public void log(String message) {
        Platform.get().log(INFO, message, null);
      }
    };
  }

  public HttpLoggingInterceptor() {
    this(Logger.DEFAULT);
  }

  public HttpLoggingInterceptor(Logger logger) {
    this.logger = logger;
  }

  private final Logger logger;

  private volatile Level level = Level.NONE;

  /** Change the level at which this interceptor logs. */
  public HttpLoggingInterceptor setLevel(Level level) {
    if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
    this.level = level;
    return this;
  }

  public Level getLevel() {
    return level;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Level level = this.level;

    Request request = chain.request();
    if (level == Level.NONE) {
      return chain.proceed(request);
    }

    boolean logBody = level == Level.BODY;
    boolean logHeaders = logBody || level == Level.HEADERS;

    RequestBody requestBody = request.body();
    boolean hasRequestBody = requestBody != null;

    Connection connection = chain.connection();
    
    String requestStartMessage = "--> "
        + request.method()
        + ' ' + request.url()
        + (connection != null ? " " + connection.protocol() : "");
    //request.url()

    System.out.println(requestStartMessage);
    
    if (logHeaders && (!(!logBody || !hasRequestBody) && !bodyEncoded(request.headers()))) {    
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
          charset = contentType.charset(UTF8);
        }
        if (isPlaintext(buffer)) {
          System.out.println(buffer.readString(charset));//request
        }
    }

    long startNs = System.nanoTime();
    Response response;
    try {
      response = chain.proceed(request);
    } catch (IOException e) {
      throw e;
    }
    long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

    ResponseBody responseBody = response.body();
    long contentLength = responseBody.contentLength();
    String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
    System.out.println( response.code()
        + (response.message().isEmpty() ? "" : ' ' + response.message())
        + ' ' + response.request().url()
        + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');

    if (logHeaders && ( !(!logBody || !HttpHeaders.hasBody(response)) && !bodyEncoded(response.headers()))) {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
          charset = contentType.charset(UTF8);
        }
        if (!isPlaintext(buffer)) {
          return response;
        }
        if (contentLength != 0) {        
         System.out.println(buffer.clone().readString(charset));
        }
    }
    return response;
  }

  static boolean isPlaintext(Buffer buffer) {
    try {
      Buffer prefix = new Buffer();
      long byteCount = buffer.size() < 64 ? buffer.size() : 64;
      buffer.copyTo(prefix, 0, byteCount);
      for (int i = 0; i < 16; i++) {
        if (prefix.exhausted()) {
          break;
        }
        int codePoint = prefix.readUtf8CodePoint();
        if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
          return false;
        }
      }
      return true;
    } catch (EOFException e) {
      return false;
    }
  }

  private boolean bodyEncoded(Headers headers) {
    String contentEncoding = headers.get("Content-Encoding");
    return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
  }
}
