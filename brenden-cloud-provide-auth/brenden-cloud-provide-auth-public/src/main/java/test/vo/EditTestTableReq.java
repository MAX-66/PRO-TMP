package test.vo;

import com.brenden.cloud.base.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/6
 */
@Data
@Schema(description = "保存请求参数")
public class EditTestTableReq extends SaveTestTableReq {
    @Serial
    private static final long serialVersionUID = -1239613165574475667L;

    @Schema(description = "id")
    private Long id;
}
