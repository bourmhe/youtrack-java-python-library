package com.hbourmo.youtrack.impl;

import com.sun.istack.internal.Nullable;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sun.nio.cs.ext.IBM037;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hbourmo on 10/3/16.
 */
public class YouTrack
{
    private final String mURL;
    private final String mBaseURL;
    private final String mUser;
    private final String mPassword;
    private final HttpHeaders mHeader = new HttpHeaders();

    public YouTrack(String pURL, String pUser, String pPassword)
    {
        mURL = pURL.endsWith("/")?pURL.substring(0, pURL.length() - 1):pURL;
        mUser = pUser;
        mPassword = pPassword;
        mBaseURL = String.format("%s/rest", mURL);
    }

    private final HttpEntity<String> request(String pUrl, HttpMethod pMethod, LinkedMultiValueMap<String, Object> pData, MediaType pContentType)
    {
        if(pContentType == null)
        {
            pContentType =MediaType.APPLICATION_XML;
        }
        mHeader.setContentType(pContentType);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(pUrl, pMethod, new HttpEntity<Object>(pData, mHeader), String.class);
    }

    public boolean login() throws HttpClientErrorException
    {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("login", mUser);
        map.add("password", mPassword);
        try
        {
            HttpEntity<String> response = restTemplate.postForEntity(mBaseURL + "/user/login?login={LOGIN}&password={PASSWORD}", map, String.class, mUser, mPassword);
            mHeader.put("Cookie", response.getHeaders().get("Set-Cookie"));
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Connection failed to the YouTrack server. "+e.getMessage());
            return false;
        }
    }

    public HttpEntity<String> createIssue(LinkedMultiValueMap<String, Object> pData)
    {
        return request(mBaseURL+"/issue", HttpMethod.PUT, pData, MediaType.APPLICATION_FORM_URLENCODED);
    }

    public HttpEntity<String> execute(String pLocation, LinkedMultiValueMap<String, Object> pData)
    {
        return request(pLocation+"/execute", HttpMethod.POST, pData, MediaType.APPLICATION_FORM_URLENCODED);
    }

    public static void main(String[] args) {
        YouTrack youTrack = new YouTrack("http://10.0.1.9:8080/", "HPv3_users", "ibapass");
        youTrack.login();
        Issue issue = new Issue(youTrack, "HP3", "C'est un test avec un classe", "Trop bien ca marche");
        issue.setType("bug");
        issue.setCustomField("uuid", "1234567891234567");
        issue.setCustomField("site", "pat115");
    }
}
