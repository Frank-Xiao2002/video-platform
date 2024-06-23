package top.frankxxj.homework.api.videobackend.vod;

import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.*;
import com.huaweicloud.sdk.iam.v3.region.IamRegion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BasicIAMService {
    private IamClient client;

    public BasicIAMService() {
        String ak = System.getenv("CLOUD_SDK_AK");
        String sk = System.getenv("CLOUD_SDK_SK");
        String domainId = "";

        ICredential auth = new GlobalCredentials()
                .withDomainId(domainId)
                .withAk(ak)
                .withSk(sk);

        this.client = IamClient.newBuilder()
                .withCredential(auth)
                .withRegion(IamRegion.valueOf("cn-north-4"))
                .build();
    }

    public String getToken() {
        KeystoneCreateUserTokenByPasswordRequest request = new KeystoneCreateUserTokenByPasswordRequest();
        KeystoneCreateUserTokenByPasswordRequestBody body = new KeystoneCreateUserTokenByPasswordRequestBody();
        AuthScopeProject projectScope = new AuthScopeProject();
        projectScope.withName("cn-north-4_api-course");
        AuthScope scopeAuth = new AuthScope();
        scopeAuth.withProject(projectScope);
        PwdPasswordUserDomain domainUser = new PwdPasswordUserDomain();
        domainUser.withName("hwid_oop9igud4h2wlai");
        PwdPasswordUser userPassword = new PwdPasswordUser();
        userPassword.withDomain(domainUser)
                .withName("api-course")
                .withPassword("bjtu12345");
        PwdPassword passwordIdentity = new PwdPassword();
        passwordIdentity.withUser(userPassword);
        List<PwdIdentity.MethodsEnum> listIdentityMethods = new ArrayList<>();
        listIdentityMethods.add(PwdIdentity.MethodsEnum.fromValue("password"));
        PwdIdentity identityAuth = new PwdIdentity();
        identityAuth.withMethods(listIdentityMethods)
                .withPassword(passwordIdentity);
        PwdAuth authbody = new PwdAuth();
        authbody.withIdentity(identityAuth)
                .withScope(scopeAuth);
        body.withAuth(authbody);
        request.withBody(body);
        try {
            KeystoneCreateUserTokenByPasswordResponse response = client.keystoneCreateUserTokenByPassword(request);
            System.out.println(response.toString());
            return response.getXSubjectToken();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}


