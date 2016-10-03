package com.hbourmo.youtrack.impl;

import com.sun.istack.internal.Nullable;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
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

    private final HttpEntity<String> request(String pUrl, HttpMethod pMethod, Map<String, String> pURLVariables, @Nullable MediaType pContentType)
    {
        if(pContentType == null)
        {
            pContentType =MediaType.APPLICATION_XML;
        }
        mHeader.setContentType(pContentType);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(mBaseURL+pUrl, pMethod, new HttpEntity<Object>(mHeader), String.class, pURLVariables);
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

    public void createIssue(String pProject, String pSummary, String pDescription)
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("PROJECT", pProject);
        param.put("SUMMARY", pSummary);
        param.put("DESCRIPTION", pDescription);
        request("/issue?project={PROJECT}&summary={SUMMARY}&description={DESCRIPTION}", HttpMethod.PUT, param, MediaType.APPLICATION_FORM_URLENCODED);
    }

}
