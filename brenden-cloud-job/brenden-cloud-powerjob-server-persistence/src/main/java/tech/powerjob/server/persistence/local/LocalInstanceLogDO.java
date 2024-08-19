package tech.powerjob.server.persistence.local;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import tech.powerjob.common.enums.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * 本地的运行时日志
 *
 * @author tjq
 * @since 2020/4/27
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "local_instance_log", indexes = {@Index(columnList = "instanceId")})
public class LocalInstanceLogDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long instanceId;
    /**
     * 日志时间
     */
    private Long logTime;
    /**
     * 日志级别 {@link LogLevel}
     */
    private Integer logLevel;
    /**
     * 日志内容
     */
    @Lob
    @Column(columnDefinition="TEXT")
    private String logContent;

    /**
     * 机器地址
     */
    private String workerAddress;
}
