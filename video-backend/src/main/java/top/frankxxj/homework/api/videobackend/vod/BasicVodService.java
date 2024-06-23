package top.frankxxj.homework.api.videobackend.vod;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.vod.v1.VodClient;
import com.huaweicloud.sdk.vod.v1.model.*;
import com.huaweicloud.sdk.vod.v1.region.VodRegion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.frankxxj.homework.api.videobackend.security.user.SecurityUser;
import top.frankxxj.homework.api.videobackend.video.CreateDTO;
import top.frankxxj.homework.api.videobackend.video.Video;
import top.frankxxj.homework.api.videobackend.video.VideoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BasicVodService {
    private final VideoRepository videoRepository;
    private VodClient client;

    public BasicVodService(VideoRepository videoRepository) {
        String ak = System.getenv("CLOUD_SDK_AK");
        String sk = System.getenv("CLOUD_SDK_SK");
        String projectId = "c63ee30438b64bf89d084daed7e88b37";

        ICredential auth = new BasicCredentials()
                .withProjectId(projectId)
                .withAk(ak)
                .withSk(sk);

        this.client = VodClient.newBuilder()
                .withCredential(auth)
                .withRegion(VodRegion.valueOf("cn-north-4"))
                .build();
        this.videoRepository = videoRepository;
    }

    public CreateAssetByFileUploadResponse createMediaAsset(CreateDTO dto) {
        return createMediaAsset(dto.title(), dto.description(), dto.filename(), "MP4");
    }

    public CreateAssetByFileUploadResponse createMediaAsset(String title, String description,
                                                            String video_name, String video_type) {
        CreateAssetByFileUploadRequest request = new CreateAssetByFileUploadRequest();
        CreateAssetByFileUploadReq body = new CreateAssetByFileUploadReq();
        body.withVideoType(video_type);
        body.withVideoName(video_name);
        body.withDescription(description);
        body.withTitle(title);
        request.withBody(body);
        try {
            CreateAssetByFileUploadResponse response = client.createAssetByFileUpload(request);
            log.debug("{}", response.toString());
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public UUID stringToUUID(String str) {
        String uuidStr = str.replaceAll(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"
        );
        return UUID.fromString(uuidStr);
    }

    public void confirmAssetUploadSolution(ConfirmDTO dto, String status) {
        ConfirmAssetUploadRequest request = new ConfirmAssetUploadRequest();
        ConfirmAssetUploadReq body = new ConfirmAssetUploadReq();
        body.withStatus(ConfirmAssetUploadReq.StatusEnum.fromValue(status));
        body.withAssetId(dto.asset_id());
        request.withBody(body);
        try {
            ConfirmAssetUploadResponse response = client.confirmAssetUpload(request);
            log.debug("{}", response.toString());
            var user = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal()).getUser();
            Video video = Video.builder().id(stringToUUID(response.getAssetId()))
                    .creator(user).description(dto.description()).title(dto.title())
                    .createTime(LocalDateTime.now()).build();
            videoRepository.save(video);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void deleteAssetsSolution(String... asset_ids) {
        DeleteAssetsRequest request = new DeleteAssetsRequest();
        try {
            DeleteAssetsResponse response = client.deleteAssets(request);
            request.withAssetId(Arrays.asList(asset_ids));
            log.info("{}", response.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void updateAssetInfoSolution(String assetId, String title, String description) {
        UpdateAssetMetaRequest request = new UpdateAssetMetaRequest();
        UpdateAssetMetaReq body = new UpdateAssetMetaReq();
        body.withAssetId(assetId);
        body.withTitle(title);
        body.withDescription(description);
        request.withBody(body);
        try {
            UpdateAssetMetaResponse response = client.updateAssetMeta(request);
            log.info("Update media: {}", response.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    public ShowAssetMetaResponse showAssetMeta(String... vid) {
        ShowAssetMetaRequest request = new ShowAssetMetaRequest();
        List<String> listRequestAssetId = new ArrayList<>();
        listRequestAssetId.addAll(Arrays.asList(vid));
        request.withAssetId(listRequestAssetId);
        try {
            ShowAssetMetaResponse response = client.showAssetMeta(request);
            System.out.println(response.toString());
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
