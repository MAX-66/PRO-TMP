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
public class SaveBatchTestTableReq extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -5096074532000825501L;

    @Schema(description = "字段A")
    private String fieldA;
    @Schema(description = "字段B")
    private String fieldB;
    @Schema(description = "字段C")
    private String fieldC;
    @Schema(description = "字段D")
    private String fieldD;

}
