package web.proxy.gae;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.http.*;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

@SuppressWarnings("serial")
public class ProxyServlet extends HttpServlet {
	private final static String UTF_8 = "UTF-8";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String encodedUrl = req.getParameter("url");
		if (encodedUrl == null) {
			resp.setStatus(500);
		} else {
			String urlStr = URLDecoder.decode(encodedUrl, UTF_8);
			String fetchStr = null;
			try {
				fetchStr = fetchUrl(urlStr, UTF_8);
			} catch (IOException ignored) {}
			
			if (fetchStr != null) {
				resp.setContentType("text/html; charset=" + UTF_8);
				resp.getWriter().print(fetchStr);
			}
		}
	}
	
	private static String fetchUrl(String urlStr, String encoding) throws IOException {
		URLFetchService urlFetchSrv = URLFetchServiceFactory.getURLFetchService();
		HTTPResponse res = null;
		URL url = new URL(urlStr);
		res = urlFetchSrv.fetch(url);
		return new String(res.getContent(), encoding);
	}
}
