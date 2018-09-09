package com.lyl.husky.lifecycle.security;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import static org.apache.commons.codec.binary.Base64.*;

@Slf4j
public final class WwwAuthFilter implements Filter {

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final String AUTH_PREFIX = "Basic ";
    private static final String ROOT_IDENTIFY = "root";
    private static final String ROOT_DEFAULT_USERNAME = "root";
    private static final String ROOT_DEFAULT_PASSWORD = "root";
    private static final String GUEST_IDENTIFY = "guest";
    private static final String GUEST_DEFAULT_USERNAME = "guest";
    private static final String GUEST_DEFAULT_PASSWORD = "guest";

    private String rootUsername;
    private String rootPassword;
    private String guestUsername;
    private String guestPassword;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        Properties properties = new Properties();
        URL classLoaderURL = Thread.currentThread().getContextClassLoader().getResource("");
        if (null != classLoaderURL){
            String configFilePath = Joiner.on(FILE_SEPARATOR).join(classLoaderURL.getPath(), "conf", "auth.properties");
            try {
                properties.load(new FileInputStream(configFilePath));
            } catch (final IOException ex){
                log.warn("Cannot found auth config file, use default auth config");
            }
        }
        rootUsername = properties.getProperty("root.username", ROOT_DEFAULT_USERNAME);
        rootPassword = properties.getProperty("root.password", ROOT_DEFAULT_PASSWORD);
        guestUsername = properties.getProperty("guest.username", GUEST_DEFAULT_USERNAME);
        guestPassword = properties.getProperty("guest.username", GUEST_DEFAULT_PASSWORD);

    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) request;
        String authorization = httpRequest.getHeader("authorization");
        if (null != authorization && authorization.length() > AUTH_PREFIX.length()){
            authorization = authorization.substring(AUTH_PREFIX.length(), authorization.length());
            if (rootUsername.concat(":").concat(rootPassword).equals(new String(Base64.decodeBase64(authorization)))){
                authorizationSuccess(httpResponse, false);
                chain.doFilter(httpRequest, httpResponse);
            }else if (guestUsername.concat(":").concat(guestPassword).equals(new String(Base64.decodeBase64(authorization)))){
                authorizationSuccess(httpResponse, true);
            }else {
                needAuthenticate(httpResponse);
            }
        }else {
            needAuthenticate(httpResponse);
        }
    }

    private void authorizationSuccess(final HttpServletResponse response, final boolean isGuest) {
        response.setStatus(200);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setHeader("identify", isGuest ? GUEST_IDENTIFY : ROOT_IDENTIFY);
    }

    private void needAuthenticate(final HttpServletResponse response) {
        response.setStatus(401);
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setHeader("WWW-authenticate", AUTH_PREFIX + "Realm=\"Elastic Job Console Auth\"");
    }

    @Override
    public void destroy() {

    }
}
