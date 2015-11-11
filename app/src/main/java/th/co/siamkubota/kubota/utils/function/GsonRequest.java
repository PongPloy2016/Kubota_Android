package th.co.siamkubota.kubota.utils.function;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

//import com.google.android.gms.appdatasearch.GetRecentContextCall;


/**
 * Created by sipangka on 7/6/2558.
 */

public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Response.Listener<T> listener;

    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> headers,
                       Map<String, String> params,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener)
    {
        super(method, url, errorListener);

        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.params = params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {

            String json = new String(
                    response.data,
                    "UTF-8");
            //HttpHeaderParser.parseCharset(response.headers)

            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public String getBodyContentType() {
       // return super.getBodyContentType();

        return "application/json; charset=utf-8";
    }
}