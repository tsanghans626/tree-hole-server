package com.lwznkj.treeholeserver.ai.embedding;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.hunyuan.v20230901.HunyuanClient;
import com.tencentcloudapi.hunyuan.v20230901.models.GetEmbeddingRequest;
import com.tencentcloudapi.hunyuan.v20230901.models.GetEmbeddingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.Embedding;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Custom EmbeddingModel implementation for Tencent Hunyuan Embedding API.
 *
 * <p>Tencent Hunyuan API details:
 * <ul>
 *   <li>Endpoint: hunyuan.tencentcloudapi.com</li>
 *   <li>Action: GetEmbedding</li>
 *   <li>Vector dimensions: 1024</li>
 *   <li>Max input length: 1024 tokens</li>
 * </ul>
 *
 * @see <a href="https://cloud.tencent.com/document/product/1729/102832">Tencent Hunyuan Embedding API</a>
 */
@Slf4j
public class HunyuanEmbeddingModel implements EmbeddingModel {

    private static final int DIMENSIONS = 1024;
    private final HunyuanClient client;

    /**
     * Create a new HunyuanEmbeddingModel.
     *
     * @param secretId Tencent Cloud Secret ID
     * @param secretKey Tencent Cloud Secret Key
     * @param region Tencent Cloud region (e.g., ap-guangzhou)
     */
    public HunyuanEmbeddingModel(String secretId, String secretKey, String region) {
        Assert.hasText(secretId, "Secret ID must not be empty");
        Assert.hasText(secretKey, "Secret Key must not be empty");
        Assert.hasText(region, "Region must not be empty");

        Credential cred = new Credential(secretId, secretKey);
        ClientProfile clientProfile = new ClientProfile();
        this.client = new HunyuanClient(cred, region, clientProfile);
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        List<String> inputs = request.getInstructions();
        List<float[]> embeddings = embed(inputs);

        List<Embedding> embeddingList = new java.util.ArrayList<>(embeddings.size());
        for (int i = 0; i < embeddings.size(); i++) {
            embeddingList.add(new Embedding(embeddings.get(i), i));
        }

        return new EmbeddingResponse(embeddingList);
    }

    @Override
    public float[] embed(Document document) {
        return embed(document.getText());
    }

    @Override
    public float[] embed(String text) {
        try {
            GetEmbeddingRequest req = new GetEmbeddingRequest();
            req.setInput(text);

            GetEmbeddingResponse resp = client.GetEmbedding(req);

            if (resp.getData() == null || resp.getData().length == 0) {
                throw new RuntimeException("Empty embedding response from Hunyuan API");
            }

            // Tencent API returns Float[] (object array), need to convert to float[] (primitive array)
            Float[] embeddingObj = resp.getData()[0].getEmbedding();
            float[] result = new float[embeddingObj.length];
            for (int i = 0; i < embeddingObj.length; i++) {
                result[i] = embeddingObj[i];
            }

            return result;

        } catch (TencentCloudSDKException e) {
            log.error("Failed to call Hunyuan Embedding API", e);
            throw new RuntimeException("Failed to generate embedding", e);
        }
    }

    @Override
    public List<float[]> embed(List<String> texts) {
        return texts.stream()
                .map(this::embed)
                .toList();
    }

    @Override
    public int dimensions() {
        return DIMENSIONS;
    }
}
