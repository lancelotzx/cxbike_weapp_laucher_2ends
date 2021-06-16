package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 模版生成用的数据对象，仅作为一个Object存取来使用
 *
 * @author RF
 * @date 2021-06-13
 */
public class SysSpotGen extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String scenicid;

    public String getScenicid() {
        return scenicid;
    }

    public void setScenicid(String scenicid) {
        this.scenicid = scenicid;
    }

    public String getNewSenicId() {
        return newSenicId;
    }

    public void setNewSenicId(String newSenicId) {
        this.newSenicId = newSenicId;
    }

    private String newSpotName;

    private String newSenicId;


    public String getNewSpotName() {
        return newSpotName;
    }

    public void setNewSpotName(String newSpotName) {
        this.newSpotName = newSpotName;
    }




}
