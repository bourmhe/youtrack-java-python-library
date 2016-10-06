package com.hbourmo.youtrack.impl;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ObjectUtils;
import sun.swing.MenuItemLayoutHelper;

import java.net.URI;
import java.net.URL;
import java.util.Map;

/**
 * Created by hbourmo on 10/5/16.
 */
public class Issue
{
    private YouTrack mServer;
    private URI mLocation;
    private String mProject;
    private String mSummary;
    private String mDescription;
    private String mAssignee;
    private String mPriority;
    private String mType;
    private String mSubsystem;
    private String mState;
    private String mAffectsVersion;
    private String mFixedVersion;
    private String mFixedInBuild;
    private String mPermittedGroup;
    private LinkedMultiValueMap<String, Object> mCustomField;

    public Issue(YouTrack pServer, String pProject, String pSummary, String pDescription, String pAssignee, String pPriority, String pType,
                 String pSubsystem, String pState, String pAffectsVersion, String pFixedVersion, String pFixedInBuild,
                 String pPermittedGroup)
    {
        mServer = pServer;
        mProject = pProject;
        mSummary = pSummary;
        mDescription = pDescription;
        mAssignee = pAssignee;
        mPriority = pPriority;
        mType = pType;
        mSubsystem = pSubsystem;
        mState = pState;
        mAffectsVersion = pAffectsVersion;
        mFixedVersion = pFixedVersion;
        mFixedInBuild = pFixedInBuild;
        mPermittedGroup = pPermittedGroup;
        mLocation = mServer.createIssue(getDefaultData()).getHeaders().getLocation();
    }

    public Issue(YouTrack pServer, String pProject, String pSummary, String pDescription)
    {
        this(pServer, pProject, pSummary, pDescription, null, null, null, null, null, null, null, null, null);
    }

    private final LinkedMultiValueMap<String, Object> getDefaultData()
    {
        LinkedMultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>();
        data.add("project", mProject);
        data.add("summary", mSummary);
        data.add("description", mDescription);
        if(mAssignee != null)
        {
            data.add("assignee", mAssignee);
        }
        if(mPriority != null)
        {
            data.add("priority", mPriority);
        }
        if(mType != null)
        {
            data.add("type", mType);
        }
        if(mSubsystem != null)
        {
            data.add("subsystem", mSubsystem);
        }
        if(mState != null)
        {
            data.add("state", mState);
        }
        if(mAffectsVersion != null)
        {
            data.add("affectsVersion", mAffectsVersion);
        }
        if(mFixedVersion != null)
        {
            data.add("fixedVersion", mFixedVersion);
        }
        if(mFixedInBuild != null)
        {
            data.add("fixedInBuild", mFixedInBuild);
        }
        if(mPermittedGroup != null)
        {
            data.add("permittedGroup", mPermittedGroup);
        }
        return data;
    }

    private void updateIssue(String pUpdatedValue)
    {
        LinkedMultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>();
        data.add("command", pUpdatedValue);
        mServer.execute(mLocation.toString(), data);
    }

    public void setAssignee(String pAssignee)
    {
        mAssignee = pAssignee;
        updateIssue(String.format("%s %s", "assignee", mAssignee));
    }

    public void setPriority(String pPriority)
    {
        mPriority = pPriority;
        updateIssue(String.format("%s %s", "priority", mPriority));
    }

    public void setType(String pType)
    {
        mType = pType;
        updateIssue(String.format("%s %s", "type", mType));
    }

    public void setSubsystem(String pSubsystem)
    {
        mSubsystem = pSubsystem;
        updateIssue(String.format("%s %s", "subsystem", mSubsystem));
    }

    public void setState(String pState)
    {
        mState = pState;
        updateIssue(String.format("%s %s", "state", mState));
    }

    public void setAffectsVersion(String pAffectsVersion)
    {
        mAffectsVersion = pAffectsVersion;
        updateIssue(String.format("%s %s", "affectsVersion", mAffectsVersion));
    }

    public void setFixedVersion(String pFixedVersion)
    {
        mFixedVersion = pFixedVersion;
        updateIssue(String.format("%s %s", "fixedVersion", mFixedVersion));
    }

    public void setFixedInBuild(String pFixedInBuild)
    {
        mFixedInBuild = pFixedInBuild;
        updateIssue(String.format("%s %s", "fixedInBuild", mFixedInBuild));

    }

    public void setPermittedGroup(String pPermittedGroup)
    {
        mPermittedGroup = pPermittedGroup;
        updateIssue(String.format("%s %s", "permittedGroup", mPermittedGroup));
    }

    public void setCustomField(String pFieldName, String pValue)
    {
        if(mCustomField == null)
        {
            mCustomField = new LinkedMultiValueMap<String, Object>();
        }
        mCustomField.add(pFieldName, pValue);
        updateIssue(String.format("%s %s", pFieldName, pValue));
    }

    public void addCustomFields(Map<String, String> pCustomFieldsMap)
    {
        for (String key : pCustomFieldsMap.keySet())
        {
            setCustomField(key, pCustomFieldsMap.get(key));
        }
    }
}
