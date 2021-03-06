/*
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.client.lib.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.cloudfoundry.client.lib.ApplicationLogListener;
import org.cloudfoundry.client.lib.ClientHttpResponseCallback;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.RestLogCallback;
import org.cloudfoundry.client.lib.StartingInfo;
import org.cloudfoundry.client.lib.StreamingLogToken;
import org.cloudfoundry.client.lib.UploadStatusCallback;
import org.cloudfoundry.client.lib.archive.ApplicationArchive;
import org.cloudfoundry.client.lib.domain.ApplicationLog;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudDomain;
import org.cloudfoundry.client.lib.domain.CloudEvent;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.cloudfoundry.client.lib.domain.CloudQuota;
import org.cloudfoundry.client.lib.domain.CloudRoute;
import org.cloudfoundry.client.lib.domain.CloudSecurityGroup;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudServiceBroker;
import org.cloudfoundry.client.lib.domain.CloudServiceInstance;
import org.cloudfoundry.client.lib.domain.CloudServiceOffering;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.CloudStack;
import org.cloudfoundry.client.lib.domain.CloudUser;
import org.cloudfoundry.client.lib.domain.CrashesInfo;
import org.cloudfoundry.client.lib.domain.InstancesInfo;
import org.cloudfoundry.client.lib.domain.ServiceKey;
import org.cloudfoundry.client.lib.domain.Staging;
import org.cloudfoundry.client.lib.domain.Upload;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * Interface defining operations available for the cloud controller REST client implementations
 *
 * @author Thomas Risberg
 */
public interface CloudControllerClient {

    // User and Info methods

    void addDomain(String domainName);

    void addRoute(String host, String domainName);

    void associateAuditorWithSpace(String orgName, String spaceName, String userGuid);

    void associateDeveloperWithSpace(String orgName, String spaceName, String userGuid);

    void associateManagerWithSpace(String orgName, String spaceName, String userGuid);

    void bindRunningSecurityGroup(String securityGroupName);

    void bindSecurityGroup(String orgName, String spaceName, String securityGroupName);

    void bindService(String appName, String serviceName);

    void bindStagingSecurityGroup(String securityGroupName);

    void createApplication(String appName, Staging staging, Integer memory, List<String> uris, List<String> serviceNames);

    void createApplication(String appName, Staging staging, Integer disk, Integer memory, List<String> uris, List<String> serviceNames);

    // Service methods

    void createQuota(CloudQuota quota);

    void createSecurityGroup(CloudSecurityGroup securityGroup);

    void createSecurityGroup(String name, InputStream jsonRulesFile);

    void createService(CloudService service);

    void createServiceBroker(CloudServiceBroker serviceBroker);

    void createSpace(String spaceName);

    void createUserProvidedService(CloudService service, Map<String, Object> credentials);

    void createUserProvidedService(CloudService service, Map<String, Object> credentials, String syslogDrainUrl);

    void debugApplication(String appName, CloudApplication.DebugMode mode);

    void deleteAllApplications();

    void deleteAllServices();

    void deleteApplication(String appName);

    void deleteDomain(String domainName);

    List<CloudRoute> deleteOrphanedRoutes();

    void deleteQuota(String quotaName);

    // App methods

    void deleteRoute(String host, String domainName);

    void deleteSecurityGroup(String securityGroupName);

    void deleteService(String service);

    void deleteServiceBroker(String name);

    void deleteSpace(String spaceName);

    CloudApplication getApplication(String appName);

    CloudApplication getApplication(String appName, boolean required);

    CloudApplication getApplication(UUID appGuid);

    CloudApplication getApplication(UUID appGuid, boolean required);

    Map<String, Object> getApplicationEnvironment(UUID appGuid);

    Map<String, Object> getApplicationEnvironment(String appName);

    List<CloudEvent> getApplicationEvents(String appName);

    InstancesInfo getApplicationInstances(String appName);

    InstancesInfo getApplicationInstances(CloudApplication app);

    ApplicationStats getApplicationStats(String appName);

    List<CloudApplication> getApplications();

    List<CloudApplication> getApplications(String inlineDepth);

    URL getCloudControllerUrl();

    Map<String, String> getCrashLogs(String appName);

    CrashesInfo getCrashes(String appName);

    CloudDomain getDefaultDomain();

    List<CloudDomain> getDomains();

    List<CloudDomain> getDomainsForOrg();

    List<CloudEvent> getEvents();

    String getFile(String appName, int instanceIndex, String filePath, int startPosition, int endPosition);

    CloudInfo getInfo();

    Map<String, String> getLogs(String appName);

    CloudOrganization getOrganization(String orgName);

    CloudOrganization getOrganization(String orgName, boolean required);

    Map<String, CloudUser> getOrganizationUsers(String orgName);

