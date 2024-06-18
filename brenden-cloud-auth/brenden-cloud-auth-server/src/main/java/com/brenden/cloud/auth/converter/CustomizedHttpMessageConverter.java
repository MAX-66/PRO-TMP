package com.brenden.cloud.auth.converter;

import com.brenden.cloud.entity.ResultEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>
 * 自定义AccessToken转换
 * </p>
 *
 * @author lxq
 * @since 2024/6/18
 */
public class CustomizedHttpMessageConverter extends AbstractHttpMessageConverter<OAuth2AccessTokenResponse> {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseParametersConverter = new DefaultOAuth2AccessTokenResponseMapConverter();

    private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP = new ParameterizedTypeReference<Map<String, Object>>() {
    };

    private final GenericHttpMessageConverter<Object> jsonMessageConverter = new MappingJackson2HttpMessageConverter();


    public CustomizedHttpMessageConverter() {
        super(DEFAULT_CHARSET, MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return OAuth2AccessTokenResponse.class.isAssignableFrom(clazz);
    }

    @Override
    protected OAuth2AccessTokenResponse readInternal(Class<? extends OAuth2AccessTokenResponse> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(OAuth2AccessTokenResponse tokenResponse, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            Map<String, Object> tokenResponseParameters = this.accessTokenResponseParametersConverter
                    .convert(tokenResponse);
            this.jsonMessageConverter.write(ResultEntity.success(tokenResponseParameters), STRING_OBJECT_MAP.getType(),
                    MediaType.APPLICATION_JSON, outputMessage);
        }
        catch (Exception ex) {
            throw new HttpMessageNotWritableException(
                    "An error occurred writing the OAuth 2.0 Access Token Response: " + ex.getMessage(), ex);
        }
    }
}
