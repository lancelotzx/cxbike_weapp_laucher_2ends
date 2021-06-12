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
 *
 * wj20210612 增加三个属性logourl\level\comment用于实现
 * 有道[https://note.youdao.com/s/Het3SozM:新增功能描述]中的功能
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

    /** 景区logo图片地址 */
    @Excel(name = "景区logo图片地址")
    private String logourl;

    /** 景区等级 */
    @Excel(name = "景区等级")
    private String level;

    /** 备注 */
    @Excel(name = "景区备注")
    private String comment;

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

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("scenicid", getScenicid())
            .append("name", getName())
            .append("status", getStatus())
            .append("iconserial", getIconserial())
            .append("sysIconList", getSysIconList())
                .append("logourl", getLogourl())
                .append("level", getLevel())
                .append("comment", getComment())
                .toString();
    }
}