    List<CloudOrganization> getOrganizations();

    List<CloudDomain> getPrivateDomains();

    CloudQuota getQuota(String quotaName);

    CloudQuota getQuota(String quotaName, boolean required);

    List<CloudQuota> getQuotas();

    List<ApplicationLog> getRecentLogs(String appName);

    List<CloudRoute> getRoutes(String domainName);

    List<CloudSecurityGroup> getRunningSecurityGroups();

    CloudSecurityGroup getSecurityGroup(String securityGroupName);

    CloudSecurityGroup getSecurityGroup(String securityGroupName, boolean required);

    List<CloudSecurityGroup> getSecurityGroups();

    CloudService getService(String service);

    CloudService getService(String service, boolean required);

    CloudServiceBroker getServiceBroker(String name);

    CloudServiceBroker getServiceBroker(String name, boolean required);

    List<CloudServiceBroker> getServiceBrokers();

    CloudServiceInstance getServiceInstance(String serviceName);

    CloudServiceInstance getServiceInstance(String serviceName, boolean required);

    List<ServiceKey> getServiceKeys(String serviceName);

    List<CloudServiceOffering> getServiceOfferings();

    List<CloudService> getServices();

    List<CloudDomain> getSharedDomains();

    // Space management

    CloudSpace getSpace(String spaceName);

    CloudSpace getSpace(String spaceName, boolean required);

    List<UUID> getSpaceAuditors(String orgName, String spaceName);
    
    List<UUID> getSpaceAuditors(UUID spaceGuid);

    List<UUID> getSpaceDevelopers(String orgName, String spaceName);
    
    List<UUID> getSpaceDevelopers(UUID spaceGuid);

    // Domains and routes management

    List<UUID> getSpaceManagers(String orgName, String spaceName);
    
    List<UUID> getSpaceManagers(UUID spaceGuid);

    List<CloudSpace> getSpaces();

    List<CloudSpace> getSpacesBoundToSecurityGroup(String securityGroupName);

    CloudStack getStack(String name);

    CloudStack getStack(String name, boolean required);

    List<CloudStack> getStacks();

    String getStagingLogs(StartingInfo info, int offset);

    List<CloudSecurityGroup> getStagingSecurityGroups();

    OAuth2AccessToken login();

    void logout();

    void openFile(String appName, int instanceIndex, String filePath, ClientHttpResponseCallback callback);

    void register(String email, String password);

    void registerRestLogListener(RestLogCallback callBack);

    // Misc. utility methods

    void removeDomain(String domainName);

    void rename(String appName, String newName);

    StartingInfo restartApplication(String appName);

    void setQuotaToOrg(String orgName, String quotaName);

    void setResponseErrorHandler(ResponseErrorHandler errorHandler);

    StartingInfo startApplication(String appName);

    void stopApplication(String appName);

    StreamingLogToken streamLogs(String appName, ApplicationLogListener listener);

    void unRegisterRestLogListener(RestLogCallback callBack);

    void unbindRunningSecurityGroup(String securityGroupName);

    void unbindSecurityGroup(String orgName, String spaceName, String securityGroupName);

    void unbindService(String appName, String serviceName);

    void unbindStagingSecurityGroup(String securityGroupName);

    void unregister();

    void updateApplicationDiskQuota(String appName, int disk);

    // Security Group Operations

    void updateApplicationEnv(String appName, Map<String, String> env);

    void updateApplicationEnv(String appName, List<String> env);

    void updateApplicationInstances(String appName, int instances);

    void updateApplicationMemory(String appName, int memory);

    void updateApplicationServices(String appName, List<String> services);

    void updateApplicationStaging(String appName, Staging staging);

    void updateApplicationUris(String appName, List<String> uris);

    void updatePassword(String newPassword);

    void updatePassword(CloudCredentials credentials, String newPassword);

    void updateQuota(CloudQuota quota, String name);

    void updateSecurityGroup(CloudSecurityGroup securityGroup);

    void updateSecurityGroup(String name, InputStream jsonRulesFile);

    void updateServiceBroker(CloudServiceBroker serviceBroker);

    void updateServicePlanVisibilityForBroker(String name, boolean visibility);

    void uploadApplication(String appName, File file, UploadStatusCallback callback) throws IOException;

    void uploadApplication(String appName, InputStream inputStream, UploadStatusCallback callback) throws IOException;

    void uploadApplication(String appName, ApplicationArchive archive, UploadStatusCallback callback) throws IOException;

    String asyncUploadApplication(String appName, File file, UploadStatusCallback callback) throws IOException;

    String asyncUploadApplication(String appName, ApplicationArchive archive, UploadStatusCallback callback) throws IOException;

    Upload getUploadStatus(String uploadToken);

}
