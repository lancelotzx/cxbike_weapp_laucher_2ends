package com.ruoyi.system.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 景区对象 sys_spot
 * 
 * @author rf
 * @date 2021-06-05
 */
public class SysSpot extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String scenicid;

    /** 景区名称 */
    @Excel(name = "景区名称")
    private String name;

    /** 景区状态 */
    @Excel(name = "景区状态")
    private String status;

    /** 图标顺序，半角逗号分隔 */
    @Excel(name = "图标顺序，半角逗号分隔")
    private String iconserial;

    /** 图标信息 */
    private List<SysIcon> sysIconList;

    public void setScenicid(String scenicid) 
    {
        this.scenicid = scenicid;
    }

    public String getScenicid() 
    {
        return scenicid;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setIconserial(String iconserial) 
    {
        this.iconserial = iconserial;
    }

    public String getIconserial() 
    {
        return iconserial;
    }

    public List<SysIcon> getSysIconList()
    {
        return sysIconList;
    }

    public void setSysIconList(List<SysIcon> sysIconList)
    {
        this.sysIconList = sysIconList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("scenicid", getScenicid())
            .append("name", getName())
            .append("status", getStatus())
            .append("iconserial", getIconserial())
            .append("sysIconList", getSysIconList())
            .toString();
    }
}
